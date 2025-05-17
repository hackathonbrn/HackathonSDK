package ru.nofeature.hackathon

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.nofeature.hackathon.evaluate.api.RatingRepository
import ru.nofeature.hackathon.evaluate.impl.InMemoryRatingRepository
import ru.nofeature.hackathon.evaluate.impl.ProjectWithTeam
import ru.nofeature.hackathon.evaluate.impl.SimpleJudge
import ru.nofeature.hackathon.evaluate.impl.SimpleProject
import ru.nofeature.hackathon.net.Ktor
import ru.nofeature.hackathon.team.impl.SimpleTeam
import ru.nofeature.hackathon.ui.ProjectListScreen
import ru.nofeature.hackathon.ui.ProjectsReportScreen
import ru.nofeature.hackathon.ui.RateProjectScreen
import ru.nofeature.hackathon.ui.RegistrationForm
import ru.nofeature.hackathon.users.Roles


@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val repository: InMemoryRatingRepository by remember { mutableStateOf(InMemoryRatingRepository()) }

        NavHost(navController, startDestination = "report") {
            composable("registration") {
                RegistrationForm {
                    Ktor.addTeammate(
                        SimpleTeam(it.name, it.command, it.project, it.role)
                    )
                    if (it.role == Roles.JUDGE.title) {
                        navController.navigate(SimpleJudge(it.name))
                    }
                }
            }

            composable<SimpleJudge> { backStackEntry ->
                ProjectListScreen(
                    onProjectSelected = { judge ->
                        navController.navigate(judge)
                    },
                    repository = repository,
                    judge = backStackEntry.toRoute()
                )
            }

            composable<ProjectWithTeam> { backStackEntry ->
                RateProjectScreen(
                    project = backStackEntry.toRoute(),
                    onBack = { navController.popBackStack() },
                    repository = repository,
                )
            }
            composable("report") {
                ProjectsReportScreen()
            }
        }

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
}