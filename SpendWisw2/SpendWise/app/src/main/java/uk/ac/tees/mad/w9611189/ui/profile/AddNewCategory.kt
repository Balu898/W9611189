package uk.ac.tees.mad.w9611189.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9611189.ui.util.Helper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun AddNewCategory() {


    val context = LocalContext.current

    var newCategory by remember {
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

    var shouldShowLoader by remember {
        mutableStateOf(false)
    }

    var user by remember {
        mutableStateOf<User?>(null)
    }

    var expenseCategoryList by remember {
        mutableStateOf(emptyList<String>())
    }

    var incomeCategoryList by remember {
        mutableStateOf(emptyList<String>())
    }

    val radioOptions = listOf("Income", "Expense")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }


    LaunchedEffect(Unit) {
        currentUser?.uid?.let {
            firebaseFireStore.collection("users").document(it).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    user = task.result.toObject(User::class.java)
                    expenseCategoryList = user?.expenseCategories ?: emptyList()
                    incomeCategoryList = user?.incomeCategories ?: emptyList()
                }
            }
        }
    }

    Column(Modifier.padding(8.dp)) {

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



        LazyColumn(modifier = Modifier.weight(1f)) {

            val count = if (selectedOption == "Income") {
                incomeCategoryList.size
            } else {
                expenseCategoryList.size
            }

            val list = if (selectedOption == "Income") {
                incomeCategoryList
            } else {
                expenseCategoryList
            }

            items(count) { index ->
                CategoryItem(list[index], onDeleteClicked = { category ->

                    val newList =
                        if (selectedOption == "Income") {
                            incomeCategoryList.toMutableList()
                        } else {
                            expenseCategoryList.toMutableList()
                        }
                    newList.remove(category)


                    if (selectedOption == "Income") {
                        user?.incomeCategories = newList
                    } else {
                        user?.expenseCategories = newList
                    }

                    firebaseFireStore.collection("users").document(currentUser?.uid!!).set(
                        user!!
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (selectedOption == "Income") {
                                incomeCategoryList = newList
                            } else {
                                expenseCategoryList = newList
                            }
                            Helper.showToast(context, "Category Removed")
                        } else {
                            Helper.showToast(context, "Failed to Remove")
                        }
                    }

                })
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {

            TextField(
                placeholder = {
                    Text(text = "Enter New Category")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 16.dp),
                value = newCategory,
                onValueChange = {
                    newCategory = it
                }
            )

            Button(
                enabled = newCategory.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {


                    val newCategoryList =
                        if (selectedOption == "Income")
                            incomeCategoryList.toMutableList()
                        else expenseCategoryList.toMutableList()




                    newCategoryList.add(newCategory)

                    if (selectedOption == "Income") {
                        user?.incomeCategories = newCategoryList
                    } else {
                        user?.expenseCategories = newCategoryList
                    }

                    firebaseFireStore.collection("users").document(currentUser?.uid!!).set(
                        user!!
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            if (selectedOption == "Income") {
                                incomeCategoryList = newCategoryList
                            } else {
                                expenseCategoryList = newCategoryList
                            }

                            newCategory = ""
                            Helper.showToast(context, "Category Added")
                        } else {
                            Helper.showToast(context, "Failed to Add")
                        }
                    }
                }) {
                Text(text = "Save")
            }
        }
    }
}


@Composable
fun CategoryItem(category: String, onDeleteClicked: (Any?) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(6f),
                    fontSize = 16.sp,
                    text = category
                )

                Icon(
                    Icons.Filled.Delete,
                    tint = Color.Red,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onDeleteClicked.invoke(category)
                        },
                    contentDescription = "Add"
                )

            }
        }
    }

}