package uk.ac.tees.mad.w9611189.ui.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

object Helper {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun getMarkerAddressDetails(lat: Double, long: Double, context: Context): String? {


        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses = geocoder.getFromLocation(lat, long, 1)

        return if (!addresses.isNullOrEmpty()) {
            addresses[0].featureName + ", " + addresses[0].locality + ", " + addresses[0].adminArea
        } else {
            null
        }
    }

    fun getStringDate(timeStamp:Long): String {
        val sdf = SimpleDateFormat("dd/MM/yy")
        val netDate = Date(timeStamp)
        return sdf.format(netDate)
    }


}