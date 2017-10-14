package pl.dawidfiruzek.sharelog

import android.app.Activity
import android.os.Handler
import android.view.MotionEvent
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import pl.dawidfiruzek.sharelog.util.archive.ArchiveUtils
import pl.dawidfiruzek.sharelog.util.cleanup.CleanupUtils
import pl.dawidfiruzek.sharelog.util.date.DateUtils
import pl.dawidfiruzek.sharelog.util.logs.LogsUtils
import pl.dawidfiruzek.sharelog.util.screenshot.ScreenshotUtils
import pl.dawidfiruzek.sharelog.util.share.ShareUtils
import pl.dawidfiruzek.sharelog.util.toast.ToastUtils
import java.lang.reflect.Constructor
import pl.dawidfiruzek.sharelog.util.archive.Callback as ArchiveCallback
import pl.dawidfiruzek.sharelog.util.logs.Callback as LogsCallback
import pl.dawidfiruzek.sharelog.util.screenshot.Callback as ScreenshotCallback

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
                    DateUtils::class.java,
                    ToastUtils::class.java,
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
    private lateinit var dateUtils: DateUtils

    @Mock
    private lateinit var toastUtils: ToastUtils

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

        `when`(dateUtils.getFormattedDateString(ArgumentMatchers.anyString())).thenReturn("123")

        sharelog = sharelogConstructor.newInstance(
                activity,
                handler,
                dateUtils,
                toastUtils,
                screenshotUtils,
                logsUtils,
                archiveUtils,
                shareUtils,
                cleanupUtils
        ) as Sharelog
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(
                screenshotUtils,
                logsUtils,
                archiveUtils,
                shareUtils,
                cleanupUtils,
                dateUtils,
                toastUtils)
    }

    protected fun mockScreenshotFailed() {
        doAnswer {
            val failure = it.arguments[2] as ScreenshotCallback
            failure.invoke()
            return@doAnswer null
        }.`when`(screenshotUtils).takeScreenshot(ArgumentMatchers.anyString(), any(), any())
    }

    protected fun verifyScreenshotFailed() {
        verifyDateUtils()
        verifyScreenshotCalled()
        verifyToast()
    }

    private fun verifyDateUtils() {
        verify(dateUtils, times(1)).getFormattedDateString(ArgumentMatchers.anyString())
    }

    private fun verifyScreenshotCalled() {
        verify(screenshotUtils, times(1)).takeScreenshot(ArgumentMatchers.anyString(), any(), any())
    }

    private fun verifyToast() {
        verify(toastUtils, times(1)).showToast(any(), ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())
    }

    protected fun mockScreenshotSuccess_logsFailed() {
        mockScreenshotSuccess()
        mockLogsFailed()
    }

    private fun mockScreenshotSuccess() {
        doAnswer {
            val success = it.arguments[1] as ScreenshotCallback
            success.invoke()
            return@doAnswer null
        }.`when`(screenshotUtils).takeScreenshot(ArgumentMatchers.anyString(), any(), any())
    }

    private fun mockLogsFailed() {
        doAnswer {
            val failure = it.arguments[2] as LogsCallback
            failure.invoke()
            return@doAnswer null
        }.`when`(logsUtils).collectLogs(ArgumentMatchers.anyString(), any(), any())
    }

    protected fun verifyScreenshotSuccess_logsFailed() {
        verifyDateUtils()
        verifyScreenshotCalled()
        verifyLogsCalled()
        verifyToast()
    }

    private fun verifyLogsCalled() {
        verify(logsUtils, times(1)).collectLogs(ArgumentMatchers.anyString(), any(), any())
    }

    protected fun mockScreenshotSuccess_logsSuccess_archiveFailed() {
        mockScreenshotSuccess()
        mockLogsSuccess()
        mockArchiveFailed()
    }

    private fun mockLogsSuccess() {
        doAnswer {
            val success = it.arguments[1] as LogsCallback
            success.invoke()
            return@doAnswer null
        }.`when`(logsUtils).collectLogs(ArgumentMatchers.anyString(), any(), any())

    }

    private fun mockArchiveFailed() {
        doAnswer {
            val failure = it.arguments[3] as ArchiveCallback
            failure.invoke()
            return@doAnswer null
        }.`when`(archiveUtils).makePackage(ArgumentMatchers.anyString(), ArgumentMatchers.anyList(), any(), any())
    }

    protected fun verifyScreenshotSuccess_logsSuccess_archiveFailed() {
        verifyDateUtils()
        verifyScreenshotCalled()
        verifyLogsCalled()
        verifyArchiveCalled()
        verifyToast()
    }

    private fun verifyArchiveCalled() {
        verify(archiveUtils, times(1)).makePackage(ArgumentMatchers.anyString(), ArgumentMatchers.anyList(), any(), any())
    }

    protected fun mockScreenshotSuccess_logsSuccess_archiveSuccess() {
        mockScreenshotSuccess()
        mockLogsSuccess()
        mockArchiveSuccess()

    }

    private fun mockArchiveSuccess() {
        doAnswer {
            val success = it.arguments[2] as ArchiveCallback
            success.invoke()
            return@doAnswer null
        }.`when`(archiveUtils).makePackage(ArgumentMatchers.anyString(), ArgumentMatchers.anyList(), any(), any())
    }

    protected fun verifyScreenshotSuccess_logsSuccess_archiveSuccess() {
        verifyDateUtils()
        verifyScreenshotCalled()
        verifyLogsCalled()
        verifyArchiveCalled()
        verifyShareCalled()
        verifyCleanupCalled()
    }

    private fun verifyShareCalled() {
        verify(shareUtils, times(1)).share(ArgumentMatchers.anyString())
    }

    private fun verifyCleanupCalled() {
        verify(cleanupUtils, times(1)).cleanup(ArgumentMatchers.anyString())
    }

    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    protected fun <T> any(c: Class<T>): T {
        Mockito.any<T>(c)
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}
