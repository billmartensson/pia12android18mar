package se.magictechnology.pia12android18mar

import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationViewmodel(application : Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val _currentlocation = MutableStateFlow<Location?>(null)
    val currentlocation : StateFlow<Location?> get() = _currentlocation

    fun getpos() {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {



            return
        }
        /*
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.i("pia12debug", "POS: " + location.latitude.toString() + " " + location.longitude.toString())
        }
         */


        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { location ->
            //Log.i("pia12debug", "CURRENT POS: " + location.latitude.toString() + " " + location.longitude.toString())

            _currentlocation.value = location
        }


    }

}