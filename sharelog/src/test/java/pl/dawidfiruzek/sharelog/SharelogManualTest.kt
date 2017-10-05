package pl.dawidfiruzek.sharelog

import org.junit.Test

class SharelogManualTest : BaseSharelogTest() {

    @Test
    fun capture_screenshotFailed() {
        mockScreenshotFailed()

        sharelog.capture()

        verifyScreenshotFailed()
    }

    @Test
    fun capture_logsFailed() {
        mockScreenshotSuccess_logsFailed()

        sharelog.capture()

        verifyScreenshotSuccess_logsFailed()
    }

    @Test
    fun capture_archiveFailed() {
        mockScreenshotSuccess_logsSuccess_archiveFailed()

        sharelog.capture()

        verifyScreenshotSuccess_logsSuccess_archiveFailed()
    }

    @Test
    fun capture_success() {
        mockScreenshotSuccess_logsSuccess_archiveSuccess()

        sharelog.capture()

        verifyScreenshotSuccess_logsSuccess_archiveSuccess()
    }
}
