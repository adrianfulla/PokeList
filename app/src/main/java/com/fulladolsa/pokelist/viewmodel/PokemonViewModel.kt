package com.fulladolsa.pokelist.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fulladolsa.pokelist.api.PokeApi
import com.fulladolsa.pokelist.model.PokemonDetail
import com.fulladolsa.pokelist.model.PokemonListItem
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    // Estados para la lista de Pokemon
    var pokemonList by mutableStateOf<List<PokemonListItem>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Estados para los detalles del Pokemon
    var selectedPokemon by mutableStateOf<PokemonDetail?>(null)
        private set

    var isLoadingDetail by mutableStateOf(false)
        private set

    var detailErrorMessage by mutableStateOf<String?>(null)
        private set

    // Paginación
    var currentOffset by mutableStateOf(0)
        private set

    private val pageSize = 20

    var canLoadMore by mutableStateOf(true)
        private set

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = PokeApi.retrofitService.getPokemonList(pageSize, currentOffset)
                pokemonList = pokemonList + response.results
                currentOffset += pageSize
                canLoadMore = response.next != null
            } catch (e: Exception) {
                errorMessage = "Error cargando la lista de Pokemon: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            isLoadingDetail = true
            detailErrorMessage = null

            try {
                selectedPokemon = PokeApi.retrofitService.getPokemonDetail(id)
            } catch (e: Exception) {
                detailErrorMessage = "Error cargando los detalles del Pokemon: ${e.message}"
            } finally {
                isLoadingDetail = false
            }
        }
    }

    fun clearSelectedPokemon() {
        selectedPokemon = null
        detailErrorMessage = null
    }
}