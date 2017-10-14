package pl.dawidfiruzek.sharelog.util.cleanup

import android.app.Activity
import android.util.Log
import android.widget.Toast
import java.io.File

internal class CleanupUtilsImpl(private val activity: Activity) : CleanupUtils {

    override fun cleanup(currentArchiveFilename: String) {
        Thread {
            val dirPath = activity.getExternalFilesDir(null).absolutePath

            try {
                val dir = File(dirPath)
                if (dir.isDirectory) {
                    removeFiles(dir, currentArchiveFilename)
                }
            } catch (e: Exception) {
                activity.runOnUiThread {
                    Toast.makeText(activity, "Error during cleanup", Toast.LENGTH_SHORT).show()
                }
                Log.e(CleanupUtils.TAG, "Error during cleanup", e)
            }
        }.run()
    }

    private fun removeFiles(dir: File, currentArchiveFilename: String) {
        dir.list().forEach {
            if (it != currentArchiveFilename) File(dir, it).delete()
        }
    }
}
