package com.fulladolsa.pokelist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fulladolsa.pokelist.model.PokemonDetail
import com.fulladolsa.pokelist.model.PokemonStat
import com.fulladolsa.pokelist.model.PokemonTypeSlot
import com.fulladolsa.pokelist.viewmodel.PokemonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonViewModel,
    onBack: () -> Unit
) {
    // Cargar detalles del Pokemon
    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Pokémon") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.clearSelectedPokemon()
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (viewModel.isLoadingDetail) {
                // Mostrar carga
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            } else if (viewModel.detailErrorMessage != null) {
                // Mostrar error
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.detailErrorMessage ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadPokemonDetail(pokemonId) }) {
                        Text("Reintentar")
                    }
                }
            } else {
                // Mostrar detalles del Pokemon
                viewModel.selectedPokemon?.let { pokemon ->
                    PokemonDetailContent(pokemon = pokemon)
                }
            }
        }
    }
}

@Composable
fun PokemonDetailContent(pokemon: PokemonDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera con nombre e ID
        Text(
            text = pokemon.name.capitalize(Locale.current),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "#${pokemon.id}",
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Imagen del Pokemon
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = pokemon.sprites.front_default,
                contentDescription = "Pokemon ${pokemon.name}",
                modifier = Modifier
                    .size(180.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tipos del Pokemon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            pokemon.types.forEach { typeSlot ->
                PokemonTypeChip(typeSlot)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Características físicas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PhysicalAttribute(label = "Altura", value = "${pokemon.height / 10.0} m")
            PhysicalAttribute(label = "Peso", value = "${pokemon.weight / 10.0} kg")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Separador
        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        // Estadísticas
        Text(
            text = "Estadísticas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        pokemon.stats.forEach { stat ->
            StatRow(stat = stat)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Habilidades
        Text(
            text = "Habilidades",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            pokemon.abilities.forEach { ability ->
                Text(
                    text = ability.ability.name.capitalize(Locale.current) +
                            if (ability.is_hidden) " (Oculta)" else "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun PokemonTypeChip(typeSlot: PokemonTypeSlot) {
    val typeColor = when (typeSlot.type.name) {
        "normal" -> Color(0xFFA8A878)
        "fire" -> Color(0xFFF08030)
        "water" -> Color(0xFF6890F0)
        "electric" -> Color(0xFFF8D030)
        "grass" -> Color(0xFF78C850)
        "ice" -> Color(0xFF98D8D8)
        "fighting" -> Color(0xFFC03028)
        "poison" -> Color(0xFFA040A0)
        "ground" -> Color(0xFFE0C068)
        "flying" -> Color(0xFFA890F0)
        "psychic" -> Color(0xFFF85888)
        "bug" -> Color(0xFFA8B820)
        "rock" -> Color(0xFFB8A038)
        "ghost" -> Color(0xFF705898)
        "dragon" -> Color(0xFF7038F8)
        "dark" -> Color(0xFF705848)
        "steel" -> Color(0xFFB8B8D0)
        "fairy" -> Color(0xFFEE99AC)
        else -> Color.Gray
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = typeColor
    ) {
        Text(
            text = typeSlot.type.name.capitalize(Locale.current),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PhysicalAttribute(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatRow(stat: PokemonStat) {
    val statName = when (stat.stat.name) {
        "hp" -> "PS"
        "attack" -> "Ataque"
        "defense" -> "Defensa"
        "special-attack" -> "Ataque Esp."
        "special-defense" -> "Defensa Esp."
        "speed" -> "Velocidad"
        else -> stat.stat.name.capitalize(Locale.current)
    }

    val statColor = when (stat.stat.name) {
        "hp" -> Color(0xFFFF5959)
        "attack" -> Color(0xFFF5AC78)
        "defense" -> Color(0xFFFAE078)
        "special-attack" -> Color(0xFF9DB7F5)
        "special-defense" -> Color(0xFFA7DB8D)
        "speed" -> Color(0xFFFA92B2)
        else -> MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = statName,
                fontSize = 16.sp,
                modifier = Modifier.width(120.dp)
            )

            Text(
                text = stat.base_stat.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = stat.base_stat / 255f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = statColor,
            trackColor = Color.LightGray
        )
    }
}