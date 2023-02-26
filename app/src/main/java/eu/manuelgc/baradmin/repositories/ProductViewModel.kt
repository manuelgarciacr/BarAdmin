package eu.manuelgc.baradmin.repositories

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.manuelgc.baradmin.model.*
import kotlinx.coroutines.flow.*

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    /**
     * The current filter data
     */
    private val itemFilterFlow = MutableStateFlow<ItemFilter>(ItemFilter("", String(), String()))

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    //val groups: LiveData<List<Group>> = productRepository.allGroups.asLiveData()

    /**
     * Filtered DataItems
     */
    val dataItems: LiveData<List<Product>> = itemFilterFlow.flatMapLatest { itemFilter ->

//Log.e("EEEEEEEEEEE", "$itemFilter ${itemFilter.groupId.length} ${itemFilter.groupId == ""}")
        if (itemFilter.groupId == "") {
            repository.getGroups()
        } else if (itemFilter.subgroupId == ""){
            repository.getSubgroups(itemFilter.groupId)
        } else if (itemFilter.productId == ""){
            repository.getProducts(itemFilter.groupId, itemFilter.subgroupId)
        } else {
            repository.getProductServices(itemFilter.groupId, itemFilter.subgroupId, itemFilter.productId)
//            productRepository.getServicesWithProduct(itemFilter.productId).combine(productRepository.getMixesWithProduct(itemFilter.productId)){
//                i, s -> s + i
//            }
        }
//Log.e("EEEEEEEEEEE", "${flowd.count()}")
    }.asLiveData()


    /**
     * Set filter data
     */
    fun setItemFilter(groupId: String, subgroupId: String, productId: String) {
        itemFilterFlow.value = ItemFilter(groupId, subgroupId, productId)
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}