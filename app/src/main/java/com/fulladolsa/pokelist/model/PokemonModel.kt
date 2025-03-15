package com.fulladolsa.pokelist.model

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
) {
    // Extraer el ID del Pokemon desde la URL
    fun getImageUrl(): String {
        val id = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
    }

    fun getId(): Int {
        return url.split("/".toRegex()).dropLast(1).last().toInt()
    }
}

// Modelo para los detalles del Pokemon
data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeSlot>,
    val stats: List<PokemonStat>,
    val abilities: List<PokemonAbility>,
    val sprites: PokemonSprites
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: PokemonType
)

data class PokemonType(
    val name: String,
    val url: String
)

data class PokemonStat(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
)

data class Stat(
    val name: String,
    val url: String
)

data class PokemonAbility(
    val ability: Ability,
    val is_hidden: Boolean,
    val slot: Int
)

data class Ability(
    val name: String,
    val url: String
)

data class PokemonSprites(
    val front_default: String,
    val back_default: String,
    val front_shiny: String,
    val back_shiny: String
)