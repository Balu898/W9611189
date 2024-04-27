package uk.ac.tees.mad.w9611189.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9611189.R
import uk.ac.tees.mad.w9611189.ui.profile.User
import uk.ac.tees.mad.w9611189.ui.util.CircularProgressBar
import uk.ac.tees.mad.w9611189.ui.util.Helper
import uk.ac.tees.mad.w9611189.ui.util.Routes

@Composable
@Preview
fun HomeScreen(navController: NavController? = null) {

    val context = LocalContext.current

    val firebaseAuth by remember {
        mutableStateOf(Firebase.auth)
    }


    val currentUser by remember {
        mutableStateOf(firebaseAuth.currentUser)
    }

    val firebaseFireStore by remember {
        mutableStateOf(Firebase.firestore)
    }

    var showLoader by remember {
        mutableStateOf(false)
    }

    var user by remember {
        mutableStateOf<User?>(null)
    }

    LaunchedEffect(Unit) {
        if(currentUser?.uid == null) {
            navController?.navigate(Routes.LoginScreen.route) {
                popUpTo(Routes.HomeScreen.route) {
                    inclusive = true
                }
            }
        } else {
            currentUser?.uid?.let {
                firebaseFireStore.collection("users").document(it).get().addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result.exists()) {
                        user = task.result.toObject(User::class.java)
                    } else {
                        Helper.showToast(context, "Add Profile Details")
                        navController?.navigate(Routes.ProfileScreen.route)
                    }
                }
            }
        }

    }


    CircularProgressBar(showLoader) {

        Box {
            FloatingActionButton(
                containerColor = colorResource(id = R.color.purple_200),
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(32.dp),
                onClick = {
                    navController?.navigate(Routes.NewTransactionScreen.route)
                }) {
                Icon(
                    Icons.Filled.Add,
                    tint = colorResource(id = R.color.white),
                    contentDescription = " Add"
                )
            }
        }
    }
}