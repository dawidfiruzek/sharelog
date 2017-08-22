package pl.dawidfiruzek.sharelog

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import pl.dawidfiruzek.sharelog.SharelogGestureMode.MANUAL

class Sharelog(private val context: Context) {

    private var touchTimestamp = 0L
    private var tapCounter = 0

    private var mode: SharelogGestureMode = MANUAL

    /**
     * Setting gesture mode. Default mode is MANUAL.
     */
    fun setGestureMode(mode: SharelogGestureMode) {
        this.mode = mode
    }

    fun capture(motionEvent: MotionEvent? = null) {
        motionEvent?.let {
            if (mode == MANUAL) return else {
                countTaps(it)
            }
        } ?: if (mode != MANUAL) return else {
            sharelog()
        }
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
            MotionEvent.ACTION_DOWN -> touchTimestamp = System.currentTimeMillis()
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - touchTimestamp > ViewConfiguration.getTapTimeout()) {
                    clear()
                } else if (++tapCounter == mode.tapsNumber) {
                    clear()
                    sharelog()
                }
            }
        }
    }

    private fun clear() {
        touchTimestamp = 0
        tapCounter = 0
    }
}

enum class SharelogGestureMode(val tapsNumber: Int) {
    MANUAL(0),
    TRIPLE_TAP(3),
    QUAD_TAP(4),
    QUINT_TAP(5)
}
