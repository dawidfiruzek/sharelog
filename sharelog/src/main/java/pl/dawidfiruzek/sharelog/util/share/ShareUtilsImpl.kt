package pl.dawidfiruzek.sharelog.util.share

import android.app.Activity
import android.content.Intent
import android.support.v4.content.FileProvider
import java.io.File

internal class ShareUtilsImpl(private val activity: Activity) : ShareUtils {

    override fun share(filename: String) {
        val path = activity.getExternalFilesDir(null).absolutePath + "/" + filename
        val uri = FileProvider.getUriForFile(
                activity,
                "pl.dawidfiruzek.sharelog.fileprovider",
                File(path))

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "application/zip"
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        activity.startActivity(sendIntent)
    }
}
