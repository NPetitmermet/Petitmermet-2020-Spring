package petitmermet.nicholas.flowerpress

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class Map : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val box = ObjectBox.boxStore.boxFor(Flower::class.java)

        box.all.forEach {
            if(it.latitude != 0.0 && it.latitude != 0.0) {
                var position = LatLng(it.latitude, it.longitude)
                mMap.addMarker(MarkerOptions().position(position).title(it.name))
            }
        }
        mMap.setOnInfoWindowClickListener {
            val flower:Flower? = box.query().equal(Flower_.name,it.title).and().equal(Flower_.description,it.snippet).build().findFirst()
            val bundle:Bundle = Bundle()
            bundle.putString("activity", "map")
        }
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    }
}
