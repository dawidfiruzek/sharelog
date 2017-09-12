package pl.dawidfiruzek.sharelog.util.screenshot

import android.app.Activity
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

internal class ScreenshotUtilsImpl(private val activity: Activity) : ScreenshotUtils {

    override fun takeScreenshot(fileName: String, success: SuccessCallback, failure: FailureCallback) {
        try {
            val path = activity.getExternalFilesDir(null).absolutePath + "/" + fileName

            val rootView = activity.window.decorView.rootView
            rootView.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(rootView.drawingCache)
            rootView.isDrawingCacheEnabled = false

            val file = File(path)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            success.invoke()
        } catch (e: Exception) {
            failure.invoke()
        }
    }
}
