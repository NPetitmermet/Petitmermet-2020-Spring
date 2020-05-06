package petitmermet.nicholas.flowerpress

import android.graphics.Bitmap
import android.net.Uri
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.sql.Blob

//import java.sql.Blob

@Entity
data class Flower(
    @Id var id: Long = 0,
    var uri: String = "",
    var name: String = "",
    var description: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0

)