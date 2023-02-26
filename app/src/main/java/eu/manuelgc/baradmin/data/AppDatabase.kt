package eu.manuelgc.baradmin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import eu.manuelgc.baradmin.dao.*
import eu.manuelgc.baradmin.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val DATABASE_NAME = "bar_admin-db"

@Database(
    entities = [
        Group::class,
        Subgroup::class,
        Product::class,
        Service::class,
        ProductService::class,
        Customer::class,
        Invoice::class,
        InvoiceDetail::class], version = 1, exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun subgroupDao(): SubgroupDao
    abstract fun productDao(): ProductDao
    abstract fun serviceDao(): ServiceDao
    abstract fun productServiceDao(): ProductServiceDao
    abstract fun customerDao(): CustomerDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun invoiceDetailDao(): InvoiceDetailDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let { database ->
                scope.launch {
                    val groupDao = database.groupDao()
                    val subgroupDao = database.subgroupDao()
                    val productDao = database.productDao()
                    val serviceDao = database.serviceDao()
                    val productServiceDao = database.productServiceDao()

                    // Delete all content here.
                    groupDao.deleteAll()
                    subgroupDao.deleteAll()
                    productDao.deleteAll()
                    serviceDao.deleteAll()
                    productServiceDao.deleteAll()

                    var product: Product

                    // Level 0
                    product = Product("INFUSIONES", "CAFE", "", "", "Café", 1, "@drawable/drinks", root=true)
                    productDao.insert(product)
                    product = Product("LICORES", "", "", "", "Licores y vinos", 2, "@drawable/drinks", root=true)
                    productDao.insert(product)
                    product = Product("INFUSIONES", "", "", "", "Infusiones", 3, "@drawable/coffee", root=true)
                    productDao.insert(product)
                    product = Product("VARIOS", "", "", "", "Varios", 4, "@drawable/drinks", root=true)
                    productDao.insert(product)
                    // Level 1 LICORES
                    product = Product("LICORES", "WHISKY", "", "", "Whisky", 1, "@drawable/whisky")
                    productDao.insert(product)
                    product = Product("LICORES", "GINEBRA", "", "", "Ginebra", 2, "@drawable/gin")
                    productDao.insert(product)
                    // Level 1 VARIOS
                    product = Product("VARIOS", "REFRESCOS", "", "", "Refrescos", 1, "@drawable/drinks")
                    productDao.insert(product)
                    // Level 2 LICORES WHISKY
                    product = Product("LICORES","WHISKY","BALLANTINES","","Ballantines",1,"@drawable/ballantines","","","","")
                    productDao.insert(product)
                    product = Product("LICORES","WHISKY","J&B","","J&B",2,"@drawable/jb","","","","")
                    productDao.insert(product)
                    // Level 2 VARIOS REFRESCOS
                    product = Product("VARIOS", "REFRESCOS", "COCACOLA", "", "Cocacola", 1, "@drawable/drinks", "", "", "", "")
                    productDao.insert(product)
                    product = Product("VARIOS", "REFRESCOS", "FANTALIMON", "", "Fanta Limon", 2, "@drawable/drinks", "", "", "", "")
                    productDao.insert(product)
                    // Level 3 LICORES WHISKY BALLANTINES
                    product = Product("LICORES","WHISKY","BALLANTINES","COPA","Ballantines Copa",1,"@drawable/ballantines","","","","", false, 315, true)
                    productDao.insert(product)
                    product = Product("LICORES","WHISKY","BALLANTINES","CHUPITO","Ballantines Chupito",2,"@drawable/ballantines","","","","", false, 215, true)
                    productDao.insert(product)
                    product = Product("LICORES","WHISKY","BALLANTINES","MIX","Ballantines CocaCola",3,"@drawable/ballantines","COCACOLA","","","", false, 535,true)
                    productDao.insert(product)
                    product = Product("LICORES","WHISKY","BALLANTINES","MIX","Ballantines con limón",4,"@drawable/ballantines","FANTALIMON","","","", false, 525, true)
                    productDao.insert(product)
                    // Level 3 LICORES WHISKY J&B
                    product = Product("LICORES","WHISKY","J&B","COPA","J&B Copa",1,"@drawable/jb","","","","")
                    productDao.insert(product)
                    product = Product("LICORES","WHISKY","J&B","CHUPITO","J&B Chupito",2,"@drawable/jb","","","","")
                    productDao.insert(product)
                    product = Product("LICORES","WHISKY","J&B","","J&B CocaCola",3,"@drawable/jb","COCACOLA","","","")
                    productDao.insert(product)
                    product = Product("LICORES","WHISKY","J&B","","J&B con limón",4,"@drawable/jb","FANTALIMON","","","")
                    productDao.insert(product)

                    // Add sample products
                    var service = Service("COPA","copa","copa","urlss")
                    serviceDao.insert(service)
                    service = Service("CHUPITO","chupito","chupito","urlss")
                    serviceDao.insert(service)

                    // Add sample product services
//                    var productService = ProductService("LICORES","WHISKY","BALLANTINES", "", "", 1, "", "SOLO")
//                    productServiceDao.insert(productService)
//                    productService = ProductService("LICORES", "WHISKY", "BALLANTINES","","", 2, "", "CHUPITO")
//                    productServiceDao.insert(productService)

                    // Mixes
//                    product = Product("LICORES","WHISKY","BALLANTINES","Ballantines con cocacola","@drawable/ballantines",1,"Whisky Ballantines con cocacola","COCACOLA","","","")
//                    productDao.insert(product)
//                    product = Product("LICORES","WHISKY","BALLANTINES","Ballantines con limón","@drawable/ballantines",1,"Whisky Ballantines con limón","FANTALIMON","","","")
//                    productDao.insert(product)
//                    product = Product("LICORES","WHISKY","J&B","J&B con cocacola","@drawable/ballantines",1,"Whisky J&B con cocacola","COCACOLA","","","")
//                    productDao.insert(product)
//                    product = Product("LICORES","WHISKY","J&B","J&B con limón","@drawable/ballantines",1,"Whisky J&B con limón","FANTALIMON","","","")
//                    productDao.insert(product)
                }
            }
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context,
                        scope
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(AppDatabaseCallback(scope))
                //.createFromAsset("database/bar_admin-db.db")
                .build()
        }
    }
}
