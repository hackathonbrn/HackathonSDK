package ru.nofeature.hackathon.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import ru.nofeature.hackathon.evaluate.api.Criterion
import ru.nofeature.hackathon.evaluate.api.Judge
import ru.nofeature.hackathon.evaluate.api.Project
import ru.nofeature.hackathon.evaluate.api.ProjectRating
import ru.nofeature.hackathon.evaluate.api.Rating
import ru.nofeature.hackathon.evaluate.api.RatingRepository
import ru.nofeature.hackathon.evaluate.impl.InMemoryRatingRepository
import ru.nofeature.hackathon.evaluate.impl.SimpleProject
import ru.nofeature.hackathon.evaluate.impl.SimpleProjectRating
import ru.nofeature.hackathon.evaluate.impl.SimpleRating

@Composable
fun EvaluateScreen(
    repository: RatingRepository = InMemoryRatingRepository(),
    judgeId: String = "j1" // В реальном приложении получаем после авторизации
) {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "projects") {
            composable("projects") {
                ProjectListScreen(
                    onProjectSelected = { project ->
                        navController.navigate(project)
                    },
                    repository = repository,
                    judgeId = judgeId
                )
            }
            composable<SimpleProject> { backStackEntry ->
                RateProjectScreen(
                    project = backStackEntry.toRoute(),
                    onBack = { navController.popBackStack() },
                    repository = repository,
                    judgeId = judgeId
                )
            }
        }
    }
}

@Composable
fun ProjectListScreen(
    onProjectSelected: (SimpleProject) -> Unit,
    repository: RatingRepository,
    judgeId: String
) {
    val projects by produceState<List<Project>>(emptyList()) {
        value = repository.getProjectsToJudge(judgeId)
    }

    val judge by produceState<Judge?>(null) {
        value = repository.getJudge(judgeId)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        judge?.let {
            Text("Welcome, ${it.name}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text("Projects to judge:", style = MaterialTheme.typography.headlineSmall)

        LazyColumn {
            items(projects) { project ->
                ProjectCard(
                    project = project,
                    onClick = { onProjectSelected(project as SimpleProject) },
                    repository = repository,
                    judgeId = judgeId
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    repository: RatingRepository,
    judgeId: String
) {
    val existingRating by produceState<ProjectRating?>(null) {
        value = repository.getExistingRating(project.id, judgeId)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(project.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            existingRating?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Score: ${it.ratings.sumOf { r -> r.score }}/${
                        it.ratings.sumOf { r ->
                            repository.getCriteria().first { c -> c.name == r.criterion.name }.maxScore
                        }
                    }",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RateProjectScreen(
    project: SimpleProject,
    onBack: () -> Unit,
    repository: RatingRepository,
    judgeId: String
) {
    val project by produceState<Project?>(null) {
        value = repository.getProjectsToJudge(judgeId).firstOrNull { it.id == project.id }
    }
    val criteria by produceState<List<Criterion>>(emptyList()) {
        value = repository.getCriteria()
    }
    val existingRating by produceState<ProjectRating?>(null) {
        value = repository.getExistingRating(project?.id ?: "1j", judgeId)
    }

    var showSubmitDialog by remember { mutableStateOf(false) }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Rating Submitted") },
            text = { Text("Your rating has been successfully submitted!") },
            confirmButton = {
                Button(onClick = {
                    showSubmitDialog = false
                    onBack()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("Back")
            }
            Text("Rate Project", style = MaterialTheme.typography.headlineSmall)
        }

        project?.let { p ->
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(p.name, style = MaterialTheme.typography.headlineSmall)
                }
            }

            Text("Evaluation Criteria:", style = MaterialTheme.typography.headlineSmall)

            criteria.forEachIndexed { index, criterion ->
                var rating by remember {
                    mutableStateOf( 0f)
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(criterion.name, style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(criterion.description, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Score (0 - ${criterion.maxScore}):")
                        Slider(
                            value = rating,
                            onValueChange = {
                                rating = it
                            },
                            valueRange = 0.0f..(criterion.maxScore.toFloat()),
                            steps = 9
                        )
                        Text("Selected: $rating")
                    }
                }
            }

            Button(
                onClick = {

                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                enabled = true
            ) {
                Text("Submit Rating")
            }
        } ?: run {
            Text("Project not found", color = MaterialTheme.colorScheme.error)
            Button(onClick = onBack) {
                Text("Back to projects")
            }
        }
    }
}