package com.example.navegaocapacitairede

sealed class Screen {
    object Home    : Screen("home")
    object Sobre   : Screen("sobre")
    object Detalhe : Screen("detalhe/{nome}") {
        fun createRoute(nome: String) = "detalhe/$nome"
    }
}

//  sealed class é uma classe que só pode ter um número fixo de filhos — todos declarados dentro dela.
//  É como um enum, mas mais poderoso porque cada filho pode ter comportamento próprio.
//  todo filho obrigatoriamente recebe uma String de rota no construtor. Não tem como criar um `Screen` sem rota.
//  object Home : Screen("home") significa que é um singleton: existe apenas uma instância de Home no app inteiro.