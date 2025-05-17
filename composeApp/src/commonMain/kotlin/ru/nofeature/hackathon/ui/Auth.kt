package ru.nofeature.hackathon.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.nofeature.hackathon.net.Ktor
import ru.nofeature.hackathon.team.api.Team
import ru.nofeature.hackathon.team.impl.SimpleTeam
import ru.nofeature.hackathon.users.Roles

data class RegistrationData(
    val name: String = "",
    val command: String = "",
    val project: String = "",
    val role: String = "",
)

@Composable
fun RegistrationForm(
    modifier: Modifier = Modifier,
    onRegisterClick: (RegistrationData) -> Unit
) {
    var teams by remember { mutableStateOf(listOf<Team>()) }

    val teamsCommand by remember(teams) {
        mutableStateOf(teams.map { it.command }.toSet().toList() )
    }
    val teamsProject by remember(teams) {
        mutableStateOf(teams.map { it.project }.toSet().toList())
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        launch {
            try {
                teams = Ktor.loadTeams().ifEmpty { listOf(SimpleTeam("", "", "", "")) }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
    var command by remember { mutableStateOf("") }
    var project by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        var selectedRole by remember {
            mutableStateOf(Roles.DEVELOPER.title)
        }
        selectItem(listOf(Roles.DEVELOPER.title, Roles.EXPERT.title, Roles.JUDGE.title)) {
            selectedRole = it
        }

        if (selectedRole == Roles.DEVELOPER.title) {
            selectItemWithAdd(teamsCommand) { si ->
                command = si
            }
            selectItemWithAdd(teamsProject) { si ->
                project = si
            }
        }

        Button(
            onClick = {
                if (name.isBlank()) {
                    errorMessage = "Заполните все поля"
                } else {
                    errorMessage = null
                    onRegisterClick(
                        RegistrationData(
                            command = command,
                            project = project,
                            name = name,
                            role = selectedRole
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Готово")
        }
    }
}

@Composable
fun ErrorWidget(
    message: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFFFEBEE),
    textColor: Color = Color(0xFFD32F2F),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = message,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
