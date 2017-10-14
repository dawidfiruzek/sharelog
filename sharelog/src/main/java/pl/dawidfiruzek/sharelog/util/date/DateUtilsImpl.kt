package pl.dawidfiruzek.sharelog.util.date

import android.text.format.DateFormat
import java.util.*

class DateUtilsImpl : DateUtils {

    override fun getFormattedDateString(format: String): String =
            DateFormat.format(format, Date()).toString()
}
