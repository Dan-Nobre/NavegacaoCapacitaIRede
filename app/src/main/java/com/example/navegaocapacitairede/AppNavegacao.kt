package com.example.navegaocapacitairede

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navegaocapacitairede.Screen.Detalhe.createRoute

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
            TelaHome(
                onIrParaSobre = {
                    navController.navigate(Screen.Sobre.route)
                },
                onIrParaDetalhe = { nome ->
                    navController.navigate(Screen.Detalhe.createRoute(nome))
                }
            )
        }

        // Registra a rota "sobre"
        composable(Screen.Sobre.route) {
            TelaSobre(
                onVoltar = { navController.popBackStack() }
            )
        }

        // Registra a rota "detalhe/{nome}" — recebe um argumento
        composable(
            route = Screen.Detalhe.route,
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // backStackEntry é o "bloco de pedido" com os dados que vieram junto
            val nome = backStackEntry.arguments?.getString("nome") ?: "Desconhecido"
            TelaDetalhe(
                nome = nome,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}