package uk.ac.tees.mad.w9611189.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9611189.R
import uk.ac.tees.mad.w9611189.ui.profile.User
import uk.ac.tees.mad.w9611189.ui.transactions.MyDatePickerDialog
import uk.ac.tees.mad.w9611189.ui.transactions.Transaction
import uk.ac.tees.mad.w9611189.ui.util.CircularProgressBar
import uk.ac.tees.mad.w9611189.ui.util.Helper
import uk.ac.tees.mad.w9611189.ui.util.Routes
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
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

    var allTrans by remember {
        mutableStateOf(listOf<Transaction>())
    }

    var barChartDataList by remember {
        mutableStateOf(listOf<BarChartData.Bar>())
    }

    var user by remember {
        mutableStateOf<User?>(null)
    }

    var currencyDropdownExpanded by remember { mutableStateOf(false) }

    var selectedCategory by remember {
        mutableStateOf("Income")
    }

    val list = listOf("Income", "Expense")

    var fromDisplayDate by remember {
        mutableStateOf("")
    }

    var toDisplayDate by remember {
        mutableStateOf("")
    }

    var fromDateDialog by remember { mutableStateOf(false) }
    var toDateDialog by remember { mutableStateOf(false) }


    var fromDateTimeStamp by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    var toDateTimeStamp by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    var thisMonthBalance by remember {
        mutableIntStateOf(0)
    }

    var thisMonthIncome by remember {
        mutableIntStateOf(0)
    }

    var thisMonthExpense by remember {
        mutableIntStateOf(0)
    }

    var totalBalance by remember {
        mutableIntStateOf(0)
    }


    LaunchedEffect(fromDateTimeStamp) {
        fromDisplayDate = Helper.getStringDate(fromDateTimeStamp)
    }

    LaunchedEffect(toDateTimeStamp) {
        toDisplayDate = Helper.getStringDate(toDateTimeStamp)
    }



    LaunchedEffect(allTrans, fromDateTimeStamp, toDateTimeStamp, selectedCategory) {

        val newT = if (selectedCategory == "Income") {
            allTrans.filter { !it.isExpense }
        } else {
            allTrans.filter { it.isExpense }
        }

        val newT1 = newT.filter {
            ((it.timestamp ?: 0L) >= fromDateTimeStamp) && ((it.timestamp ?: 0L) <= toDateTimeStamp)
        }


        val catMap = hashMapOf<String, Int>()
        for (transaction in newT1) {
            val category = transaction.category!!
            val amount = transaction.amount?.toInt() ?: 0
            if (catMap.containsKey(category)) {
                catMap[category] = catMap[category]!! + amount
            } else {
                catMap[category] = amount
            }
        }

        val barList = mutableListOf<BarChartData.Bar>()

        var i = 0
        catMap.forEach {
            if (i <= 5) {
                barList.add(BarChartData.Bar(it.value.toFloat(), Helper.getRandomColor(i), it.key))
                i++
            }
        }
        barChartDataList = barList
    }


    LaunchedEffect(allTrans) {
        allTrans.map {
            totalBalance += (it.amount)?.toInt() ?: 0
        }

        val now = LocalDate.now()
        val startOfMonth = now.withDayOfMonth(1)
        val endOfMonth = now.withDayOfMonth(now.lengthOfMonth())


        val startOfMonthEpochMilli =
            startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        fromDateTimeStamp = startOfMonthEpochMilli
        val endOfMonthEpochMilli =
            endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        toDateTimeStamp = endOfMonthEpochMilli


        allTrans.forEach { trans ->

            if ((trans.timestamp ?: 0L) >= startOfMonthEpochMilli && (trans.timestamp
                    ?: 0L) <= endOfMonthEpochMilli
            ) {
                if (trans.isExpense) {
                    thisMonthExpense += trans.amount?.toInt() ?: 0
                } else {
                    thisMonthIncome += trans.amount?.toInt() ?: 0
                }
            }
        }
        thisMonthBalance = thisMonthIncome - thisMonthExpense
    }

    LaunchedEffect(Unit) {
        if (currentUser?.uid == null) {
            navController?.navigate(Routes.LoginScreen.route) {
                popUpTo(Routes.HomeScreen.route) {
                    inclusive = true
                }
            }
        } else {
            showLoader = true
            currentUser?.uid?.let {
                firebaseFireStore.collection("users").document(it).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result.exists()) {
                            user = task.result.toObject(User::class.java)

                            firebaseFireStore.collection("transaction").document(currentUser!!.uid)
                                .collection("trans").get().addOnCompleteListener { task1 ->
                                    if (task1.isSuccessful) {
                                        showLoader = false
                                        val localTrans = mutableListOf<Transaction>()
                                        for (document in task1.result.documents) {
                                            if (document.exists()) {
                                                val trans =
                                                    document.toObject(Transaction::class.java)
                                                if (trans != null) {
                                                    localTrans.add(trans)
                                                }
                                            }
                                        }
                                        allTrans = localTrans
                                    } else {
                                        showLoader = false
                                    }
                                }


                        } else {
                            showLoader = false
                            Helper.showToast(context, "Add Profile Details")
                            navController?.navigate(Routes.ProfileScreen.route)
                        }
                    }
            }
        }

    }


    CircularProgressBar(showLoader) {

        Box {
            Column {
                BalanceCard(
                    thisMonthBalance,
                    thisMonthIncome,
                    thisMonthExpense,
                    totalBalance,
                    user?.currency ?: "£"
                )


                Column {


                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .padding(horizontal = 2.dp),
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
                                Text(
                                    text = "Type"
                                )
                            })

                        ExposedDropdownMenu(
                            expanded = currencyDropdownExpanded,
                            onDismissRequest = {
                                currencyDropdownExpanded = false
                            }
                        ) {
                            list.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = item
                                        )
                                    },
                                    onClick = {
                                        selectedCategory = item
                                        currencyDropdownExpanded = false
                                    }
                                )
                            }


                        }

                    }

                    Row(modifier = Modifier.padding(top = 12.dp)) {


                        TextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .fillMaxWidth(),
                            readOnly = true,
                            value = fromDisplayDate,
                            onValueChange = { _ -> },
                            trailingIcon = {
                                Icon(Icons.Filled.DateRange, modifier = Modifier.clickable {
                                    fromDateDialog = true
                                }, contentDescription = "Choose")
                            },
                            placeholder = {
                                Text(text = "From")
                            })

                        TextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 2.dp)
                                .fillMaxWidth(),
                            readOnly = true,
                            value = toDisplayDate,
                            onValueChange = { _ -> },
                            trailingIcon = {
                                Icon(Icons.Filled.DateRange, modifier = Modifier.clickable {
                                    toDateDialog = true
                                }, contentDescription = "Choose")
                            },
                            placeholder = {
                                Text(text = "To")
                            })

                        if (fromDateDialog || toDateDialog) {
                            MyDatePickerDialog(onDateSelected = { stc ->
                                if (fromDateDialog) {
                                    fromDateTimeStamp = stc ?: 0L
                                    fromDateDialog = false
                                } else {
                                    toDateTimeStamp = stc ?: 0L
                                    toDateDialog = false
                                }

                            }) {
                                if (fromDateDialog) {
                                    fromDateDialog = false
                                } else {
                                    toDateDialog = false
                                }

                            }
                        }
                    }

                }

                BarChart(

                    barChartData = BarChartData(
                        bars = barChartDataList
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    animation = simpleChartAnimation(),
                    barDrawer = SimpleBarDrawer(),
                    xAxisDrawer = SimpleXAxisDrawer(),
                    yAxisDrawer = SimpleYAxisDrawer(),
                    labelDrawer = SimpleValueDrawer(labelTextColor = Color.White)
                )
            }

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

@Composable
fun BalanceCard(
    thisMonthBalance: Int,
    thisMonthIncome: Int,
    thisMonthExpense: Int,
    totalBalance: Int,
    currency: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                color = colorResource(id = R.color.black_light),
                text = "BALANCE",
                fontSize = 18.sp
            )
            Text(
                text = "$currency $thisMonthBalance",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "This Month",
                color = colorResource(id = R.color.light_blue)
            )
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                        contentDescription = "Down Arrow"
                    )
                    Text(
                        text = "$currency $thisMonthIncome ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_upward_24),
                        contentDescription = "Up Arrow"
                    )
                    Text(
                        text = "$currency $thisMonthExpense",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Balance ",
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "$currency $totalBalance",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

