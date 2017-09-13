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

    override fun makePackage(filename: String, fileNamesToArchive: List<String>, success: Callback, failure: Callback) {
        try {
            val path = activity.getExternalFilesDir(null).absolutePath + "/sharelog_" + filename
            val BUFFER = 4096

            val fos = FileOutputStream(path)
            val zos = ZipOutputStream(BufferedOutputStream(
                    fos))
            val data = ByteArray(BUFFER)

            fileNamesToArchive.forEach {
                val origin: BufferedInputStream
                Log.v("Compressing", "Adding $it")
                val fi = FileInputStream(activity.getExternalFilesDir(null).absolutePath + "/" + it)
                origin = BufferedInputStream(fi, BUFFER)

                val entry = ZipEntry(it)
                zos.putNextEntry(entry)


                while (true) {
                    val count = origin.read(data, 0, BUFFER)
                    if (count == -1) break
                    zos.write(data, 0, count)
                }

                origin.close()
                zos.closeEntry()
            }

            zos.close()
            success.invoke()
        } catch (e: Exception) {
            failure.invoke()
        }
    }
}
