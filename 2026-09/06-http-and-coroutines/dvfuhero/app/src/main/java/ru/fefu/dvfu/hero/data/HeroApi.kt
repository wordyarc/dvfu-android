package ru.fefu.dvfu.hero.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroApi {
    @GET("magic-items/{index}")
    suspend fun magicItem(@Path("index") index: String): MagicItemDto
}

@Serializable
data class MagicItemDto(
    val name: String,
    val rarity: RarityDto,
    @SerialName("desc") val description: List<String> = emptyList(),
)

@Serializable
data class RarityDto(val name: String)
