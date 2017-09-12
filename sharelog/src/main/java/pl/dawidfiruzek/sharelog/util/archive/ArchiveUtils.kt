package pl.dawidfiruzek.sharelog.util.archive

internal typealias SuccessCallback = () -> Unit
internal typealias FailureCallback = () -> Unit

internal interface ArchiveUtils {

    fun makePackage(filePath: String, fileNamesToArchive: List<String>, success: SuccessCallback, failure: FailureCallback)
}
