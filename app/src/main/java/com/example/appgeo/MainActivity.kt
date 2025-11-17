package com.example.appgeo // <-- Añade esta línea al inicio de todo

import android.Manifest // Importante
import android.content.pm.PackageManager // Importante
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts // Importante
import androidx.core.content.ContextCompat // Importante

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    // --- INICIO DE CÓDIGO DE PERMISOS ---

    // 1. Registra el "contrato" para pedir permiso.
    //    Esto se prepara para cuando el usuario responda al diálogo de permiso.
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Si el usuario ACEPTA el permiso
                enableMyLocation()
            } else {
                // Si el usuario RECHAZA el permiso.
                // Aquí podrías mostrar un mensaje explicando por qué lo necesitas.
                // Por ahora, solo lo dejamos así.
            }
        }

    // --- FIN DE CÓDIGO DE PERMISOS ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    /**
     * Se llama cuando el mapa está listo.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Ya no añadimos el marcador fijo de la UNAH-VS
        // val unahVs = LatLng(15.556213, -88.026131)
        // mMap.addMarker(MarkerOptions().position(unahVs).title("Marcador en UNAH-VS"))
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(unahVs, 15f))

        // 2. En lugar del marcador fijo, intentamos habilitar la ubicación del usuario.
        enableMyLocation()
    }

    /**
     * 3. Nueva función para comprobar permisos y activar la capa de ubicación
     */
    private fun enableMyLocation() {
        // Comprueba si el mapa (mMap) ya fue inicializado
        if (!::mMap.isInitialized) return

        // 4. Comprueba si el permiso ya fue CONCEDIDO
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Si el permiso está concedido, activa la capa "mi ubicación"
            mMap.isMyLocationEnabled = true
        } else {
            // 5. Si el permiso NO está concedido, se lo pedimos al usuario.
            requestLocationPermission()
        }
    }

    /**
     * 6. Nueva función que lanza el diálogo de solicitud de permiso
     */
    private fun requestLocationPermission() {
        // Lanza el diálogo de permiso que registramos al inicio
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}