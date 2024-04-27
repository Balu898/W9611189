package uk.ac.tees.mad.w9611189.ui.profile

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9611189.ui.util.CircularProgressBar
import uk.ac.tees.mad.w9611189.ui.util.Helper
import uk.ac.tees.mad.w9611189.ui.util.Routes
import uk.ac.tees.mad.w9611189.ui.utildata.CurrencyList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun ProfileScreen(navController: NavHostController? = null) {


    val context = LocalContext.current

    var userName by remember {
        mutableStateOf("")
    }

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


    var currencyDropdownExpanded by remember { mutableStateOf(false) }

    var selectedCurrency by remember {
        mutableStateOf(CurrencyList.getCurrencyList()[0].name + CurrencyList.getCurrencyList()[0].symbol)
    }

    var selectedCurrencySymbol by remember {
        mutableStateOf("£")
    }


    LaunchedEffect(Unit) {

        currentUser?.uid?.let {
            firebaseFireStore.collection("users").document(it).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    user = task.result.toObject(User::class.java)
                }
            }
        }
    }

    LaunchedEffect(user) {
        userName = user?.name ?: ""
        user?.currency?.let {
            selectedCurrencySymbol = it
            CurrencyList.getCurrencyBySymbol(it).let { curr ->
                selectedCurrency = curr?.name + curr?.symbol
            }
        }
    }





    CircularProgressBar(showLoader) {

        Column {

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {

                TextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    value = userName, onValueChange = { value ->
                        userName = value
                    },
                    placeholder = {
                        Text(text = "User Name")
                    })




                ExposedDropdownMenuBox(
                    expanded = currencyDropdownExpanded,
                    onExpandedChange = {
                        currencyDropdownExpanded = !currencyDropdownExpanded
                    }
                ) {
                    TextField(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        value = selectedCurrency, onValueChange = { value ->
                            selectedCurrency = value
                        },
                        trailingIcon = {
                            Icon(Icons.Filled.Add, modifier = Modifier.clickable {
                                currencyDropdownExpanded = true
                            }, contentDescription = "Choose")
                        },
                        placeholder = {
                            Text(text = "Preferred Currency")
                        })

                    ExposedDropdownMenu(
                        expanded = currencyDropdownExpanded,
                        onDismissRequest = {
                            currencyDropdownExpanded = false
                        }
                    ) {
                        CurrencyList.getCurrencyList().forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.name + item.symbol) },
                                onClick = {
                                    selectedCurrency = item.name + item.symbol
                                    selectedCurrencySymbol = item.symbol
                                    currencyDropdownExpanded = false
                                }
                            )
                        }
                    }
                }




                Button(
                    enabled = userName.isNotEmpty() && selectedCurrency.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    onClick = {
                        showLoader = true

                        if (user == null) {
                            val user1 = User(
                                name = userName,
                                currency = selectedCurrencySymbol,
                                expenseCategories = listOf(
                                    "Rent",
                                    "Utilities",
                                    "Groceries",
                                    "Transportation",
                                    "Dining Out",
                                    "Entertainment",
                                    "Health",
                                    "Personal Care",
                                    "Clothing",
                                    "Education",
                                    "Debt Payments",
                                    "Home Maintenance",
                                    "Gifts/Donations",
                                    "Travel",
                                    "Subscriptions/Memberships"
                                ),
                                incomeCategories = listOf(
                                    "Salary/Wages",
                                    "Freelance/Contract Work",
                                    "Investment Income",
                                    "Rental Income",
                                    "Business Income",
                                    "Bonuses/Commissions",
                                    "Grants/Scholarships",
                                    "Alimony/Child Support",
                                    "Social Security Benefits",
                                    "Pension/Retirement Income",
                                    "Reimbursements",
                                    "Royalties",
                                    "Tips/Gratuities",
                                    "Gambling Winnings",
                                    "Sales of Assets"
                                )
                            )

                            firebaseFireStore.collection("users").document(currentUser?.uid!!)
                                .set(user1).addOnCompleteListener { addTask ->
                                    showLoader = false
                                    if (addTask.isSuccessful) {
                                        Helper.showToast(context, "Profile got updated")
                                    } else {
                                        Helper.showToast(context, "Failed to update")
                                    }
                                }
                        } else {
                            user?.let {
                                it.name = userName
                                it.currency = selectedCurrencySymbol
                            }
                            firebaseFireStore.collection("users").document(currentUser?.uid!!)
                                .set(user!!).addOnCompleteListener { addTask ->
                                    showLoader = false
                                    if (addTask.isSuccessful) {
                                        Helper.showToast(context, "Profile got updated")
                                    } else {
                                        Helper.showToast(context, "Failed to update")
                                    }
                                }
                        }

                    }) {
                    Text(text = "Update Profile")
                }




                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    onClick = {

                        if (user != null) {
                            navController?.navigate(Routes.NewCategoryScreen.route)
                        } else {
                            Helper.showToast(
                                context,
                                "Add Profile Info before adding custom category"
                            )
                        }

                    }) {
                    Text(text = "Add Custom Category")
                }


                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    onClick = {
                        firebaseAuth.signOut()
                        navController?.navigate("Login")
                    }) {
                    Text(text = "Logout")
                }
            }
        }
    }
}



