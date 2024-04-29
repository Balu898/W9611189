package uk.ac.tees.mad.w9611189.ui.transactions

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.rpc.Help
import uk.ac.tees.mad.w9611189.R
import uk.ac.tees.mad.w9611189.ui.profile.User
import uk.ac.tees.mad.w9611189.ui.util.CircularProgressBar
import uk.ac.tees.mad.w9611189.ui.util.Helper
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Objects
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTransaction(navController: NavController) {

    val context = LocalContext.current

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
        mutableLongStateOf(System.currentTimeMillis())
    }

    var selectedLatLng by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var selectedAddressToDisplay by remember {
        mutableStateOf("")
    }

    var firebaseStorage by remember {
        mutableStateOf(FirebaseStorage.getInstance())
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


    var receiptImageUri by remember {
        mutableStateOf<String?>(null)
    }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "uk.ac.tees.mad.w9611189" + ".provider", file
    )


    var currencyDropdownExpanded by remember { mutableStateOf(false) }
    var dateDialog by remember { mutableStateOf(false) }
    var showMap by remember { mutableStateOf(false) }

    val radioOptions = listOf("Income", "Expense")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri1 ->
            receiptImageUri = uri1?.toString()
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            showMap = true
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            receiptImageUri = uri?.toString()
        }

    LaunchedEffect(selectedDateTimeStamp) {
        selectedDisplayDate = Helper.getStringDate(selectedDateTimeStamp)
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

    LaunchedEffect(selectedLatLng) {
        selectedAddressToDisplay = Helper.getMarkerAddressDetails(
            selectedLatLng.latitude, selectedLatLng.longitude,
            context
        ) ?: ""
    }

    CircularProgressBar(shouldShow = showLoader) {


        Box {


            if (showMap) {
                MapScreen(
                    onMapClicked = { latLng ->
                        showMap = false
                        selectedLatLng = latLng
                    }
                )
            } else {


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
                            Text(text = user?.currency ?: "£")
                        },
                        placeholder = {
                            Text(text = "Enter transaction Value")
                        },
                        label = {
                            Text(text = "Amount")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            if (it.isDigitsOnly()) amount = it
                        }
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

                    if (dateDialog) {
                        MyDatePickerDialog(onDateSelected = { stc ->
                            selectedDateTimeStamp = stc ?: 0L
                            dateDialog = false
                        }) {
                            dateDialog = false
                        }
                    }


                    TextField(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedAddressToDisplay,
                        onValueChange = { _ -> },
                        trailingIcon = {
                            Icon(Icons.Filled.LocationOn, modifier = Modifier.clickable {

                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    showMap = true
                                } else {
                                    locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                }

                            }, contentDescription = "Choose")
                        },
                        placeholder = {
                            Text(text = "Select Location")
                        })


                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .width(150.dp)
                            .height(150.dp)
                            .padding(top = 16.dp),
                        painter = if (receiptImageUri != null) rememberAsyncImagePainter(model = receiptImageUri)
                        else painterResource(id = R.drawable.baseline_add_photo_alternate_24),
                        contentDescription = "Image"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.CAMERA
                                    )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri)
                                } else {
                                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                            }) {
                            Text(text = "Camera")
                        }
                        Button(modifier = Modifier.padding(8.dp),
                            onClick = {
                                launcher.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }) {
                            Text(text = "Gallery")
                        }
                    }

                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        onClick = {

                            if (amount.isEmpty() || amount == "0") {
                                Helper.showToast(context, "Enter Valid Amount")
                            } else if (selectedCategory.isEmpty() || selectedCategory.isBlank()) {
                                Helper.showToast(context, "Select Category")
                            } else if(receiptImageUri == null) {
                                Helper.showToast(context, "Upload Image")
                            }else {
                                showLoader = true


                                val uuid = UUID.randomUUID().toString()

                                firebaseStorage.reference.child(uuid)
                                    .putFile(Uri.parse(receiptImageUri))
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            firebaseStorage.reference.child(uuid).downloadUrl.addOnCompleteListener { downTask ->
                                                if (downTask.isSuccessful) {


                                                    val trans = Transaction(
                                                        id = uuid,
                                                        isExpense = selectedOption == "Expense",
                                                        amount = amount,
                                                        category = selectedCategory,
                                                        timestamp = selectedDateTimeStamp,
                                                        latitude = selectedLatLng.latitude,
                                                        longitude = selectedLatLng.longitude,
                                                        imageUrl = downTask.result.toString()
                                                    )

                                                    firebaseFireStore.collection("transaction")
                                                        .document(currentUser?.uid!!)
                                                        .collection("trans")
                                                        .document(uuid).set(trans)
                                                        .addOnCompleteListener { task3 ->
                                                            showLoader = false
                                                            if (task3.isSuccessful) {
                                                                Helper.showToast(
                                                                    context,
                                                                    "Transaction added successfully"
                                                                )
                                                                navController.popBackStack()

                                                            } else {
                                                                firebaseStorage.reference.child(uuid)
                                                                    .delete()
                                                                Helper.showToast(
                                                                    context,
                                                                    "Transaction failed to add"
                                                                )
                                                            }
                                                        }
                                                } else {
                                                    showLoader = false
                                                    Helper.showToast(context, "Failed to Add")
                                                }
                                            }
                                        } else {
                                            showLoader = false
                                            Helper.showToast(context, "Failed to Add")
                                        }
                                    }
                            }
                        }) {
                        Text(text = "Save Transaction")
                    }
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {

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

@Composable
fun MapScreen(onMapClicked: (latLng: LatLng) -> Unit) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            onMapClick = { latLng ->
                onMapClicked.invoke(latLng)
            },
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(
                compassEnabled = true
            ),
            cameraPositionState = cameraPositionState,
        )
    }
}


fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("MMddHHmmss").format(java.util.Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}
