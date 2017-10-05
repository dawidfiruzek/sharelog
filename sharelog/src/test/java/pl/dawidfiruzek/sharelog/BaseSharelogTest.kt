package pl.dawidfiruzek.sharelog

import android.app.Activity
import android.os.Handler
import android.view.MotionEvent
import org.junit.After
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

abstract class BaseSharelogTest {

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
    protected lateinit var motionEvent: MotionEvent

    protected lateinit var sharelog: Sharelog

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

    protected fun mockScreenshotFailed() {
        TODO("Not implemented")
    }

    protected fun verifyScreenshotFailed() {
        TODO("Not implemented")
    }

    protected fun mockScreenshotSuccess_logsFailed() {
        TODO("Not implemented")
    }

    protected fun verifyScreenshotSuccess_logsFailed() {
        TODO("Not implemented")
    }

    protected fun mockScreenshotSuccess_logsSuccess_archiveFailed() {
        TODO("Not implemented")
    }

    protected fun verifyScreenshotSuccess_logsSuccess_archiveFailed() {
        TODO("Not implemented")
    }

    protected fun mockScreenshotSuccess_logsSuccess_archiveSuccess() {
        TODO("Not implemented")
    }

    protected fun verifyScreenshotSuccess_logsSuccess_archiveSuccess() {
        TODO("Not implemented")
    }
}
