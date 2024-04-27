package uk.ac.tees.mad.w9611189.ui.transactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9611189.R
import uk.ac.tees.mad.w9611189.ui.profile.User
import uk.ac.tees.mad.w9611189.ui.util.Helper

@Composable
@Preview
fun AllTransactions() {


    val firebaseAuth by remember {
        mutableStateOf(Firebase.auth)
    }

    val currentUser by remember {
        mutableStateOf(firebaseAuth.currentUser)
    }

    val firebaseFireStore by remember {
        mutableStateOf(Firebase.firestore)
    }

    val allTrans = remember {
        mutableStateListOf<Transaction>()
    }

    var user by remember {
        mutableStateOf<User?>(null)
    }


    LaunchedEffect(Unit) {

        currentUser?.uid?.let {
            firebaseFireStore.collection("users").document(it).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    user = task.result.toObject(User::class.java)
                    firebaseFireStore.collection("transaction").document(currentUser!!.uid)
                        .collection("trans").get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                for (document in task.result.documents) {
                                    if (document.exists()) {
                                        val trans = document.toObject(Transaction::class.java)
                                        if (trans != null) {
                                            allTrans.add(trans)
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }


    }


    Column {
        LazyColumn {
            items(allTrans.size) { index ->
                Item(allTrans[index],user!!.currency!!)
            }

        }
    }
}

@Composable
fun Item(transaction: Transaction, currency: String) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Icon(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                tint = if (transaction.isExpense) Color.Red else colorResource(id = R.color.green),
                painter = painterResource(id = R.drawable.baseline_circle_24),
                contentDescription = "Circle"
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    modifier = Modifier,
                    text = transaction.category ?: "",
                    fontSize = 18.sp
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = Helper.getStringDate(transaction.timestamp ?: 0),
                    fontSize = 12.sp
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                text = if (transaction.isExpense) "- $currency${transaction.amount}" else
                    "+ $currency${transaction.amount}",
                color = if(transaction.isExpense) Color.Red else colorResource(id = R.color.green),
                textAlign = TextAlign.End
            )
        }
    }
}