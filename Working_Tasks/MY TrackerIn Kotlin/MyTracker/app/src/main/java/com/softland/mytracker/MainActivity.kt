package com.softland.mytracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //        barcodeListAdapter = new BarcodeListAdapter(Barcodenumbers, MainActivity.this);

//
//        recycler_vew_barcode_number = (RecyclerView) findViewById(R.id.recycler_vew_barcode_number);
//        recycler_vew_barcode_number.setAdapter(barcodeListAdapter);
//        recycler_vew_barcode_number.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        val fragment: Fragment = MapsFragment()

        // Open fragment
        supportFragmentManager
            .beginTransaction().replace(R.id.frame_layout, fragment)
            .commit()
    }
}