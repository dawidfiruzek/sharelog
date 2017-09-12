package pl.dawidfiruzek.sharelog.util.screenshot

internal typealias SuccessCallback = () -> Unit
internal typealias FailureCallback = () -> Unit

internal interface ScreenshotUtils {

    fun takeScreenshot(fileName: String, success: SuccessCallback, failure: FailureCallback)
}
