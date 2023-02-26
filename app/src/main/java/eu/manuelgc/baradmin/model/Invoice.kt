package eu.manuelgc.baradmin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "invoices")
data class Invoice(
    @PrimaryKey val invoiceId: String,
    val series: String,
    val number: Int,
    val date: Date,
    val customerId: String
) {
    override fun toString() = "$date $number"
}
