package pl.dawidfiruzek.sharelog

import android.app.Activity
import android.os.Handler
import android.text.format.DateFormat
import android.view.MotionEvent
import android.widget.Toast
import pl.dawidfiruzek.sharelog.SharelogGestureMode.MANUAL
import pl.dawidfiruzek.sharelog.util.archive.ArchiveUtils
import pl.dawidfiruzek.sharelog.util.archive.ArchiveUtilsImpl
import pl.dawidfiruzek.sharelog.util.cleanup.CleanupUtils
import pl.dawidfiruzek.sharelog.util.cleanup.CleanupUtilsImpl
import pl.dawidfiruzek.sharelog.util.logs.LogsUtils
import pl.dawidfiruzek.sharelog.util.logs.LogsUtilsImpl
import pl.dawidfiruzek.sharelog.util.screenshot.ScreenshotUtils
import pl.dawidfiruzek.sharelog.util.screenshot.ScreenshotUtilsImpl
import pl.dawidfiruzek.sharelog.util.share.ShareUtils
import pl.dawidfiruzek.sharelog.util.share.ShareUtilsImpl
import java.util.*

class Sharelog(private val activity: Activity) {

    companion object {
        private const val dateFormat = "yyyy_MM_dd_hh_mm_ss"
        private const val tapTimeout = 300L
    }

    private var tapCounter = 0
    private val handler: Handler = Handler()

    private val screenshotUtils: ScreenshotUtils = ScreenshotUtilsImpl(activity)
    private val logsUtils: LogsUtils = LogsUtilsImpl(activity)
    private val archiveUtils: ArchiveUtils = ArchiveUtilsImpl(activity)
    private val shareUtils: ShareUtils = ShareUtilsImpl(activity)
    private val cleanupUtils: CleanupUtils = CleanupUtilsImpl(activity)

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

    private fun sharelog() {
        val date = DateFormat.format(dateFormat, Date()).toString()
        val screenshotFileName = date + ".png"
        val logsFileName = date + ".txt"
        val archiveFileName = date + ".zip"

        screenshotUtils.takeScreenshot(screenshotFileName, {
            logsUtils.collectLogs(logsFileName, {
                archiveUtils.makePackage(archiveFileName, arrayListOf(screenshotFileName, logsFileName), {
                    shareUtils.share(archiveFileName)
                    cleanupUtils.cleanup(archiveFileName)
                }, {
                    Toast.makeText(activity, "Failed to create archive", Toast.LENGTH_SHORT).show()
                })
            }, {
                Toast.makeText(activity, "Failed to collect logs", Toast.LENGTH_SHORT).show()
            })
        }, {
            Toast.makeText(activity, "Failed to take a screenshot", Toast.LENGTH_SHORT).show()
        })
    }
}
