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
                activity,
                handler,
                screenshotUtils,
                logsUtils,
                archiveUtils,
                shareUtils,
                cleanupUtils)
    }

    @Test
    fun captureManual() {
        TODO("Not implemented")
    }

    @Test
    fun captureGesture() {
        TODO("Not implemented")
    }

    @Test
    fun captureBadGesture() {
        sharelog.capture(motionEvent)

        //nothing should happen because of not set mode
    }
}
