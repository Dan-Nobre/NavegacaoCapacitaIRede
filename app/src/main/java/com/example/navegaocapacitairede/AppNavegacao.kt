package com.example.navegaocapacitairede

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavegacao() {

    // cria o NavController e usa o remember por baixo dos panos para memorizá-lo entre recomposições.
    // Se o Compose redesenhar a tela por qualquer motivo, o mesmo NavController é reutilizado
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            TelaHome()
        }
    }
}