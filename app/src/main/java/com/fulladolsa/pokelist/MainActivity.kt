package com.fulladolsa.pokelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fulladolsa.pokelist.screens.PokemonDetailScreen
import com.fulladolsa.pokelist.screens.PokemonListScreen
import com.fulladolsa.pokelist.ui.theme.PokeListTheme
import com.fulladolsa.pokelist.viewmodel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokeListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokeListApp()
                }
            }
        }
    }
}

@Composable
fun PokeListApp() {
    val navController = rememberNavController()
    val pokemonViewModel: PokemonViewModel = viewModel()

    NavHost(navController = navController, startDestination = "pokemon_list") {
        composable("pokemon_list") {
            PokemonListScreen(
                viewModel = pokemonViewModel,
                onPokemonSelected = { pokemonId ->
                    navController.navigate("pokemon_detail/$pokemonId")
                }
            )
        }

        composable(
            route = "pokemon_detail/{pokemonId}",
            arguments = listOf(
                navArgument("pokemonId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 1
            PokemonDetailScreen(
                pokemonId = pokemonId,
                viewModel = pokemonViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}