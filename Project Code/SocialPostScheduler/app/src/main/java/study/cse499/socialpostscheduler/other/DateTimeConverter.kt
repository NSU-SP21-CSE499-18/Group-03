package study.cse499.socialpostscheduler.other

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*

@ProvidedTypeConverter
object DateTimeConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}