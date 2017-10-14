package pl.dawidfiruzek.sharelog.util.cleanup

internal interface CleanupUtils {

    companion object {
        const val TAG = "CleanupUtils"
    }

    fun cleanup(currentArchiveFilename: String)
}
