package pl.dawidfiruzek.sharelog.util.logs

import android.app.Activity
import android.os.Process
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

internal class LogsUtilsImpl(private val activity: Activity) : LogsUtils {

    private val processId = Process.myPid().toString()

    override fun collectLogs(filename: String, success: Callback, failure: Callback) {
        Thread {
            try {
                val path = activity.getExternalFilesDir(null).absolutePath + "/" + filename

                val logCommand = arrayOf("logcat", "-d", "-v", "threadtime")
                val process = Runtime.getRuntime().exec(logCommand)
                val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
                val sb = StringBuilder()

                bufferedReader.forEachLine {
                    if (it.contains(processId)) sb.append(it).append("\n")
                }

                val file = File(path)
                val fos = FileOutputStream(file)
                fos.write(sb.toString().toByteArray())
                fos.flush()
                fos.close()
                activity.runOnUiThread { success.invoke() }
            } catch (e: Exception) {
                activity.runOnUiThread { failure.invoke() }
            }
        }.run()
    }
}
