package eu.manuelgc.baradmin.repositories

import android.util.Log
import androidx.lifecycle.*
import eu.manuelgc.baradmin.model.Invoice
import eu.manuelgc.baradmin.model.InvoiceDetail
import eu.manuelgc.baradmin.model.ItemFilter
import eu.manuelgc.baradmin.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Integer.parseInt
import java.time.Year
import java.util.*

class InvoiceViewModel(
    private val repository: InvoiceRepository,
    private val productRepository: ProductRepository) : ViewModel() {

    /**
     * The current invoice
     */
    private var currentInvoice = Invoice("", "", 0, Date(), "")
    private var currentInvoiceDetail = MutableLiveData<MutableList<InvoiceDetail>>(mutableListOf())
    val dataItems: LiveData<MutableList<InvoiceDetail>> = currentInvoiceDetail

    /**
     * The current product
     */
    var currentProduct: Product? = null
        private set

    /**
     * Get last line
     */
    fun getLastLine(): InvoiceDetail {
        return currentInvoiceDetail.value!!.first()
    }

    /**
     * Reset current invoice
     */
    fun resetCurrentInvoice(series: String = "FS" + Calendar.getInstance().get(Calendar.YEAR)){
        viewModelScope.launch(Dispatchers.Default) {
            val maxId = repository.getMaxInvoiceId()?: "0"
            val maxNumber = repository.getMaxInvoiceNumber(series)?: 0
            val invoiceId = parseInt(maxId) + 1
            val number = maxNumber + 1
            currentInvoice = Invoice(invoiceId.toString(), series, number, Date(), "" )

            withContext(Dispatchers.Main) {
                val total = InvoiceDetail(invoiceId.toString(), 0, "", "", "", "", "Total", 0, 0)
                currentInvoiceDetail.value = mutableListOf(total)
            }
        }
    }

    /**
     * Set current product
     */
    suspend fun setCurrentProduct(groupId: String, subgroupId: String, productId: String, serviceId: String){
        currentProduct = productRepository.getProduct(groupId, subgroupId, productId, serviceId)
    }

    /**
     * Set current product
     */
    fun deleteCurrentProduct(){
        currentProduct = null
    }

    /**
     * Add invoice line
     */
    fun addInvoiceLine(groupId: String, subgroupId: String, productId: String, serviceId: String, name: String, quantity: Int, price: Int){
        val newLine = getLastLine().line + 1
        val invoiceDetail = InvoiceDetail(
            currentInvoice.invoiceId,
            newLine,
            groupId,
            subgroupId,
            productId,
            serviceId,
            name,
            quantity,
            price
        )
        currentInvoiceDetail.value?.add(0, invoiceDetail)
        setTotal()
    }

    /**
     * Actualize first Invoize Line (Last inserted)
     */
    fun setInvoiceLine(name: String?, price: Int?, quantity: Int?) {
        name?.let {currentInvoiceDetail.value!!.first().name = it }
        price?.let { currentInvoiceDetail.value!!.first().price = it }
        quantity?.let { currentInvoiceDetail.value!!.first().quantity = it }
        if (price != null || quantity != null)
            setTotal()
    }

    /**
     * Delete invoice line
     */
    fun deleteInvoiceLine(position: Int) {
        currentInvoiceDetail.value!!.removeAt(position)
        setTotal()
    }

    /**
     * Calculate total
     */
    fun setTotal() {
        var total = 0
        for (it in dataItems.value!!) {
            if (it.line != 0)
                total += it.quantity * it.price
        }
        currentInvoiceDetail.value!!.last().price  = total
        currentInvoiceDetail.value!!.last().quantity = 1
    }

 }

class InvoiceViewModelFactory(
    private val repository: InvoiceRepository,
    private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvoiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
Log.e("RERERERE000", "CREATE VM")
            return InvoiceViewModel(repository, productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}