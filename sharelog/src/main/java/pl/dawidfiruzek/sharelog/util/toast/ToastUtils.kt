package pl.dawidfiruzek.sharelog.util.toast

import android.app.Activity
import android.widget.Toast

interface ToastUtils {

    fun showToast(activity: Activity, text: CharSequence, duration: Int = Toast.LENGTH_SHORT)
}
