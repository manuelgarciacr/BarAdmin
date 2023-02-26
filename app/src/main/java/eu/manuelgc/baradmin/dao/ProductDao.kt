package eu.manuelgc.baradmin.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.manuelgc.baradmin.model.Product
import eu.manuelgc.baradmin.model.Subgroup
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

//    @Query("SELECT * FROM products ORDER BY groupId, subgroupId, sequence ASC")
//    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE root = true ORDER BY sequence ASC")
    fun getGroups(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE groupId = :groupId AND subgroupId != '' AND productId = '' AND root = false ORDER BY sequence ASC")
    fun getSubgroups(groupId: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE groupId = :groupId AND subgroupId = :subgroupId AND productId = :productId AND serviceId = :serviceId")
    suspend fun getProduct(groupId: String, subgroupId: String, productId: String, serviceId: String): Product

    @Query("SELECT * FROM products WHERE groupId = :groupId AND subgroupId = :subgroupId AND productId != '' AND serviceId = '' AND root = false ORDER BY groupId, subgroupId, productId, sequence ASC")
    fun getProducts(groupId: String, subgroupId: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE groupId = :groupId AND subgroupId = :subgroupId AND productId = :productId AND serviceId != '' AND root = false ORDER BY groupId, subgroupId, productId, sequence ASC")
    fun getProductServices(groupId: String, subgroupId: String, productId: String): Flow<List<Product>>

    //    @Query("SELECT * FROM products WHERE id = :productId AND product1Id != \"\" ORDER BY groupId, subgroupId, sequence ASC")
//    fun getMixesWithProduct(productId: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}