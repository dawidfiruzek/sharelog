package pl.dawidfiruzek.sharelog.util.logs

internal typealias SuccessCallback = () -> Unit
internal typealias FailureCallback = () -> Unit

internal interface LogsUtils {

    fun collectLogs(fileName: String, success: SuccessCallback, failure: FailureCallback)
}
