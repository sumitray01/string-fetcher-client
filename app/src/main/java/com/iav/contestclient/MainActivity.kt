package com.iav.contestclient

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.iav.contestclient.data.model.RandomStringEntity
import com.iav.contestclient.ui.state.UiState
import com.iav.contestclient.ui.theme.StringFetcherClientTheme
import com.iav.contestclient.viewmodel.RandomTextViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StringFetcherClientTheme {
                RandomTextScreen()
            }
        }
    }
}

@Composable
fun RandomTextScreen(viewModel: RandomTextViewModel = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val uiState by viewModel.uiState.collectAsState()
    val randomStrings by viewModel.randomStrings.collectAsState()
    var lengthInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Show a here snackbar when an error occurs
    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            scaffoldState.snackbarHostState.showSnackbar((uiState as UiState.Error).message)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Random String Generator") },
                actions = {
                    IconButton(onClick = { viewModel.deleteAllRandomStrings() }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete All")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = lengthInput,
                onValueChange = { lengthInput = it },
                label = { Text("Enter desired length") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val length = lengthInput.toIntOrNull()
                    if (length != null && length > 0) {
                        viewModel.generateRandomString(length)
                    } else {
                        Toast.makeText(context, "Please enter a valid positive number", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UiState.Loading
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Generate")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(randomStrings) { item ->
                    RandomStringCard(item = item, onDelete = { viewModel.deleteRandomString(it) })
                }
            }
        }
    }
}

@Composable
fun RandomStringCard(item: RandomStringEntity, onDelete: (RandomStringEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("String: ${item.value}", style = MaterialTheme.typography.subtitle1)
                Text("Length: ${item.length}", style = MaterialTheme.typography.body2)
                Text("Created: ${item.created}", style = MaterialTheme.typography.caption)
            }
            IconButton(onClick = { onDelete(item) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
