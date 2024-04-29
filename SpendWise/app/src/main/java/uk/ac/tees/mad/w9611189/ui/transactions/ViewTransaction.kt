package uk.ac.tees.mad.w9611189.ui.transactions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9611189.ui.util.CircularProgressBar
import uk.ac.tees.mad.w9611189.ui.util.Helper

@Composable
fun ViewTransaction(
    navController: NavController,
    transactionId: String,
    userId: String,
    currency: String
) {


    val context = LocalContext.current

    var showLoader by remember {
        mutableStateOf(false)
    }

    val firebaseFireStore by remember {
        mutableStateOf(Firebase.firestore)
    }

    var transaction by remember {
        mutableStateOf<Transaction?>(null)
    }

    LaunchedEffect(Unit) {
        showLoader = true
        firebaseFireStore.collection("transaction").document(userId)
            .collection("trans").document(transactionId).get().addOnCompleteListener { task ->

                showLoader = false
                if (task.isSuccessful && task.result.exists()) {
                    transaction = task.result.toObject(Transaction::class.java)
                } else {
                    Helper.showToast(context, "Invalid Entry")
                }
            }
    }

    CircularProgressBar(shouldShow = showLoader) {


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (transaction?.imageUrl != null) {
                Image(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(250.dp)
                        .width(250.dp),
                    contentScale = ContentScale.Fit,
                    painter = rememberAsyncImagePainter(model = transaction?.imageUrl),
                    contentDescription = "Image"
                )
            }



            Text(
                text = "Amount : $currency ${transaction?.amount}",
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 18.sp
            )
            Text(
                text = "Category : ${transaction?.category} " +
                        if (transaction?.isExpense == true) "Expense" else "Income",
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 18.sp
            )
            Text(
                text = "Date : ${Helper.getStringDate(transaction?.timestamp ?: 0)}",
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 18.sp
            )
            Text(
                text = "Location : ${
                    Helper.getMarkerAddressDetails(
                        transaction?.latitude ?: 0.0, transaction?.longitude ?: 0.0,
                        context
                    )
                }",
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 18.sp
            )

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                onClick = {
                    showLoader = true
                    firebaseFireStore.collection("transaction").document(userId)
                        .collection("trans").document(transactionId).delete()
                        .addOnCompleteListener { task ->
                            showLoader = false
                            if (task.isSuccessful) {
                                navController.popBackStack()
                                Helper.showToast(context, "Transaction Deleted")
                            } else {
                                Helper.showToast(context, "Failed to Delete")
                            }
                        }
                }) {
                Text(text = "Delete")
            }
        }
    }
}