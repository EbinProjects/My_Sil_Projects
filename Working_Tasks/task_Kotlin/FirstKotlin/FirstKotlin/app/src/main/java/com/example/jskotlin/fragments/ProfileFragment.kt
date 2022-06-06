@file:Suppress("DEPRECATION")

package com.example.jskotlin.fragments
import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.fragment.app.Fragment
import com.example.jskotlin.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.*
import java.util.*


class ProfileFragment: Fragment(), View.OnClickListener {
    private val permissionId = 4
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        tvUploadImage.setOnClickListener(this)
        tvCurrentLocation.setOnClickListener(this)
        tvShowLocation.setOnClickListener(this)
    }

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled1()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        tvShowLocation.text = "Latitude\n" +
                                "${list[0].latitude}\n Longitude\n" +
                                "${list[0].longitude} \n Address\n" +
                                "${list[0].getAddressLine(0)}"
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

        private fun selectImage() {
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Add Photo!")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                if (options[item] == "Take Photo") {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 1)
                } else if (options[item] == "Choose from Gallery") {
                    val intent =
                        Intent(
                            Intent.ACTION_GET_CONTENT,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                    intent.type = "image/*"
                    startActivityForResult(intent, 2)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            })
            builder.show()
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.tvUploadImage -> {
                    selectImage()
                }
                R.id.tvCurrentLocation -> {
                    getLocation()
                }
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == RESULT_OK) {
                if (requestCode == 1) {
                    ivImage.setImageBitmap(data!!.getExtras()!!.get("data") as Bitmap?)

                } else if (requestCode == 2) {
                    try {
                        val imageUri: Uri = data!!.getData()!!
                        val imageStream: InputStream =
                            requireActivity().getContentResolver().openInputStream(imageUri)!!
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        ivImage.setImageBitmap(selectedImage)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


    private fun isLocationEnabled1(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
}


