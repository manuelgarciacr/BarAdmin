package eu.manuelgc.baradmin

import android.app.Application
import eu.manuelgc.baradmin.data.AppDatabase
import eu.manuelgc.baradmin.repositories.InvoiceRepository
import eu.manuelgc.baradmin.repositories.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BarAdminApplication: Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the productRepository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getInstance(this, applicationScope) }
    val productRepository by lazy { ProductRepository(
        database.groupDao(),
        database.subgroupDao(),
        database.productDao(),
        database.serviceDao(),
        database.productServiceDao()
    ) }
    val invoiceRepository by lazy {
        InvoiceRepository(
            database.invoiceDao(),
            database.invoiceDetailDao()
        )
    }
}