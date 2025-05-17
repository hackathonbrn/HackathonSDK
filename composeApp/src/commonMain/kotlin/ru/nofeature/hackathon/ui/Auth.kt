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
import ru.nofeature.hackathon.users.Roles

data class RegistrationData(
    val name: String = "",
    val command: String = "",
    val project: String = "",
)

@Composable
fun RegistrationForm(
    modifier: Modifier = Modifier,
    onRegisterClick: (RegistrationData) -> Unit
) {
    var command by remember { mutableStateOf("") }
    var project by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
            selectItemWithAdd(
                listOf("Команда 1", "команда 2")
            ) {

            }
            selectItemWithAdd(listOf("проект 1", "проект 2")) {

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
                            name = name
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
