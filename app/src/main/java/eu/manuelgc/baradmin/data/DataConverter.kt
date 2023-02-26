package eu.manuelgc.baradmin.data

import androidx.room.TypeConverter
import java.util.*

class DataConverter {

    @TypeConverter
    fun dateToLong(date: Date): Long = date.time

    @TypeConverter
    fun longToDate(value: Long): Date = Date(value)
}