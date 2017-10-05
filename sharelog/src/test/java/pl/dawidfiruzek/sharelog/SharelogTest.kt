package pl.dawidfiruzek.sharelog

import android.app.Activity
import android.os.Handler
import android.view.MotionEvent
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pl.dawidfiruzek.sharelog.util.archive.ArchiveUtils
import pl.dawidfiruzek.sharelog.util.cleanup.CleanupUtils
import pl.dawidfiruzek.sharelog.util.logs.LogsUtils
import pl.dawidfiruzek.sharelog.util.screenshot.ScreenshotUtils
import pl.dawidfiruzek.sharelog.util.share.ShareUtils
import java.lang.reflect.Constructor

class SharelogTest {

    companion object {
        @JvmStatic
        private lateinit var sharelogConstructor: Constructor<out Any>

        @JvmStatic
        @BeforeClass
        fun init() {
            val sharelogClass = Class.forName("pl.dawidfiruzek.sharelog.Sharelog")
            sharelogConstructor = sharelogClass.getDeclaredConstructor(
                    Activity::class.java,
                    Handler::class.java,
                    ScreenshotUtils::class.java,
                    LogsUtils::class.java,
                    ArchiveUtils::class.java,
                    ShareUtils::class.java,
                    CleanupUtils::class.java
            )
            sharelogConstructor.isAccessible = true
        }
    }

    @Mock
    private lateinit var activity: Activity

    @Mock
    private lateinit var handler: Handler

    @Mock
    private lateinit var screenshotUtils: ScreenshotUtils

    @Mock
    private lateinit var logsUtils: LogsUtils

    @Mock
    private lateinit var archiveUtils: ArchiveUtils

    @Mock
    private lateinit var shareUtils: ShareUtils

    @Mock
    private lateinit var cleanupUtils: CleanupUtils

    @Mock
    private lateinit var motionEvent: MotionEvent

    private lateinit var sharelog: Sharelog

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        sharelog = sharelogConstructor.newInstance(
                activity,
                handler,
                screenshotUtils,
                logsUtils,
                archiveUtils,
                shareUtils,
                cleanupUtils
        ) as Sharelog
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(
                screenshotUtils,
                logsUtils,
                archiveUtils,
                shareUtils,
                cleanupUtils)
    }

    @Test
    fun captureManual_screenshotFailed() {
        mockScreenshotFailed()

        sharelog.capture()

        verifyScreenshotFailed()
    }

    @Test
    fun captureManual_logsFailed() {
        mockScreenshotSuccess_logsFailed()

        sharelog.capture()

        verifyScreenshotSuccess_logsFailed()
    }

    @Test
    fun captureManual_archiveFailed() {
        mockScreenshotSuccess_logsSuccess_archiveFailed()

        sharelog.capture()

        verifyScreenshotSuccess_logsSuccess_archiveFailed()
    }

    @Test
    fun captureManual_success() {
        mockScreenshotSuccess_logsSuccess_archiveSuccess()

        sharelog.capture()

        verifyScreenshotSuccess_logsSuccess_archiveSuccess()
    }

    private fun mockScreenshotFailed() {
        TODO("Not implemented")
    }

    private fun verifyScreenshotFailed() {
        TODO("Not implemented")
    }

    private fun mockScreenshotSuccess_logsFailed() {
        TODO("Not implemented")
    }

    private fun verifyScreenshotSuccess_logsFailed() {
        TODO("Not implemented")
    }

    private fun mockScreenshotSuccess_logsSuccess_archiveFailed() {
        TODO("Not implemented")
    }

    private fun verifyScreenshotSuccess_logsSuccess_archiveFailed() {
        TODO("Not implemented")
    }

    private fun mockScreenshotSuccess_logsSuccess_archiveSuccess() {
        TODO("Not implemented")
    }

    private fun verifyScreenshotSuccess_logsSuccess_archiveSuccess() {
        TODO("Not implemented")
    }

    @Test
    fun captureGesture_screenshotFailed() {
        TODO("Not implemented")
    }

    @Test
    fun captureGesture_logsFailed() {
        TODO("Not implemented")
    }

    @Test
    fun captureGesture_archiveFailed() {
        TODO("Not implemented")
    }

    @Test
    fun captureGesture_success() {
        TODO("Not implemented")
    }

    @Test
    fun captureBadGesture() {
        sharelog.capture(motionEvent)

        //nothing should happen because of not set mode
    }

    @Test
    fun captureTriple_tooLittleTaps() {
        sharelog.setGestureMode(SharelogGestureMode.TRIPLE_TAP)

        sharelog.capture(motionEvent)
        sharelog.capture(motionEvent)

        //nothing should happen because of too little taps
    }

    @Test
    fun captureQuad_tooLittleTaps() {
        sharelog.setGestureMode(SharelogGestureMode.QUAD_TAP)

        sharelog.capture(motionEvent)
        sharelog.capture(motionEvent)
        sharelog.capture(motionEvent)

        //nothing should happen because of too little taps
    }

    @Test
    fun captureQuint_tooLittleTaps() {
        sharelog.setGestureMode(SharelogGestureMode.QUINT_TAP)

        sharelog.capture(motionEvent)
        sharelog.capture(motionEvent)
        sharelog.capture(motionEvent)
        sharelog.capture(motionEvent)

        //nothing should happen because of too little taps
    }
}
