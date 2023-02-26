package eu.manuelgc.baradmin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products", primaryKeys = ["groupId", "subgroupId", "productId", "serviceId"])
data class Product(
    val groupId: String,
    val subgroupId: String,
    val productId: String,
    val serviceId: String,
    val name: String,
    val sequence: Int,
    val imageUrl: String = "",
    val product1Id: String = "",
    val product2Id: String = "",
    val product3Id: String = "",
    val product4Id: String = "",
    val root: Boolean = false,
    val price: Int = 0,
    val isFinal: Boolean = false
) {
    override fun toString() = name
}
