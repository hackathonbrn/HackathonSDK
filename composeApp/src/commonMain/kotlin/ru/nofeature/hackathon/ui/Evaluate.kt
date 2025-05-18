package ru.nofeature.hackathon.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.nofeature.hackathon.evaluate.api.*
import ru.nofeature.hackathon.evaluate.impl.InMemoryRatingRepository
import ru.nofeature.hackathon.evaluate.impl.ProjectWithTeam
import ru.nofeature.hackathon.evaluate.impl.SimpleCriterion
import ru.nofeature.hackathon.evaluate.impl.SimpleJudge
import ru.nofeature.hackathon.evaluate.impl.SimpleProject
import ru.nofeature.hackathon.evaluate.impl.SimpleProjectRating
import ru.nofeature.hackathon.evaluate.impl.SimpleRating
import ru.nofeature.hackathon.net.Ktor


@Composable
fun ProjectListScreen(
    onProjectSelected: (ProjectWithTeam) -> Unit,
    repository: InMemoryRatingRepository,
    judge: SimpleJudge
) {
    val projects by produceState<List<ProjectWithTeam>>(emptyList()) {
        value = repository.getProjects()
    }

    LaunchedEffect(Unit) {
        repository.judge = judge
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Добро пожаловать, ${judge.name}", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Проекты к оценке:", style = MaterialTheme.typography.headlineSmall)

        FlowColumn {
            projects.forEach { project ->
                ProjectCard(
                    project = project,
                    onClick = { onProjectSelected(project) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ProjectCard(
    project: ProjectWithTeam,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Команда: ${project.teamName}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(project.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun RateProjectScreen(
    project: ProjectWithTeam,
    onBack: () -> Unit,
    repository: InMemoryRatingRepository,
) {
    val criteria by produceState<List<Criterion>>(emptyList()) {
        value = repository.getCriteria()
    }

    var showSubmitDialog by remember { mutableStateOf(false) }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Оценка отправлена!") },
            text = { Text("Ваша оценка успешно отправлена!") },
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
                Text("Назад")
            }
            Text("Оценка проекта", style = MaterialTheme.typography.headlineSmall)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(project.name, style = MaterialTheme.typography.headlineSmall)
            }
        }

        Text("Оцениваем критерий:", style = MaterialTheme.typography.headlineSmall)

        var ratings : MutableList<Float> by remember { mutableStateOf(mutableListOf()) }

        criteria.forEachIndexed { index, criterion ->
            if (ratings.lastIndex < index) {
                ratings.add(0f)
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

                    Text("Баллы (0 - ${criterion.maxScore}):")
                    Slider(
                        value = ratings[index],
                        onValueChange = {
                            ratings = MutableList<Float>(ratings.size) { i ->
                                if (index == i) it else ratings[i]
                            }
                        },
                        valueRange = 0.0f..(criterion.maxScore.toFloat()),
                        steps = 9
                    )
                    Text("Выбрано: ${ratings[index].toString().take(4)}")
                }
            }
        }

        Button(
            onClick = {
                Ktor.addEvaluate(
                    SimpleProjectRating(
                        project = SimpleProject(project.name),
                        judge = repository.judge!!,
                        ratings = List(ratings.size) { index ->
                            SimpleRating(
                                SimpleCriterion(
                                    criteria[index].name,
                                    criteria[index].description,
                                    criteria[index].maxScore
                                ),
                                ratings[index].toDouble()
                            )
                        }
                    )
                )
                showSubmitDialog = true
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            enabled = true
        ) {
            Text("Отправить оценку")
        }
    }
}