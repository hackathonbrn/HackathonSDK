package ru.nofeature.hackathon.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun selectItem(
    items: List<String>,
    selectItem : (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(items.firstOrNull() ?: "") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Выпадающий список
        Box {
            Button(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedItem.ifEmpty { "Выберите элемент" })
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = item
                            selectItem(item)
                            expanded = false
                        },
                        text = {
                            Text(item)
                        })
                }
            }
        }
    }
}

@Composable
fun selectItemWithAdd(
    items: List<String>,
    selectItem: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(items.firstOrNull() ?: "") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Выпадающий список
        Box {
            Button(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedItem.ifEmpty { "Выберите элемент" })
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            selectItem(item)
                            selectedItem = item
                            expanded = false
                        },
                        text = {
                            Text(item)
                        })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Виджет для добавления новых элементов
        StringItemCreator(
            onItemCreated = { newItem ->
                selectedItem = newItem
            },
            buttonText = "Добавить новый элемент",
            dialogTitle = "Новый элемент списка",
            dialogHint = "Введите текст нового элемента"
        )
    }
}

@Composable
fun StringItemCreator(
    onItemCreated: (String) -> Unit,
    buttonText: String = "Добавить элемент",
    dialogTitle: String = "Создать новый элемент",
    dialogHint: String = "Введите текст элемента"
) {
    var showDialog by remember { mutableStateOf(false) }
    var newItemText by remember { mutableStateOf("") }

    // Кнопка для открытия диалога
    Button(
        onClick = { showDialog = true },
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Add")
        Spacer(Modifier.width(8.dp))
        Text(buttonText)
    }

    // Диалоговое окно для ввода нового элемента
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = dialogTitle,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = newItemText,
                        onValueChange = { newItemText = it },
                        label = { Text(dialogHint) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { showDialog = false }
                        ) {
                            Text("Отмена")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (newItemText.isNotBlank()) {
                                    onItemCreated(newItemText)
                                    newItemText = ""
                                    showDialog = false
                                }
                            },
                            enabled = newItemText.isNotBlank()
                        ) {
                            Text("Создать")
                        }
                    }
                }
            }
        }
    }
}