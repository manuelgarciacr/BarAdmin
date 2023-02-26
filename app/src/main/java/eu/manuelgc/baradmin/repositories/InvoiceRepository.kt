package eu.manuelgc.baradmin.repositories

import eu.manuelgc.baradmin.dao.InvoiceDao
import eu.manuelgc.baradmin.dao.InvoiceDetailDao
import eu.manuelgc.baradmin.model.Product
import kotlinx.coroutines.flow.Flow

class InvoiceRepository(
    private val invoiceDao: InvoiceDao,
    private val invoiceDetailDao: InvoiceDetailDao
) {

    suspend fun getMaxInvoiceNumber(series: String): Int?
        = invoiceDao.getMaxInvoiceNumberBySeries(series)

    suspend fun getMaxInvoiceId(): String?
            = invoiceDao.getMaxInvoiceId()
}