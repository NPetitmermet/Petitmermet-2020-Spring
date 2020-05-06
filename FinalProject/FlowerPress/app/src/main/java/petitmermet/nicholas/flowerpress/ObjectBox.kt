import android.content.Context
import io.objectbox.BoxStore
import petitmermet.nicholas.flowerpress.MyObjectBox

object ObjectBox {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}