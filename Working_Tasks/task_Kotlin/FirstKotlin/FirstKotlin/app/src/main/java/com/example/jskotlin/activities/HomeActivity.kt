package com.example.jskotlin.activities


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.example.jskotlin.R
import com.example.jskotlin.adapter.MyAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var navigationView: NavigationView
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val intent = intent
        val message = intent.getStringExtra("message")
        if(!message.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Notification")
                .setMessage(message)
                .setPositiveButton("Ok", { dialog, which -> }).show()
        }
        checkRunTimePermission()
        actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this, drawer,
                com.example.jskotlin.R.string.nav_open,
                com.example.jskotlin.R.string.nav_close
            )

        drawer.addDrawerListener(actionBarDrawerToggle!!);
        actionBarDrawerToggle!!.syncState();
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tbLayout!!.addTab(tbLayout!!.newTab().setText("Tab 1"))
        tbLayout!!.addTab(tbLayout!!.newTab().setText("Tab 2"))
        tbLayout!!.addTab(tbLayout!!.newTab().setText("Tab 3"))
        tbLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MyAdapter(this, supportFragmentManager, tbLayout!!.tabCount)
        viewPager!!.offscreenPageLimit=3
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tbLayout))
        tbLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {


            }
        })
        ivMenu.setOnClickListener(View.OnClickListener {
            drawer.openDrawer(GravityCompat.START)
        })

        navigationView = findViewById(R.id.nav)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.nav_account) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun checkRunTimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

            ) {

            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    10
                )
            }
        } else {

        }
    }


    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val id = p0.itemId
        when (id) {
            R.id.nav_account -> {
                Toast.makeText(this, "My account option is clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Settings option is clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Logout option is clicked", Toast.LENGTH_SHORT).show()
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}




