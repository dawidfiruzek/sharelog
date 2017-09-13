package pl.dawidfiruzek.sharelog.util.screenshot

internal typealias Callback = () -> Unit

internal interface ScreenshotUtils {

    fun takeScreenshot(filename: String, success: Callback, failure: Callback)
}
