package ru.nofeature.hackathon.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.nofeature.hackathon.evaluate.impl.InMemoryRatingRepository
import ru.nofeature.hackathon.evaluate.impl.SimpleCriterion


@Composable
fun CriteriaScreen(
    onAddCriterion: (SimpleCriterion) -> Unit,
    onDeleteCriterion: (SimpleCriterion) -> Unit,
    key : String,
    repository: InMemoryRatingRepository,
    modifier: Modifier = Modifier
) {
    val criteria: List<SimpleCriterion> by produceState<List<SimpleCriterion>>(emptyList(), key) {
        value = repository.getCriteria()
    }

    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {
        // Заголовок и кнопка добавления
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Критерии оценки",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить критерий")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Список критериев
        if (criteria.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет критериев. Добавьте первый критерий.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(criteria) { criterion ->
                    CriterionItem(
                        criterion = criterion,
                        onDelete = { onDeleteCriterion(criterion) }
                    )
                }
            }
        }
    }

    // Диалог добавления нового критерия
    if (showAddDialog) {
        AddCriterionDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newCriterion ->
                onAddCriterion(newCriterion)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun CriterionItem(
    criterion: SimpleCriterion,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = criterion.name,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = onDelete
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить критерий")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = criterion.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Макс. оценка: ${criterion.maxScore}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AddCriterionDialog(
    onDismiss: () -> Unit,
    onConfirm: (SimpleCriterion) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var maxScore by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить новый критерий") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = maxScore,
                    onValueChange = { maxScore = it },
                    label = { Text("Максимальная оценка") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val score = maxScore.toDoubleOrNull() ?: 0.0
                    onConfirm(SimpleCriterion(name, description, score))
                },
                enabled = name.isNotBlank() && maxScore.toDoubleOrNull() != null
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}