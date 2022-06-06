package com.softland.mytracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapsFragment : Fragment() {
    private lateinit var locationViewModel: LocationViewMOdel
    private val permissionId = 2
    private var mMap: GoogleMap? = null
    var latt: Double? =null
    var lot: Double? =null
    var origin: LatLng? = null
    var dest: LatLng? = null

    var arrayList = ArrayList<locations>()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mMap = googleMap

//        val sydney = LatLng(-34.0, 151.0)
        origin?.let { MarkerOptions().position(it).title("Marker in Sydney") }
            ?.let { googleMap.addMarker(it) }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin!!, 15f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        locationViewModel = ViewModelProvider(this).get(LocationViewMOdel::class.java)
        startLocationUpdate()


    }

    private fun startLocationUpdate() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                locationViewModel.getlocation().observe(requireActivity(), Observer {

                        latt=it.lattitude
                    lot=it.longittude
                    Toast.makeText(requireContext(),"uyuy",Toast.LENGTH_LONG).show()
                    Log.e("TAG", "startLocationUpdate: $latt,$lot")
                    arrayList.add(locations(latt!!, lot!!))
                    Log.e("TAG", "startLocationUpdate: ${arrayList.get(0).latti}")
                    origin = LatLng(arrayList[0].latti, arrayList[0].logiti)
                    drawPolylines()


                })
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermissions()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =requireContext().
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Debug.getLocation()
            }
        }
    }
    private fun drawPolylines() {
        val points = ArrayList<LatLng>()
        val lineOptions = PolylineOptions()
        for (i in arrayList) {
            val position = LatLng(arrayList.get.latti, arrayList[i].logiti)
            points.add(position)
        }
        lineOptions.addAll(points)
        lineOptions.width(18f)
        lineOptions.color(Color.BLUE)
                lineOptions.geodesic(true);

            mMap!!.addPolyline(lineOptions)

    }
}