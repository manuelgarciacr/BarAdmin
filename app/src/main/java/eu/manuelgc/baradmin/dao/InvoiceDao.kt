package eu.manuelgc.baradmin.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.manuelgc.baradmin.model.Invoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {

    @Query("SELECT * FROM invoices ORDER BY invoiceId ASC")
    fun getOrderedInvoices(): Flow<List<Invoice>>

    @Query("SELECT MAX(number) FROM invoices WHERE series = :series")
    suspend fun getMaxInvoiceNumberBySeries(series: String): Int

    @Query("SELECT MAX(invoiceId) FROM invoices")
    suspend fun getMaxInvoiceId(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoice: Invoice)

    @Query("DELETE FROM invoices")
    suspend fun deleteAll()
}