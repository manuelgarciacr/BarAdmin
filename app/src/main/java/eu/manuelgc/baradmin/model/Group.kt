package eu.manuelgc.baradmin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group(
    @PrimaryKey override val groupId: String,
    override val subgroupId: String = "",
    override val productId: String = "",
    override val name: String,
    override val imageUrl: String = "",
    override val sequence: Int,
    val description: String = ""
): DataItem() {
    override fun toString() = name
}