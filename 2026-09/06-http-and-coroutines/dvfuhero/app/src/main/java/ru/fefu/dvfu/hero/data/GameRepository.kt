package ru.fefu.dvfu.hero.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import ru.fefu.dvfu.hero.model.Item
import ru.fefu.dvfu.hero.model.ItemDetails
import ru.fefu.dvfu.hero.model.ItemLoadException
import ru.fefu.dvfu.hero.model.ItemNotFoundException
import java.io.IOException

const val MAX_HP = 100

// Помним пока делаем допущение чтот так ок. Но в будущем никакого mutableState в дата слое
class GameRepository(private val api: HeroApi) {

    var hp by mutableStateOf(MAX_HP)
        private set

    var items by mutableStateOf(startItems())
        private set

    fun item(id: Int): Item? = items.find { it.id == id }

    fun damage() {
        updateHp(hp - 10)
    }

    fun heal() {
        if (hp == 0) return
        updateHp(hp + 10)
    }

    fun useItem(id: Int) {
        if (hp == 0) return
        val item = item(id) ?: return
        items = items - item
        updateHp(hp + item.heal)
    }

    fun reset() {
        hp = MAX_HP
        items = startItems()
    }

    suspend fun itemDetails(item: Item): ItemDetails = try {
        retryIO { api.magicItem(item.apiIndex) }.toDetails()
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        throw ItemLoadException(canRetry = true, e)
    } catch (e: HttpException) {
        if (e.code() == 404) throw ItemNotFoundException()
        throw ItemLoadException(canRetry = e.code() >= 500, e)
    } catch (e: SerializationException) {
        Log.e("GameRepository", "Ответ сервера не совпал с DTO: ${item.apiIndex}", e)
        throw ItemLoadException(canRetry = false, e)
    }

    private fun updateHp(value: Int) {
        hp = value.coerceIn(0, MAX_HP)
    }
}

private fun MagicItemDto.toDetails() = ItemDetails(
    title = name,
    rarity = rarity.name,
    description = description.drop(1).joinToString("\n\n"),
)
