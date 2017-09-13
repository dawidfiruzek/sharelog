package pl.dawidfiruzek.sharelog.util.archive

internal typealias Callback = () -> Unit

internal interface ArchiveUtils {

    fun makePackage(filename: String, fileNamesToArchive: List<String>, success: Callback, failure: Callback)
}
