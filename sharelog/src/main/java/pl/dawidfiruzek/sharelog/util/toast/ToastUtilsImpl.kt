package pl.dawidfiruzek.sharelog.util.toast

import android.app.Activity
import android.widget.Toast

internal class ToastUtilsImpl : ToastUtils {

    override fun showToast(activity: Activity, text: CharSequence, duration: Int) {
        Toast.makeText(activity, text, duration).show()
    }
}
