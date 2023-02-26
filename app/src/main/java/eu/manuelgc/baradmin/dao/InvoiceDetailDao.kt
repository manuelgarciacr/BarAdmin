package eu.manuelgc.baradmin.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.manuelgc.baradmin.model.InvoiceDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDetailDao {

    @Query("SELECT * FROM invoice_detail ORDER BY invoiceId, line ASC")
    fun getOrderedInvoicesDetails(): Flow<List<InvoiceDetail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoiceDetail: InvoiceDetail)

    @Query("DELETE FROM invoice_detail")
    suspend fun deleteAll()
}