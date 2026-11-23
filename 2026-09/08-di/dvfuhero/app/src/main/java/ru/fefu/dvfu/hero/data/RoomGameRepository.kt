package ru.fefu.dvfu.hero.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room3.withWriteTransaction
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import ru.fefu.dvfu.hero.data.db.HeroDatabase
import ru.fefu.dvfu.hero.data.db.HeroEntity
import ru.fefu.dvfu.hero.data.db.ItemEntity
import ru.fefu.dvfu.hero.model.Game
import ru.fefu.dvfu.hero.model.Item
import ru.fefu.dvfu.hero.model.ItemDetails
import ru.fefu.dvfu.hero.model.ItemLoadException
import ru.fefu.dvfu.hero.model.ItemNotFoundException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// Помним пока делаем допущение чтот так ок. Но в будущем никакого mutableState в дата слое
@Singleton
class RoomGameRepository @Inject constructor(
    private val api: HeroApi,
    private val db: HeroDatabase,
    private val scope: CoroutineScope,
) : GameRepository {
    private val dao = db.gameDao()

    override var game: Game? by mutableStateOf(null)
        private set

    init {
        write { if (dao.hp() == null) newGame() }
    }

    override suspend fun item(id: Int): Item? = dao.item(id)?.toItem()

    override fun reset() = write { newGame() }

    override fun damage() = changeHp { it - 10 }

    override fun heal() = changeHp { if (it == 0) 0 else it + 10 }

    override fun useItem(id: Int) = write {
        val hp = dao.hp() ?: return@write
        if (hp == 0) return@write
        val item = dao.item(id) ?: return@write
        dao.deleteItem(id)
        dao.saveHero(HeroEntity(hp = (hp + item.heal).coerceAtMost(MAX_HP)))
    }

    override suspend fun itemDetails(item: Item): ItemDetails = try {
        retryIO { api.magicItem(item.apiIndex) }.toDetails()
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        throw ItemLoadException(canRetry = true, e)
    } catch (e: HttpException) {
        if (e.code() == 404) throw ItemNotFoundException()
        throw ItemLoadException(canRetry = e.code() >= 500, e)
    } catch (e: SerializationException) {
        Log.e("RoomGameRepository", "Ответ сервера не совпал с DTO: ${item.apiIndex}", e)
        throw ItemLoadException(canRetry = false, e)
    }

    private fun write(action: suspend () -> Unit) {
        scope.launch {
            game = db.withWriteTransaction {
                action()
                readGame()
            }
        }
    }

    private suspend fun readGame(): Game? {
        val hp = dao.hp() ?: return null
        return Game(hp, dao.items().map { it.toItem() })
    }

    private suspend fun newGame() {
        dao.deleteAllItems()
        dao.insertItems(startItems())
        dao.saveHero(HeroEntity(hp = MAX_HP))
    }

    private fun changeHp(change: (Int) -> Int) = write {
        val hp = dao.hp() ?: return@write
        dao.saveHero(HeroEntity(hp = change(hp).coerceIn(0, MAX_HP)))
    }
}

private fun ItemEntity.toItem() = Item(id = id, name = name, heal = heal, apiIndex = apiIndex)

private fun MagicItemDto.toDetails() = ItemDetails(
    title = name,
    rarity = rarity.name,
    description = description.drop(1).joinToString("\n\n"),
)
