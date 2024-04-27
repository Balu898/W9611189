package uk.ac.tees.mad.w9611189.ui.transactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9611189.ui.profile.User
import uk.ac.tees.mad.w9611189.ui.util.CircularProgressBar
import uk.ac.tees.mad.w9611189.ui.utildata.CurrencyList
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NewTransaction() {

    var amount by remember {
        mutableStateOf("")
    }


    var selectedCategory by remember {
        mutableStateOf("")
    }

    var selectedDisplayDate by remember {
        mutableStateOf("")
    }

    var selectedDateTimeStamp by remember {
        mutableLongStateOf(0L)
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
    var dateDialog by remember { mutableStateOf(false) }

    val radioOptions = listOf("Income", "Expense")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(selectedDateTimeStamp) {
        val sdf = SimpleDateFormat("dd/MM/yy")
        val netDate = Date(selectedDateTimeStamp)
        selectedDisplayDate = sdf.format(netDate)
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

    LaunchedEffect(selectedOption) {
        selectedCategory = if (selectedOption == "Income") {
            user?.incomeCategories?.get(0) ?: ""
        } else {
            user?.expenseCategories?.get(0) ?: ""
        }
    }

    CircularProgressBar(shouldShow = showLoader) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {


            Row {
                radioOptions.forEach { text ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) }
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            modifier = Modifier.padding(start = 8.dp),
                            onClick = {
                                onOptionSelected(text)
                            }
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }



            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = amount,
                prefix = {
                    Text(text = "$ ")
                },
                placeholder = {
                    Text(text = "Enter transaction Value")
                },
                label = {
                    Text(text = "Amount")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { amount = it }
            )


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
                    value = selectedCategory, onValueChange = { value ->
                        selectedCategory = value
                    },
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, modifier = Modifier.clickable {
                            currencyDropdownExpanded = true
                        }, contentDescription = "Choose")
                    },
                    placeholder = {
                        Text(text = "Select Category")
                    })

                ExposedDropdownMenu(
                    expanded = currencyDropdownExpanded,
                    onDismissRequest = {
                        currencyDropdownExpanded = false
                    }
                ) {
                    if (selectedOption == "Income") {
                        user?.incomeCategories?.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedCategory = item
                                    currencyDropdownExpanded = false
                                }
                            )
                        }
                    } else {
                        user?.expenseCategories?.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedCategory = item
                                    currencyDropdownExpanded = false
                                }
                            )
                        }
                    }

                }

                TextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    readOnly = true,
                    value = selectedDisplayDate,
                    onValueChange = { _ -> },
                    trailingIcon = {
                        Icon(Icons.Filled.DateRange, modifier = Modifier.clickable {
                            dateDialog = true
                        }, contentDescription = "Choose")
                    },
                    placeholder = {
                        Text(text = "Choose Date")
                    })

                if(dateDialog) {
                    MyDatePickerDialog(onDateSelected = { stc ->
                        selectedDateTimeStamp = stc ?: 0L
                        dateDialog = false
                    }) {
                        dateDialog = false
                    }
                }
            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(onDateSelected: (Long?) -> Unit,
                       onDismiss: () -> Unit) {

    val datePickerState = rememberDatePickerState()


    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }


}

