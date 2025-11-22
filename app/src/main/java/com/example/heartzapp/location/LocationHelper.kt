package com.example.heartzapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import java.util.Locale

class LocationHelper(private val context: Context) {

    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getAddress(): Pair<String, String>? {
        val location: Location = fusedClient.lastLocation.await() ?: return null

        val geocoder = Geocoder(context, Locale.getDefault())
        val result = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        val address = result?.firstOrNull() ?: return null

        val direccion = address.thoroughfare ?: ""
        val numero = address.subThoroughfare ?: ""
        val comuna = address.locality ?: ""

        val direccionCompleta = "$direccion $numero"

        return Pair(direccionCompleta, comuna)
    }
}
