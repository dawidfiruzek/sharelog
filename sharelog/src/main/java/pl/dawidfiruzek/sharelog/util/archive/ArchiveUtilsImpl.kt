package pl.dawidfiruzek.sharelog.util.archive

import android.app.Activity
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal class ArchiveUtilsImpl(private val activity: Activity) : ArchiveUtils {

    override fun makePackage(filename: String, filenamesToArchive: List<String>, success: Callback, failure: Callback) {
        Thread {
            try {
                val path = activity.getExternalFilesDir(null).absolutePath + "/" + filename
                val bufferSize = 4096

                val fos = FileOutputStream(path)
                val zos = ZipOutputStream(BufferedOutputStream(
                        fos))
                val data = ByteArray(bufferSize)

                filenamesToArchive.forEach {
                    val origin: BufferedInputStream
                    Log.v("Compressing", "Adding $it")
                    val fi = FileInputStream(activity.getExternalFilesDir(null).absolutePath + "/" + it)
                    origin = BufferedInputStream(fi, bufferSize)

                    val entry = ZipEntry(it)
                    zos.putNextEntry(entry)


                    while (true) {
                        val count = origin.read(data, 0, bufferSize)
                        if (count == -1) break
                        zos.write(data, 0, count)
                    }

                    origin.close()
                    zos.closeEntry()
                }

                zos.close()
                activity.runOnUiThread { success.invoke() }
            } catch (e: Exception) {
                activity.runOnUiThread { failure.invoke() }
            }
        }.run()
    }
}
