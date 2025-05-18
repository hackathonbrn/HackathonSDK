package ru.nofeature.hackathon

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.nofeature.hackathon.evaluate.impl.InMemoryRatingRepository
import ru.nofeature.hackathon.evaluate.impl.ProjectWithTeam
import ru.nofeature.hackathon.evaluate.impl.SimpleJudge
import ru.nofeature.hackathon.hackInfo.createSampleHackathon
import ru.nofeature.hackathon.net.Ktor
import ru.nofeature.hackathon.team.impl.SimpleTeam
import ru.nofeature.hackathon.ui.*
import ru.nofeature.hackathon.users.Roles


@Composable
@Preview
fun App() {
    MaterialTheme {
        val repository: InMemoryRatingRepository by lazy { InMemoryRatingRepository() }

        val navController = rememberNavController()

        NavHost(navController, startDestination = "start") {
            composable("start") {
                HackathonLandingPage(
                    createSampleHackathon(),
                    registerClick = { navController.navigate("registration") },
                    reportClick = { navController.navigate("report") },
                )
            }
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
    }
}