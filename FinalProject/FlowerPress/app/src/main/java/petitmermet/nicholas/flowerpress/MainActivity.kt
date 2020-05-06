package petitmermet.nicholas.flowerpress

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Environment.getExternalStorageDirectory
import android.text.Editable
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import java.io.ByteArrayOutputStream
import java.io.File
import java.sql.Blob
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.objectbox.BoxStore.context


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    var currentPictureBitmap: Bitmap? = null
    private lateinit var mMap: GoogleMap

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                //message.setText(R.string.title_dashboard)
                replaceFragment(GalleryView())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                //message.setText(R.string.title_notifications)
                val mapFragment = com.google.android.gms.maps.SupportMapFragment()
                mapFragment.getMapAsync(this)
                replaceFragment(mapFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_picture -> {
                //message.setText(R.string.new_picture)
                replaceFragment(CreateFlower())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        checkRunTimePermission()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(GalleryView())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0){
            currentPictureBitmap = data?.getExtras()?.get("data") as Bitmap
            //imageView.setImageBitmap(currentPictureBitmap)
        }
    }

    fun selectButton(index:Int){
        navigation.getMenu().getItem(index).setChecked(true);
    }

    fun replaceFragment(fragment:Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val box = ObjectBox.boxStore.boxFor(Flower::class.java)

        box.all.forEach {
            if(it.latitude != 0.0 && it.latitude != 0.0) {
                val position = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(position).title(it.name).snippet(it.description))
                var name = it.name
            }
        }
        mMap.setOnInfoWindowClickListener {
            val marker = it
            val output = box.query().equal(Flower_.name, marker.title).equal(Flower_.description, marker.snippet).build().find()
            var name = ""
            if(output.size != 0){
                name = output.get(0).name
            }
            val bundle:Bundle = Bundle()
            bundle.putString("activity", "map")
            bundle.putLong("id", output.get(0).id)
            bundle.putBoolean("edit", true)
            val frag = CreateFlower()
            frag.arguments = bundle
            replaceFragment(frag)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            var lm: LocationManager =
                ContextCompat.getSystemService(
                    this.applicationContext,
                    LocationManager::class.java
                ) as LocationManager
            var location: Location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var longitude = location.longitude
            var latitude = location.latitude
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude,longitude)))
        }
    }
    fun checkRunTimePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                10
            )
        }
    }
}

