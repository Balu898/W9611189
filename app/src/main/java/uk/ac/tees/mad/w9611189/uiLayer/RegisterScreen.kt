package uk.ac.tees.mad.w9611189.uiLayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.w9611189.viewModel.UserSignupViewModel

@Composable
fun RegisterScreen(
    userSignupViewModel: UserSignupViewModel = hiltViewModel(),
    navController: NavController
) {
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SimpleOutlinedTextField(
                value = emailAddress,
                onValueChange = { emailAddress = it },
                label = "Email Address"
            )

            Spacer(modifier = Modifier.height(height = 7.dp))
            SimpleOutlinedTextField(
                value = emailAddress,
                onValueChange = { emailAddress = it },
                label = "Password"
            )
            Spacer(modifier = Modifier.height(height = 21.dp))
            Button(onClick = {
//                userSignupViewModel.registerUser(email = emailAddress, password = password)
//                navController.navigate(Routes.Home.name)
            }) {
                Text(text = "Register")
            }
        }
    }
}

@Composable
fun SimpleOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    val userSignupViewModel = hiltViewModel<UserSignupViewModel>()
    RegisterScreen(userSignupViewModel = userSignupViewModel, navController = navController)
}
