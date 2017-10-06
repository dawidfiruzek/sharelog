package pl.dawidfiruzek.sharelog

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import pl.dawidfiruzek.sharelog.SharelogGestureMode.MANUAL

@RunWith(Parameterized::class)
class SharelogGestureTest(private val gestureMode: SharelogGestureMode) : BaseSharelogTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<SharelogGestureMode> =
                SharelogGestureMode.values().filter { it.tapsNumber > MANUAL.tapsNumber }
    }

    @Test
    fun capture_tooLittleTaps() {
        sharelog.setGestureMode(gestureMode)

        (1 until gestureMode.tapsNumber).forEach {
            sharelog.capture(motionEvent)
        }

        //nothing should happen because of too little taps
    }

    @Test
    fun capture_manualGestureMode() {
        sharelog.setGestureMode(MANUAL)

        (1..gestureMode.tapsNumber).forEach {
            sharelog.capture(motionEvent)
        }

        //nothing should happen because of explicit set of MANUAL mode
    }

    @Test
    fun capture_noGestureSet() {
        (1..gestureMode.tapsNumber).forEach {
            sharelog.capture(motionEvent)
        }

        //nothing should happen because of not set mode which is default set to MANUAL
    }

    @Test
    fun capture_screenshotFailed() {
        sharelog.setGestureMode(gestureMode)
        mockScreenshotFailed()

        (1..gestureMode.tapsNumber).forEach {
            sharelog.capture(motionEvent)
        }

        verifyScreenshotFailed()
    }

    @Test
    fun capture_logsFailed() {
        sharelog.setGestureMode(gestureMode)
        mockScreenshotSuccess_logsFailed()

        (1..gestureMode.tapsNumber).forEach {
            sharelog.capture(motionEvent)
        }

        verifyScreenshotSuccess_logsFailed()
    }

    @Test
    fun capture_archiveFailed() {
        sharelog.setGestureMode(gestureMode)
        mockScreenshotSuccess_logsSuccess_archiveFailed()

        (1..gestureMode.tapsNumber).forEach {
            sharelog.capture(motionEvent)
        }

        verifyScreenshotSuccess_logsSuccess_archiveFailed()
    }

    @Test
    fun capture_success() {
        sharelog.setGestureMode(gestureMode)
        mockScreenshotSuccess_logsSuccess_archiveSuccess()

        (1..gestureMode.tapsNumber).forEach {
            sharelog.capture(motionEvent)
        }

        verifyScreenshotSuccess_logsSuccess_archiveSuccess()
    }
}
