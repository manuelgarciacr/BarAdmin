package eu.manuelgc.baradmin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import eu.manuelgc.baradmin.databinding.ActivityMainBinding
import eu.manuelgc.baradmin.repositories.*


class MainActivity : AppCompatActivity() {
//    private val model: InvoiceViewModel = ViewModelProvider(this, InvoiceViewModelFactory(
//        application.invoiceRepository,
//        (application as BarAdminApplication).productRepository))
//        .get(InvoiceViewModel::class.java)
//    private val model: InvoiceViewModel by ViewModels {
//        InvoiceViewModelFactory(
//            (application as BarAdminApplication).invoiceRepository,
//            (application as BarAdminApplication).productRepository)
//    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
Log.e("RRRRRRRRRRRRRACTIVITY", this.toString())
        binding.btnSale.setOnClickListener { onClick(it) }
    }

    /**
     * View onClick handler
     */
    private fun onClick(view: View) {
        when (view.id) {
            R.id.btn_sale -> {// If maps
                val intent = Intent(this, SaleActivity::class.java)

                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }
    }
}