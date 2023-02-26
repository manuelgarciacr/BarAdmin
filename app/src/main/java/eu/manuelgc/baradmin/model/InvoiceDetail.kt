package eu.manuelgc.baradmin.model

import androidx.room.Entity

@Entity(tableName = "invoice_detail", primaryKeys = ["invoiceId", "line"])
data class InvoiceDetail(
    val invoiceId: String,
    val line: Int,
    val groupId: String,
    val subgroupId: String,
    val productId: String,
    val serviceId: String,
    var name: String,
    var quantity: Int,
    var price: Int, // Price in cents with tax
) {
    override fun toString() = "$invoiceId $line"
}
