package petitmermet.nicholas.flowerpress

import android.Manifest
import android.app.Activity.LOCATION_SERVICE
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeByteArray
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.fragment_create_flower.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CreateFlower.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CreateFlower.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CreateFlower : Fragment() {

    var editFlower: Boolean = false
    var previousActivity: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arg = arguments
        if(arg != null){
            editFlower = arg.getBoolean("edit")
            previousActivity = arg.getString("activity")
        }
        if(!editFlower) {
            val view: View = inflater.inflate(R.layout.fragment_create_flower, container, false)

            val box = ObjectBox.boxStore.boxFor(Flower::class.java)

            val takePicture: Button = view.findViewById(R.id.takePicture) as Button
            val savePicture: Button = view.findViewById(R.id.savePicture) as Button

            takePicture.setOnClickListener {
                dispatchTakePictureIntent()
            }

            savePicture.setOnClickListener {
                val flower = Flower()
                flower.name = flowerName.text.toString()
                flower.description = flowerDescription.text.toString()

                if(flower.name == ""){
                    Toast.makeText(activity, "Please enter a name for the flower, can be changed later", Toast.LENGTH_SHORT).show()
                } else {
                    var longitude: Double = 0.0
                    var latitude: Double = 0.0

                    if (ActivityCompat.checkSelfPermission(
                            activity!!.applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        var lm: LocationManager =
                            getSystemService(
                                activity!!.applicationContext,
                                LocationManager::class.java
                            ) as LocationManager
                        var location: Location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        longitude = location.longitude
                        latitude = location.latitude
                    }
                    flower.latitude = latitude
                    flower.longitude = longitude
                    flower.uri = currentPhotoPath
                    box.put(flower)
                    Toast.makeText(activity!!.applicationContext, "Flower has been added!", Toast.LENGTH_SHORT).show()
                    flowerName.setText("")
                    flowerDescription.setText("")
                    val uri: Uri = Uri.parse("drawable/picture_frame")
                    imageView.setImageURI(uri)
                }
            }
            return view
        } else {
            val view: View = inflater.inflate(R.layout.fragment_create_flower, container, false)

            val box = ObjectBox.boxStore.boxFor(Flower::class.java)

            val takePicture: Button = view.findViewById(R.id.takePicture) as Button
            val savePicture: Button = view.findViewById(R.id.savePicture) as Button
            val imageView: ImageView = view.findViewById(R.id.imageView) as ImageView
            val flowerName: EditText = view.findViewById(R.id.flowerName) as EditText
            val flowerDescription: EditText = view.findViewById(R.id.flowerDescription) as EditText

            savePicture.text = getString(R.string.save_changes)
            takePicture.visibility = View.INVISIBLE

            val flower:Flower = box.get(arg!!.getLong("id"))

            imageView.setImageURI(Uri.parse(flower.uri))
            flowerName.text =  SpannableStringBuilder(flower.name)
            flowerDescription.text = SpannableStringBuilder(flower.description)

            savePicture.setOnClickListener {
                if(flowerName.text.equals("")){
                    Toast.makeText(activity, "Please enter a name", Toast.LENGTH_SHORT).show()
                } else {
                    val id: Long = arg!!.getLong("id")
                    val flower = box.get(id)
                    flower.name = flowerName.text.toString()
                    flower.description = flowerDescription.text.toString()
                    box.put(flower)
                    if (previousActivity.equals("map")) {
                        val mapFragment = com.google.android.gms.maps.SupportMapFragment()
                        mapFragment.getMapAsync(activity as MainActivity)
                        (activity as MainActivity).selectButton(2)
                        (activity as MainActivity).replaceFragment(mapFragment)
                    }
                    if (previousActivity.equals("gallery")) {
                        (activity as MainActivity).selectButton(0)
                        (activity as MainActivity).replaceFragment(GalleryView())
                    }
                }
            }
            return view
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val context:Context = getActivity()!!.getApplicationContext()
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        "petitmermet.nicholas.flowerpress.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode == RESULT_OK) {
            imageView.setImageURI(Uri.parse(currentPhotoPath))
//            imageView.setImageBitmap(currentPictureBitmap)
        }
    }
}
