package eu.manuelgc.baradmin.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.manuelgc.baradmin.model.ProductService
import eu.manuelgc.baradmin.model.Subgroup
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductServiceDao {

    @Query("SELECT * FROM product_service ORDER BY productId, serviceId ASC")
    fun getProductsServices(): Flow<List<ProductService>>

//    @Query("SELECT product_service.groupId as groupId, product_service.subgroupId as subgroupId, product_service.productId as productId" +
//            " , services.name as name, services.imageUrl as imageUrl, product_service.sequence as sequence" +
//            " , product_service.serviceId, product_service.description FROM services INNER JOIN product_service ON product_service.serviceId = services.id WHERE product_service.productId = :productId ORDER BY product_service.sequence ASC")
//    fun getServicesWithProduct(productId: String): Flow<List<ProductService>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productService: ProductService)

    @Query("DELETE FROM product_service")
    suspend fun deleteAll()
}