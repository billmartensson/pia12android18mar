package se.magictechnology.pia12android18mar

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import se.magictechnology.pia12android18mar.ui.theme.Pia12android18marTheme

class MainActivity : ComponentActivity() {

    lateinit var locvm : LocationViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locvm = LocationViewmodel(application)

        setContent {
            Pia12android18marTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LocationScreen(locvm)
                }
            }
        }

    }

    fun getpermission() {

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                    //getlastpos()
                }
                permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    //getlastpos()
                } else -> {
                // No location access granted.
            }
            }
        }

        // ...

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION))

    }


}

@Composable
fun LocationScreen(locvm : LocationViewmodel) {

    val currentpos by locvm.currentlocation.collectAsState()

    Column {
        Text(
            text = "Hello"
        )

        if(currentpos != null) {
            Text(currentpos!!.latitude.toString())
        }

        Button(onClick = {
            locvm.getpos()
        }) {
            Text("Get position")
        }




        val singapore = LatLng(1.35, 103.87)
        val singapore2 = LatLng(1.351, 103.871)

        var usermarker by remember {
            mutableStateOf<LatLng?>(null)
        }

        val singporepoints = mutableListOf<LatLng>(singapore, singapore2)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }

        var uiSettings by remember { mutableStateOf(MapUiSettings()) }
        var properties by remember {
            mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings
        ) {
            Marker(
                state = MarkerState(position = singapore),
                title = "Singapore",
                snippet = "Marker in Singapore",
                onClick = {
                    Log.i("pia12debug", "CLICK " + it.title)
                    false
                }
            )
            Marker(
                state = MarkerState(position = singapore2),
                title = "Singapore2",
                snippet = "Marker in Singapore2",
                onClick = {
                    Log.i("pia12debug", "CLICK " + it.title)
                    false
                }
            )

            Polyline(points = singporepoints)



            if(usermarker != null) {
                Marker(
                    state = MarkerState(position = usermarker!!),
                    title = "USER",
                    snippet = "User selected this",
                    draggable = true
                )
            }

            val pinConfig = PinConfig.builder()
                .setBackgroundColor(Color.GREEN)
                .build()

            AdvancedMarker(
                state = MarkerState(position = LatLng(-34.0, 151.0)),
                title = "Magenta marker in Sydney",
                pinConfig = pinConfig
            )

        }
        Switch(
            checked = uiSettings.zoomControlsEnabled,
            onCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
        )
        if(usermarker != null) {
            Text(usermarker!!.latitude.toString())
        }


        Button(onClick = {
            usermarker = cameraPositionState.position.target
        }) {
            Text("User is here")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pia12android18marTheme {
        //LocationScreen()
    }
}