package eu.manuelgc.baradmin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class Service(
    @PrimaryKey @ColumnInfo(name = "id") val serviceId: String,
    val name: String,
    val description: String,
    val imageUrl: String = ""
) {
    override fun toString() = name
}
