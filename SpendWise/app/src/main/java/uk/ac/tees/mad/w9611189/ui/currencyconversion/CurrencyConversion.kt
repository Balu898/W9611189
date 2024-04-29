package uk.ac.tees.mad.w9611189.ui.currencyconversion

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.google.rpc.Help
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response
import uk.ac.tees.mad.w9611189.repo.ApiClient
import uk.ac.tees.mad.w9611189.repo.ApiService
import uk.ac.tees.mad.w9611189.repo.ConversionResult
import uk.ac.tees.mad.w9611189.ui.util.CircularProgressBar
import uk.ac.tees.mad.w9611189.ui.util.Helper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun CurrencyConversion() {

    var context = LocalContext.current

    var showLoader by remember {
        mutableStateOf(false)
    }


    var fromCurrencyExpanded by remember { mutableStateOf(false) }


    var supportedCurrencies by remember {
        mutableStateOf(
            listOf(
                "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN",
                "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL",
                "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY",
                "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP",
                "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS",
                "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF",
                "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD",
                "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD", "KZT",
                "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD",
                "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN",
                "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK",
                "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR",
                "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SOS", "SRD", "SSP",
                "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD",
                "TVD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VES", "VND",
                "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW",
                "ZWL"
            )
        )
    }


    var toCurrencyExpanded by remember { mutableStateOf(false) }

    var selectedFromCurrency by remember {
        mutableStateOf("GBP")
    }


    var selectedToCurrency by remember {
        mutableStateOf("USD")
    }


    var fromCurrency by remember {
        mutableStateOf("")
    }


    var toCurrency by remember {
        mutableStateOf("0.0")
    }

    var conversionResult by remember {
        mutableStateOf<ConversionResult?>(null)
    }

    conversionResult


    CircularProgressBar(shouldShow = showLoader) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {


            ExposedDropdownMenuBox(
                expanded = fromCurrencyExpanded,
                onExpandedChange = {
                    fromCurrencyExpanded = !fromCurrencyExpanded
                }
            ) {
                TextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedFromCurrency, onValueChange = { value ->
                        selectedFromCurrency = value
                    },
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, modifier = Modifier.clickable {
                            fromCurrencyExpanded = true
                        }, contentDescription = "Choose")
                    },
                    placeholder = {
                        Text(text = "Select Category")
                    })

                ExposedDropdownMenu(
                    expanded = fromCurrencyExpanded,
                    onDismissRequest = {
                        fromCurrencyExpanded = false
                    }
                ) {

                        supportedCurrencies.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedFromCurrency = item
                                    fromCurrencyExpanded = false
                                }
                            )
                        }


                }

            }


            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = fromCurrency,
                placeholder = {
                    Text(text = "Enter From Currency Value")
                },
                label = {
                    Text(text = "From")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    if (it.isDigitsOnly()) fromCurrency = it
                }
            )


            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp),
                textAlign = TextAlign.Center,
                text = "---- Convert ----")



            ExposedDropdownMenuBox(
                expanded = toCurrencyExpanded,
                onExpandedChange = {
                    toCurrencyExpanded = !toCurrencyExpanded
                }
            ) {
                TextField(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedToCurrency, onValueChange = { value ->
                        selectedToCurrency = value
                    },
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, modifier = Modifier.clickable {
                            toCurrencyExpanded = true
                        }, contentDescription = "Choose")
                    },
                    placeholder = {
                        Text(text = "Select Category")
                    })

                ExposedDropdownMenu(
                    expanded = toCurrencyExpanded,
                    onDismissRequest = {
                        toCurrencyExpanded = false
                    }
                ) {

                    supportedCurrencies.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                if(selectedFromCurrency != item) {
                                    selectedToCurrency = item
                                    toCurrencyExpanded = false
                                } else {
                                    Helper.showToast(context, "Both cannot be same")
                                }

                            }
                        )
                    }


                }

            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = toCurrency,
                readOnly = true,
                placeholder = {
                    Text(text = "Enter To Currency Value")
                },
                label = {
                    Text(text = "To")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    if (it.isDigitsOnly()) toCurrency = it
                }
            )


            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 16.dp),
                onClick = {

                    if(
                        fromCurrency.isNotEmpty() &&
                        fromCurrency.toDouble() != 0.0 && selectedFromCurrency.isNotEmpty()) {

                        val call = ApiClient.apiService.getExchangeValue(from = selectedFromCurrency,to = selectedToCurrency)
                        call.enqueue(object : retrofit2.Callback<ConversionResult>{
                            override fun onResponse(
                                call: Call<ConversionResult>,
                                response: Response<ConversionResult>
                            ) {
                                if(response.isSuccessful){
                                    conversionResult = response.body()
                                    toCurrency = ((conversionResult?.conversion_rate?:0.0) * fromCurrency.toDouble()).toString()

                                } else {
                                    Helper.showToast(context,"Error Fetching Values")
                                }
                            }

                            override fun onFailure(call: Call<ConversionResult>, t: Throwable) {
                                Helper.showToast(context,"Error Fetching Values")
                            }
                        })
                    } else {
                        Helper.showToast(context,"Select Correct Values")
                    }

                }) {
                Text(text = "Convert")
            }

        }

    }


}