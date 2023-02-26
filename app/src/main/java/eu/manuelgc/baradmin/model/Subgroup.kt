package eu.manuelgc.baradmin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subgroups", primaryKeys = ["groupId", "productId"])
data class Subgroup (
    override val groupId: String,
    override val subgroupId: String,
    override val productId: String = "",
    override val name: String,
    override val imageUrl: String = "",
    override val sequence: Int,
    val description: String = "",
    val single: Boolean
) : DataItem() {
    override fun toString() = name
}
