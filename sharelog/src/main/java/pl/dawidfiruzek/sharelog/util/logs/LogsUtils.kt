package pl.dawidfiruzek.sharelog.util.logs

internal typealias Callback = () -> Unit

internal interface LogsUtils {

    fun collectLogs(filename: String, success: Callback, failure: Callback)
}
