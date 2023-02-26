package eu.manuelgc.baradmin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import eu.manuelgc.baradmin.repositories.InvoiceViewModel
import eu.manuelgc.baradmin.repositories.InvoiceViewModelFactory
import eu.manuelgc.baradmin.ui.Ticket.TicketFragment

class SaleActivity : AppCompatActivity() {
    private lateinit var model: InvoiceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)

        model = ViewModelProvider(this, InvoiceViewModelFactory(
            (application as BarAdminApplication).invoiceRepository,
            (application as BarAdminApplication).productRepository)
        )[InvoiceViewModel::class.java]

        if (model.dataItems.value?.size == 0)
            model.resetCurrentInvoice()
    }

}