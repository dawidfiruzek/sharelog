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
import pl.dawidfiruzek.sharelog.util.date.DateUtils
import pl.dawidfiruzek.sharelog.util.date.DateUtilsImpl
import pl.dawidfiruzek.sharelog.util.logs.LogsUtils
import pl.dawidfiruzek.sharelog.util.logs.LogsUtilsImpl
import pl.dawidfiruzek.sharelog.util.screenshot.ScreenshotUtils
import pl.dawidfiruzek.sharelog.util.screenshot.ScreenshotUtilsImpl
import pl.dawidfiruzek.sharelog.util.share.ShareUtils
import pl.dawidfiruzek.sharelog.util.share.ShareUtilsImpl
import pl.dawidfiruzek.sharelog.util.toast.ToastUtils
import pl.dawidfiruzek.sharelog.util.toast.ToastUtilsImpl
import java.util.*

class Sharelog private constructor(
        private val activity: Activity,
        private val handler: Handler,
        private val dateUtils: DateUtils,
        private val toastUtils: ToastUtils,
        private val screenshotUtils: ScreenshotUtils,
        private val logsUtils: LogsUtils,
        private val archiveUtils: ArchiveUtils,
        private val shareUtils: ShareUtils,
        private val cleanupUtils: CleanupUtils
) {

    companion object {
        fun getInstance(activity: Activity) =
                Sharelog(
                        activity,
                        Handler(),
                        DateUtilsImpl(),
                        ToastUtilsImpl(),
                        ScreenshotUtilsImpl(activity),
                        LogsUtilsImpl(activity),
                        ArchiveUtilsImpl(activity),
                        ShareUtilsImpl(activity),
                        CleanupUtilsImpl(activity)
                )

        private const val dateFormat = "yyyy_MM_dd_hh_mm_ss"
        private const val tapTimeout = 300L
    }

    private var tapCounter = 0
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
        val date = dateUtils.getFormattedDateString(dateFormat)
        val screenshotFileName = date + ".png"
        val logsFileName = date + ".txt"
        val archiveFileName = date + ".zip"

        screenshotUtils.takeScreenshot(screenshotFileName, {
            logsUtils.collectLogs(logsFileName, {
                archiveUtils.makePackage(archiveFileName, arrayListOf(screenshotFileName, logsFileName), {
                    shareUtils.share(archiveFileName)
                    cleanupUtils.cleanup(archiveFileName)
                }, {
                    toastUtils.showToast(activity, "Failed to create archive")
                })
            }, {
                toastUtils.showToast(activity, "Failed to collect logs")
            })
        }, {
            toastUtils.showToast(activity, "Failed to take a screenshot")
        })
    }
}
