package com.example.navegaocapacitairede

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaHome(
    onIrParaSobre: () -> Unit,
    onIrParaDetalhe: (String) -> Unit
) {
    val cursos = listOf("Android Básico", "Android Intermediário", "Android Avançado")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Home",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Escolha um curso para ver o detalhe:",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Para cada curso, cria um card clicável
        cursos.forEach { curso ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onIrParaDetalhe(curso) },   // passa o nome
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = curso,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))  // empurra o botão para baixo

        Button(
            onClick = onIrParaSobre,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sobre o app")
        }
    }
}