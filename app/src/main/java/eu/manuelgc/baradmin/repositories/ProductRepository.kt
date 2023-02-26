package eu.manuelgc.baradmin.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import eu.manuelgc.baradmin.dao.*
import eu.manuelgc.baradmin.model.*
import kotlinx.coroutines.flow.*

// Declares the DAOs as private properties in the constructor. Pass in the DAOs
// instead of the whole database, because you only need access to the DAOs
class ProductRepository (
    private val groupDao: GroupDao,
    private val subgroupDao: SubgroupDao,
    private val productDao: ProductDao,
    private val serviceDao: ServiceDao,
    private val productServiceDao: ProductServiceDao
) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
//    val allGroups: Flow<List<Group>>
//        get() = groupDao.getGroups()
    fun getGroups(): Flow<List<Product>>
        = productDao.getGroups()
    fun getSubgroups(groupId: String): Flow<List<Product>>
        = productDao.getSubgroups(groupId)
    suspend fun getProduct(groupId: String, subgroupId: String, productId: String, serviceId: String)
        = productDao.getProduct(groupId, subgroupId, productId, serviceId)
    fun getProducts(groupId: String, subgroupId: String): Flow<List<Product>>
        = productDao.getProducts(groupId, subgroupId)
    fun getProductServices(groupId: String, subgroupId: String, productId: String): Flow<List<Product>>
            = productDao.getProductServices(groupId, subgroupId, productId)
//    fun getServicesWithProduct(productId: String): Flow<List<ProductService>>
//        = productServiceDao.getServicesWithProduct(productId)
//    fun getMixesWithProduct(productId: String): Flow<List<Product>>
//        = productDao.getMixesWithProduct(productId)

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
   // @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(group: Group) {
        groupDao.insert(group)
    }
    @WorkerThread
    suspend fun insert(subgroup: Subgroup) {
        subgroupDao.insert(subgroup)
    }
    @WorkerThread
    suspend fun insert(product: Product) {
        productDao.insert(product)
    }
    @WorkerThread
    suspend fun insert(service: Service) {
        serviceDao.insert(service)
    }
    @WorkerThread
    suspend fun insert(productService: ProductService) {
        productServiceDao.insert(productService)
    }

    @WorkerThread
    suspend fun deleteAllGroups() {
        groupDao.deleteAll()
    }
    @WorkerThread
    suspend fun deleteAllSubgroups() {
        subgroupDao.deleteAll()
    }
    @WorkerThread
    suspend fun deleteAllProducts() {
        productDao.deleteAll()
    }
    @WorkerThread
    suspend fun deleteAllServices() {
        serviceDao.deleteAll()
    }
    @WorkerThread
    suspend fun deleteAllProductsServices() {
        productServiceDao.deleteAll()
    }
}