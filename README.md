
<img width="1002" height="254" alt="Frame 4" src="https://github.com/user-attachments/assets/cc33c7b6-9256-48cf-9a34-5cb9ca6ea6e0" />

# Navegação com Jetpack Compose

Material de apoio ao código do repositório — Capacita iRede, Módulo Intermediário.

---

## Os ingredientes para a navegação

### NavController

É o **cérebro** da navegação. Sabe qual tela está ativa, guarda o histórico (back stack) e executa as transições.

> **Regra importante:** o NavController deve ser criado no nível mais alto da árvore de composables e passado para baixo. Nunca crie um NavController dentro de um composable filho — você perderia o histórico.

---

### NavHost

É o **container** — o espaço da tela onde os composables de cada rota são exibidos.

O `NavHost` ouve o `NavController` e troca o composable exibido quando a rota muda.

---

### Composable

Dentro do `NavHost`, cada bloco `composable("rota") { ... }` **registra** uma tela com um endereço.

> Pense nos blocos `composable(...)` como as páginas de um livro. O `NavHost` é o livro. O `NavController` é o dedo que vira as páginas.

---

## Passo 1 — Importando a dependência

```kotlin
dependencies {
    implementation("androidx.navigation:navigation-compose:2.7.7")
}
```

---

## Passo 2 — Criar a Sealed Class

```kotlin
sealed class Screen(val route: String) {
    object Home    : Screen("home")
    object Sobre   : Screen("sobre")
    object Detalhe : Screen("detalhe/{nome}") {
        fun createRoute(nome: String) = "detalhe/$nome"
    }
}
```

`sealed class` é uma classe que só pode ter um número fixo de filhos — todos declarados dentro dela. É como um enum, mas mais poderoso porque cada filho pode ter comportamento próprio.

`val route: String` — todo filho obrigatoriamente recebe uma String de rota no construtor. Não tem como criar um `Screen` sem rota.

`object Home : Screen("home")` — `object` significa que é um **singleton**: existe apenas uma instância de `Home` no app inteiro. Faz sentido porque você não precisa de dois objetos "Home" — é sempre o mesmo destino.

### O que é `object`?

`object` cria uma classe e **já instancia ela automaticamente** — e garante que existe **só uma instância** no app inteiro. Isso se chama **singleton**.

```kotlin
object Home : Screen("home")

// É equivalente a escrever isso:
class Home private constructor() : Screen("home") {
    companion object {
        val instance = Home()  // criada uma vez, nunca mais
    }
}
```

O `object` é só o atalho do Kotlin para não ter que escrever tudo isso.

```kotlin
// Sem object — você poderia criar várias instâncias (não faz sentido)
val rota1 = Home()
val rota2 = Home()  // pra quê isso? são a mesma rota

// Com object — existe só uma, você acessa diretamente
Screen.Home.route  // sempre o mesmo objeto
```

`Screen("home")` — está chamando o construtor da `sealed class` passando `"home"` como rota. Então `Screen.Home.route` retorna `"home"`.

No `Detalhe`, além da rota `"detalhe/{nome}"`, tem um método extra:

```kotlin
fun createRoute(nome: String) = "detalhe/$nome"
```

O `{nome}` na rota é o **placeholder** — é onde o argumento vai ser lido pelo NavHost. O `createRoute` monta a URL real substituindo o placeholder pelo valor: `createRoute("Android Básico")` retorna `"detalhe/Android Básico"`.

> **Por que não usar String direto?** Sem a sealed class, você escreveria `"home"` em vários lugares. Se um dia mudar para `"tela_home"`, teria que caçar todas as ocorrências. Com a sealed class, muda em um lugar só.

---

## Passo 3 — Iniciando a navegação na MainActivity

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavegacao()
            }
        }
    }
}
```

- `ComponentActivity` — a versão moderna de `AppCompatActivity`, recomendada para projetos com Compose.
- `setContent { }` — substitui o `setContentView(R.layout....)` do mundo XML. Em vez de apontar para um arquivo XML, você passa um bloco de composables diretamente.
- `MaterialTheme { }` — aplica o tema do Material Design (cores, tipografia, formas) em todos os composables dentro dele. Sem ele o app funciona, mas fica sem estilo padrão.
- `AppNavegacao()` — a Activity não sabe nada sobre telas, rotas ou navegação. Ela delega tudo para esse composable. A Activity fica enxuta de propósito.

---

## Passo 4 — Montando o NavHost

```kotlin
@Composable
fun AppNavegacao() {
    val navController = rememberNavController()
```

`rememberNavController()` — cria o NavController e usa o `remember` por baixo dos panos para memorizá-lo entre recomposições. Se o Compose redesenhar a tela por qualquer motivo, o mesmo NavController é reutilizado — não perde o histórico de navegação.

```kotlin
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
```

`NavHost` recebe dois parâmetros obrigatórios:

- `navController` — quem vai mandar os comandos de navegação
- `startDestination` — qual rota exibir quando o app abrir

O bloco `{ }` depois é onde você **registra todas as rotas**. O NavHost lê esse bloco e monta o "cardápio" de telas disponíveis.

### Registrando a rota Home

```kotlin
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
```

- `composable(Screen.Home.route)` — registra a rota `"home"`. Quando o NavController navegar para `"home"`, o NavHost executa o bloco dentro e exibe `TelaHome`.
- `onIrParaSobre = { navController.navigate(...) }` — a `TelaHome` não recebe o NavController. Ela recebe uma **lambda** que já sabe o que fazer. A tela só chama `onIrParaSobre()` — quem decide que isso vira um `navigate` é o `AppNavegacao`.
- `onIrParaDetalhe = { nome -> ... }` — essa lambda recebe um parâmetro `nome`. Quando a `TelaHome` chamar `onIrParaDetalhe("Android Básico")`, o `AppNavegacao` vai executar `navController.navigate("detalhe/Android Básico")`.

### Registrando a rota Sobre

```kotlin
        composable(Screen.Sobre.route) {
            TelaSobre(
                onVoltar = { navController.popBackStack() }
            )
        }
```

`popBackStack()` — remove a tela atual da pilha e volta para a anterior. É o equivalente do botão voltar do celular feito por código.

**Como funciona o back stack aqui:**

```
Abre o app:        [Home]
navigate("sobre"): [Home] [Sobre]
popBackStack():    [Home]
```

### Registrando a rota Detalhe (com argumento)

```kotlin
        composable(
            route = Screen.Detalhe.route,
            arguments = listOf(
                navArgument("nome") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nome = backStackEntry.arguments?.getString("nome") ?: "Desconhecido"
            TelaDetalhe(
                nome = nome,
                onVoltar = { navController.popBackStack() }
            )
        }
```

- `route = Screen.Detalhe.route` — a rota é `"detalhe/{nome}"`. O `{nome}` entre chaves é o placeholder que o NavHost vai associar ao argumento.
- `arguments = listOf(navArgument("nome") { type = NavType.StringType })` — declara que essa rota espera um argumento chamado `"nome"` do tipo String. Isso é obrigatório para o NavHost saber como extrair o valor da URL.
- `{ backStackEntry -> }` — o bloco recebe um parâmetro `backStackEntry`. Ele é o "bilhete de entrada" dessa tela — contém os argumentos que vieram junto na navegação.
- `backStackEntry.arguments?.getString("nome")` — abre o bundle de argumentos e pega o valor pela chave `"nome"`. O `?.` é null safety: se por algum motivo `arguments` for null, não trava.
- `?: "Desconhecido"` — operador Elvis: se `getString` retornar null, usa `"Desconhecido"` como padrão.

---

## Passo 5 — As telas

> **Repare:** nenhuma tela recebe o NavController diretamente! Elas recebem lambdas — boa prática que reduz o acoplamento. A tela não sabe **como** navegar. Ela só avisa que algo aconteceu.

### TelaHome

```kotlin
@Composable
fun TelaHome(
    onIrParaSobre: () -> Unit,
    onIrParaDetalhe: (String) -> Unit
) {
```

- `onIrParaSobre: () -> Unit` — função que não recebe nada e não retorna nada. Só avisa que o botão "Sobre" foi clicado.
- `onIrParaDetalhe: (String) -> Unit` — função que recebe uma String (o nome do curso) e não retorna nada. Quem a chama passa o nome, quem a implementa decide o que fazer.

```kotlin
    val cursos = listOf("Android Básico", "Android Intermediário", "Android Avançado")
```

Lista simples de dados. No mundo real viria de um ViewModel ou API, mas para o exemplo de navegação isso é suficiente e não distrai do foco.

```kotlin
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
```

- `Column` — organiza os filhos verticalmente, um embaixo do outro.
- `Modifier.fillMaxSize()` — ocupa toda a largura e altura disponível.
- `.padding(24.dp)` — espaçamento interno de 24dp em todos os lados. O `.` encadeia os modificadores — a ordem importa.

```kotlin
        cursos.forEach { curso ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onIrParaDetalhe(curso) },
```

- `cursos.forEach { curso -> }` — para cada item da lista, executa o bloco. É a função de alta ordem vista na Unidade 1.
- `Modifier.clickable { onIrParaDetalhe(curso) }` — torna o Card clicável. Quando clicado, chama a lambda passando o nome do curso. Esse `curso` vem do `forEach` — cada card passa seu próprio nome.

```kotlin
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onIrParaSobre,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sobre o app")
        }
```

- `Spacer(Modifier.weight(1f))` — ocupa todo o espaço vazio disponível entre os cards e o botão. O `weight(1f)` diz: "pegue tudo que sobrou". Resultado: o botão "Sobre" fica sempre no fundo da tela, independente de quantos cards existam.
- `onClick = onIrParaSobre` — passa a lambda diretamente, sem criar uma nova. É equivalente a `onClick = { onIrParaSobre() }`, só mais enxuto.

---

### TelaSobre

```kotlin
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
```

- `FontWeight.Bold` — deixa o texto em negrito. Outras opções: `SemiBold`, `Medium`, `Light`.
- `fontSize = 28.sp` — `sp` (scale-independent pixels) é a unidade correta para textos. Ela respeita a configuração de tamanho de fonte do usuário no sistema. Para tamanhos de layout use `dp`, para fontes use `sp`.

---

### TelaDetalhe

```kotlin
@Composable
fun TelaDetalhe(nome: String, onVoltar: () -> Unit) {
```

Recebe `nome: String` — o valor que veio da rota, já extraído do `backStackEntry` lá no `AppNavegacao`. A tela não sabe que veio de uma rota, não sabe o que é `backStackEntry`. Ela só recebe uma String. Isso é o desacoplamento na prática.

```kotlin
        Text(
            text = nome,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A73E8)
        )
```

`Color(0xFF1A73E8)` — cor em hexadecimal no formato ARGB. O `0xFF` no início é o canal alpha (opacidade) — `FF` significa totalmente opaco. `1A73E8` é o azul do Google.

```kotlin
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
            }
        }
```

`CardDefaults.cardColors(containerColor = ...)` — customiza a cor de fundo do Card. O `CardDefaults` é um objeto que fornece os valores padrão do Card, e você sobrescreve só o que quer mudar.

---

## Fluxo completo

```
1. App abre
   └── MainActivity.onCreate()
       └── setContent { AppNavegacao() }
           └── rememberNavController() cria o NavController
           └── NavHost registra as 3 rotas
           └── startDestination = "home" → exibe TelaHome

2. Usuário clica em "Android Básico"
   └── Card.clickable { onIrParaDetalhe("Android Básico") }
       └── AppNavegacao recebe o nome
           └── navController.navigate("detalhe/Android Básico")
               └── NavHost encontra a rota "detalhe/{nome}"
                   └── backStackEntry.arguments.getString("nome") = "Android Básico"
                       └── TelaDetalhe(nome = "Android Básico") é exibida

3. Back stack nesse momento:
   [Home] [Detalhe]

4. Usuário clica em "← Voltar"
   └── onVoltar() → navController.popBackStack()
       └── Remove Detalhe do back stack
           └── Home volta a ser exibida

5. Back stack:
   [Home]

6. Usuário clica em "Sobre o app"
   └── onIrParaSobre() → navController.navigate("sobre")
       └── [Home] [Sobre]

7. Usuário aperta o botão voltar do celular
   └── Sistema chama popBackStack() automaticamente
       └── [Home]
```

---

*Capacita iRede — Módulo Intermediário Android*
