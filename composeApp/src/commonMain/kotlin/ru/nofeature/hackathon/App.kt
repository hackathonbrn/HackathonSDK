package ru.nofeature.hackathon

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.nofeature.hackathon.ui.RegistrationForm


@Composable
@Preview
fun App() {
    RegistrationForm{

    }

//        EvaluateScreen()
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
}