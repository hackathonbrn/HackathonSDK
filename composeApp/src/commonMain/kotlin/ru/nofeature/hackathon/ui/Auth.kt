package ru.nofeature.hackathon.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ru.nofeature.hackathon.net.Ktor
import ru.nofeature.hackathon.team.api.Team
import ru.nofeature.hackathon.team.impl.SimpleTeam
import ru.nofeature.hackathon.users.Roles

@Composable
fun RegistrationForm(
    modifier: Modifier = Modifier,
    onRegisterClick: (RegistrationData) -> Unit
) {
    var teams by remember { mutableStateOf(listOf<Team>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Извлечение уникальных значений команд и проектов
    val teamsCommand by remember(teams) {
        mutableStateOf(teams.map { it.command }.toSet().toList())
    }
    val teamsProject by remember(teams) {
        mutableStateOf(teams.map { it.project }.toSet().toList())
    }

    // Состояния полей формы
    var name by remember { mutableStateOf("") }
    var command by remember { mutableStateOf("") }
    var project by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(Roles.DEVELOPER.title) }

    // Загрузка данных
    LaunchedEffect(Unit) {
        try {
            teams = Ktor.loadTeams().ifEmpty { listOf(SimpleTeam("", "", "", "")) }
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.message ?: "Ошибка загрузки данных"
            isLoading = false
        }
    }

    // Анимация появления
    AnimatedVisibility(
        visible = !isLoading,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Заголовок формы
            Text(
                text = "Регистрация",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Сообщение об ошибке
            errorMessage?.let {
                AlertMessage(
                    message = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Поле имени
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ваше имя") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                }
            )

            // Выбор роли
            Text(
                text = "Выберите роль",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            RoleSelection(
                selectedRole = selectedRole,
                onRoleSelected = { selectedRole = it },
                modifier = Modifier.fillMaxWidth()
            )

            // Дополнительные поля для разработчиков
            AnimatedVisibility(
                visible = selectedRole == Roles.DEVELOPER.title,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Выбор команды
                    DropdownSelectorWithAdd(
                        items = teamsCommand,
                        selectedItem = command,
                        onItemSelected = { command = it },
                        label = "Команда",
                        placeholder = "Выберите или введите название команды",
                        leadingIcon = Icons.Default.Person,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Выбор проекта
                    DropdownSelectorWithAdd(
                        items = teamsProject,
                        selectedItem = project,
                        onItemSelected = { project = it },
                        label = "Проект",
                        placeholder = "Выберите или введите название проекта",
                        leadingIcon = Icons.Default.Done,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Кнопка регистрации
            Button(
                onClick = {
                    if (name.isBlank()) {
                        errorMessage = "Пожалуйста, введите ваше имя"
                    } else {
                        errorMessage = null
                        onRegisterClick(
                            RegistrationData(
                                name = name,
                                command = command,
                                project = project,
                                role = selectedRole
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Text(
                    text = "Зарегистрироваться",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // Индикатор загрузки
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
private fun RoleSelection(
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val roles =
        listOf(Roles.DEVELOPER.title, Roles.EXPERT.title, Roles.JUDGE.title, Roles.ORGANIZER.title)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        roles.forEach { role ->
            val isSelected = role == selectedRole

            val borderColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = borderColor,
                    )
                    .clickable { onRoleSelected(role) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = role,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}

@Composable
private fun DropdownSelectorWithAdd(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var customText by remember { mutableStateOf("") }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedItem.ifEmpty { customText },
            onValueChange = {
                customText = it
                if (it.isNotEmpty()) {
                    onItemSelected(it)
                }
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Показать варианты",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        customText = ""
                        expanded = false
                    },
                    text = {
                        Text(text = item)
                    })
            }
        }
    }
}

@Composable
private fun AlertMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color.Red.copy(alpha = 0.1f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Ошибка",
            tint = Color.Red
        )
        Text(
            text = message,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )
    }
}