package com.example.navegaocapacitairede

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaDetalhe(nome: String, onVoltar: () -> Unit) {
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
            text = "📋 Detalhe",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe o argumento que veio pela rota
        Text(
            text = "Você selecionou:",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = nome,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A73E8)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card mostrando o que aconteceu "por baixo dos panos"
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8F0FE)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🔍 O que aconteceu aqui?",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A73E8)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A rota chamada foi:\n\"detalhe/$nome\"\n\n" +
                            "O NavHost leu o argumento {nome}\n" +
                            "do backStackEntry e repassou\npara essa tela.",
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}