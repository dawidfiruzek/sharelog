package pl.dawidfiruzek.sharelog.util.archive

internal typealias Callback = () -> Unit

internal interface ArchiveUtils {

    fun makePackage(filename: String, filenamesToArchive: List<String>, success: Callback, failure: Callback)
}
