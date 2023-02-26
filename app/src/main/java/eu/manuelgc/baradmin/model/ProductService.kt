package eu.manuelgc.baradmin.model

import androidx.room.Entity

@Entity(tableName = "product_service", primaryKeys = ["productId", "serviceId"])
data class ProductService(
    override val groupId: String,
    override val subgroupId: String,
    override val productId: String,

    override val name: String,
    override val imageUrl: String,
    override val sequence: Int,
    val serviceId: String
) : DataItem()