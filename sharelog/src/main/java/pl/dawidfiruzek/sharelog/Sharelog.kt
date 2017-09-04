package pl.dawidfiruzek.sharelog

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Process
import android.text.format.DateFormat
import android.util.Log
import android.view.MotionEvent
import pl.dawidfiruzek.sharelog.SharelogGestureMode.MANUAL
import java.io.*
import java.util.*

class Sharelog(private val activity: Activity) {

    companion object {
        private const val tapTimeout = 300L
    }

    private var tapCounter = 0
    private val handler: Handler = Handler()
    private val processId = Process.myPid().toString()

    private var mode: SharelogGestureMode = MANUAL

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
        val date = DateFormat.format("yyyy_MM_dd_hh:mm:ss", Date()).toString()
        val screenshotPath = takeScreenshot(date)
        val logsFilePath = collectLogs(date)
        makePackage()
        sharePackage()
        Log.d("Sharelog", "Sharelogged")
    }

    private fun takeScreenshot(date: String) : String {
        val path = activity.filesDir.absolutePath + "/" + date + ".png"

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

    private fun collectLogs(date: String) : String {
        val path = activity.filesDir.absolutePath + "/" + date

        val logCommand = arrayOf("logcat", "-d", "-v", "threadtime")
        val process = Runtime.getRuntime().exec(logCommand)
        val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
        val sb = StringBuilder()

        bufferedReader.forEachLine {
            if (it.contains(processId)) sb.append(it)
        }

        val file = File(path)
        val fos = FileOutputStream(file)
        fos.write(sb.toString().toByteArray())
        fos.flush()
        fos.close()

        return path
    }

    private fun makePackage() {}

    private fun sharePackage() {}

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
