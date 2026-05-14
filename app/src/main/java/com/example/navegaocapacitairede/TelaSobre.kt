package com.example.navegaocapacitairede

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaSobre(onVoltar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Button(onClick = onVoltar) {
            Text("← Voltar")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "ℹ️ Sobre",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Esse app é um exemplo de Navegação com Jetpack Compose.\n\n" +
                    "Conceitos usados:\n" +
                    "• NavController\n" +
                    "• NavHost\n" +
                    "• composable(\"rota\")\n" +
                    "• Argumentos na rota\n" +
                    "• Sealed class para rotas",
            fontSize = 15.sp,
            lineHeight = 24.sp
        )
    }
}