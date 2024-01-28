package com.example.lucaregistra

import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp  // Add this import for 'sp'
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lucaregistra.ui.theme.LucaRegistraTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LucaRegistraTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    // Set up the navigation host
                    NavHost(navController = navController, startDestination = "main") {
                        // Main screen
                        composable("main") { Greeting("Android", navController) }
                        // Second screen
                        composable("second") { SecondPage(navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, navController: NavController, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Hello $name!",
            modifier = Modifier
                .padding(24.dp)
                .background(Color.Cyan)
        )

        // Add a small box labeled 'ADD' in the right corner
        Box(
            modifier = Modifier
                .padding(16.dp)
                //.align(Alignment.TopEnd)
                .background(Color.Gray)
                .clickable { navController.navigate("second") } // Navigate to the second page
        ) {
            Text(
                text = "ADD",
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
        }
    }
}

@Composable
fun SecondPage(navController: NavController) {
    // Reference to Firebase Realtime Database
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("userInput")

    // MutableState for each input
    var numberInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }
    var colorSelection by remember { mutableStateOf("Entrada") }

    // Options for category selection
    val categories = listOf("Gallinas", "Plantas", "Administracion", "Cafe", "Perros y Gatos")

    // Set the background color of the Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
    ) {
        // Input box for entering a number
        InputField("Ingresa el valor:", KeyboardType.Number, numberInput) { userInput ->
            numberInput = userInput
        }

        // Radio buttons for category selection
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Select Category:", modifier = Modifier.padding(bottom = 4.dp), color = Color.Black, fontWeight = FontWeight.Bold)

            categories.forEach { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = categoryInput == category,
                        onClick = { categoryInput = category }
                    )
                    Text(text = category, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Radio buttons for color selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // RadioButton for "Entrada"
            RadioButton(
                selected = colorSelection == "Entrada",
                onClick = { colorSelection = "Entrada" }
            )
            Text(text = "Entrada", color = Color.Black, fontWeight = FontWeight.Bold)

            // RadioButton for "Salida"
            RadioButton(
                selected = colorSelection == "Salida",
                onClick = { colorSelection = "Salida" }
            )
            Text(text = "Salida", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        // Button to save inputs to Firebase
        Button(onClick = {
            val userInputMap = mapOf(
                "number" to numberInput,
                "category" to categoryInput,
                "color" to colorSelection // Include color in the map
            )

            // Save the JSON structure to Firebase Realtime Database
            myRef.setValue(userInputMap)
        }) {
            Text("Save to Firebase")
        }

        // Button to navigate back to the main screen
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}

@Composable
fun InputField(label: String, keyboardType: KeyboardType, inputValue: String, onValueChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Text displaying the purpose of the input
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Color.Black, // Set the text color to black
            fontWeight = FontWeight.Bold // Set the text to bold
        )

        // Box with TextField for user input
        BasicTextField(
            value = inputValue,
            onValueChange = {
                onValueChanged.invoke(it)
            },
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                color = Color.Black, // Set the text color to black
                fontWeight = FontWeight.Bold // Set the text to bold
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LucaRegistraTheme {
        Greeting("William", rememberNavController())
    }
}
