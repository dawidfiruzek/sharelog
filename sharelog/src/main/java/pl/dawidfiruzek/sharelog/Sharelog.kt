package pl.dawidfiruzek.sharelog

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import pl.dawidfiruzek.sharelog.SharelogGestureMode.MANUAL

class Sharelog(private val context: Context) {

    companion object {
        private const val tapTimeout = 300L
    }

    private var tapCounter = 0
    private val handler: Handler = Handler()

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
        Log.e("Sharelog", "Yeah!")
        takeScreenshot()
        collectLogs()
        makePackage()
        sharePackage()
    }

    private fun takeScreenshot() {}

    private fun collectLogs() {}

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
        Log.e("Sharelog", "clear called")
        tapCounter = 0
    }
}

enum class SharelogGestureMode(val tapsNumber: Int) {
    MANUAL(0),
    TRIPLE_TAP(3),
    QUAD_TAP(4),
    QUINT_TAP(5)
}
