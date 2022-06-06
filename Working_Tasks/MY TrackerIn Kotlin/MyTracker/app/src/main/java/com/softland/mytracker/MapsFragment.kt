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
    private lateinit  var mMap: GoogleMap
    var latt: Double? =null
    var lot: Double? =null
    var origin: LatLng? = null
    var dest: LatLng? = null
    var mapready:Boolean=false
    lateinit var LocationModel: List<LocationModel>
    var arraylist = ArrayList<locations>()
    var firstTime:Boolean = true


//    var arrayList = ArrayList<locations>()

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
    }

//    private fun updatetask() {
//        if (mapready && LocationModel!=null){
//           LocationModel.forEach{
//               val marker=LatLng(it.lattitude,it.longittude)
//               Log.e("TAG", "updatetask: $marker", )
//               mMap.addMarker(MarkerOptions().position(marker).title(it.toString()))
//           }
//        }
//
//    }

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
                    LocationModel->this.LocationModel= listOf(LocationModel)

                    Log.e("TAG", "startLocationUpdate: $LocationModel" )
                    updateTask();

//                    val marker=LatLng(LocationModel.lattitude,LocationModel.longittude)
//
//                    mMap.addMarker(MarkerOptions().position(marker).title(LocationModel.toString()))

//                        latt=it.lattitude
//                    lot=it.longittude
//                    Toast.makeText(requireContext(),"uyuy",Toast.LENGTH_LONG).show()
//                    Log.e("TAG", "startLocationUpdate: $latt,$lot")
//                    arrayList.add(locations(latt!!, lot!!))
//                    Log.e("TAG", "startLocationUpdate: ${arrayList.get(0).latti}")
//                    origin = LatLng(arrayList[0].latti, arrayList[0].logiti)
//                    drawPolylines()


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

    private fun updateTask() {

      arraylist.add(locations(LocationModel.get(0).lattitude,LocationModel.get(0).longittude))
        Log.e("TAG", "updateTask: ${arraylist.size}", )
        if (firstTime) {
            origin = LatLng(arraylist.get(0).lattitude, arraylist.get(0).longittude)
            mMap.addMarker(MarkerOptions().position(origin!!).title(LocationModel.toString()))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin!!, 15f))
        }
        firstTime=false
        drawPolylines()


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
        for (i in arraylist) {

            val position = LatLng(i.lattitude,i.longittude)
            points.add(position)
            Log.e("TAG", "drawPolylines: $points", )
        }
        lineOptions.addAll(points)
        lineOptions.width(18f)
        lineOptions.color(Color.BLUE)
                lineOptions.geodesic(true);

            mMap!!.addPolyline(lineOptions)

    }

    override fun onDestroy() {
        super.onDestroy()
        firstTime=true
    }
}