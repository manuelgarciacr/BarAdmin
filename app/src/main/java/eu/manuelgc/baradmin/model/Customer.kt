package eu.manuelgc.baradmin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey @ColumnInfo(name = "id") val customerId: String,
    val name: String,
    val company: String,
    val cif: String,
    val email: String,
    val address: String,
    val telephone: String,
    val imageUrl: String = ""
) {
    override fun toString() = "$name, $company, $cif"
}
