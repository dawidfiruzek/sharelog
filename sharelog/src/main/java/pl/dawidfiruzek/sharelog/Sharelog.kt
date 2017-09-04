package pl.dawidfiruzek.sharelog

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.Process
import android.support.v4.content.FileProvider
import android.text.format.DateFormat
import android.util.Log
import android.view.MotionEvent
import pl.dawidfiruzek.sharelog.SharelogGestureMode.MANUAL
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class Sharelog(private val activity: Activity) {

    companion object {
        private const val tapTimeout = 300L
    }

    private var tapCounter = 0
    private val handler: Handler = Handler()
    private val processId = Process.myPid().toString()

    private var mode: SharelogGestureMode = MANUAL

    private var date: String = ""

    /**
     * Setting gesture mode. Default mode is MANUAL.
     */
    fun setGestureMode(mode: SharelogGestureMode): Sharelog {
        this.mode = mode
        return this
    }

    fun capture(motionEvent: MotionEvent? = null) {
        motionEvent?.let {
            if (mode == MANUAL) return else {
                countTaps(it)
            }
        } ?: sharelog()
    }

    private fun sharelog() {
        date = DateFormat.format("yyyy_MM_dd_hh_mm_ss", Date()).toString()
        val screenshotPath = takeScreenshot()
        val logsFilePath = collectLogs()
        val zipFilePath = makePackage(screenshotPath, logsFilePath)
        share(zipFilePath)
        Log.d("Sharelog", "Sharelogged")
    }

    private fun takeScreenshot() : String {
        val path = activity.getExternalFilesDir(null).absolutePath + "/" + date + ".png"

        val rootView = activity.window.decorView.rootView
        rootView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(rootView.drawingCache)
        rootView.isDrawingCacheEnabled = false

        val file = File(path)
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.flush()
        fos.close()

        return path
    }

    private fun collectLogs() : String {
        val path = activity.getExternalFilesDir(null).absolutePath + "/" + date + ".txt"

        val logCommand = arrayOf("logcat", "-d", "-v", "threadtime")
        val process = Runtime.getRuntime().exec(logCommand)
        val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
        val sb = StringBuilder()

        bufferedReader.forEachLine {
            if (it.contains(processId)) sb.append(it).append("\n")
        }

        val file = File(path)
        val fos = FileOutputStream(file)
        fos.write(sb.toString().toByteArray())
        fos.flush()
        fos.close()

        return path
    }

    private fun makePackage(vararg filePaths: String) : String {
        val path = activity.getExternalFilesDir(null).absolutePath + "/sharelog_" + date + ".zip"
        val BUFFER = 4096

        val fos = FileOutputStream(path)
        val zos = ZipOutputStream(BufferedOutputStream(
                fos))
        val data = ByteArray(BUFFER)

        filePaths.forEach {
            val origin: BufferedInputStream
            Log.v("Compressing", "Adding $it")
            val fi = FileInputStream(it)
            origin = BufferedInputStream(fi, BUFFER)

            val entry = ZipEntry(it.substring(it.lastIndexOf("/") + 1))
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
        return path
    }

    private fun share(filePath: String) {
        val uri = FileProvider.getUriForFile(
                activity,
                BuildConfig.APPLICATION_ID + ".provider",
                File(filePath))

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "application/zip"
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        activity.startActivity(sendIntent)
    }

    private fun countTaps(motionEvent: MotionEvent) {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                if (tapCounter == 0) {
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({ clearCounter() }, tapTimeout * mode.tapsNumber)
                }

                ++tapCounter

                if (tapCounter == mode.tapsNumber) {
                    handler.removeCallbacksAndMessages(null)
                    clearCounter()
                    sharelog()
                }
            }
        }
    }

    private fun clearCounter() {
        tapCounter = 0
    }
}

enum class SharelogGestureMode(val tapsNumber: Int) {
    MANUAL(0),
    TRIPLE_TAP(3),
    QUAD_TAP(4),
    QUINT_TAP(5)
}
