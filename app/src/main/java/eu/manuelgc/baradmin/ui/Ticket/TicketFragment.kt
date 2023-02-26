package eu.manuelgc.baradmin.ui.Ticket

import android.graphics.Color
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.os.*
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import eu.manuelgc.baradmin.BarAdminApplication
import eu.manuelgc.baradmin.R
import eu.manuelgc.baradmin.databinding.FragmentTicketBinding
import eu.manuelgc.baradmin.repositories.InvoiceViewModel
import eu.manuelgc.baradmin.repositories.InvoiceViewModelFactory
import eu.manuelgc.baradmin.utils.fnUtilConfirmation
import kotlinx.coroutines.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TicketFragment : Fragment() {
//    private val model: InvoiceViewModel by activityViewModels {
//        InvoiceViewModelFactory(
//            (requireActivity().application as BarAdminApplication).invoiceRepository,
//            (requireActivity().application as BarAdminApplication).productRepository)
//    }
    private lateinit var model: InvoiceViewModel
    private lateinit var binding: FragmentTicketBinding
//    private lateinit var edtPrice: EditText
//    private lateinit var edtQuantity: EditText
//    private lateinit var lblTotal: TextView
//    private lateinit var
    private val ticketListener = View.OnClickListener { view ->
//        val tag = view.tag as Triple<String, String, Pair<String, String>>
    }
    val adapter = TicketAdapter(ticketListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    model.deleteCurrentProduct()
                    model.deleteInvoiceLine(0)
                    adapter.notifyItemChanged(0)
                    isEnabled = false
                    //requireActivity().onBackPressed();
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController

            if(controller != null) {
                controller.hide(WindowInsets.Type.statusBars())
                //controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        binding = FragmentTicketBinding.inflate(inflater, container, false)
        val safeArgs: TicketFragmentArgs by navArgs()
        val recyclerView = binding.rvTicket
        val edtPrice = binding.edtPrice
        val edtQuantity = binding.edtQuantity
        val btnPlus = binding.btnPlus
        val btnMinus = binding.btnMinus
        val btnBack = binding.btnBack
        val btnEnd = binding.btnEnd
        val btnMore = binding.btnMore

        edtPrice.addTextChangedListener (object : TextWatcher {
            var changing = false

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val text = editable.toString()
                val separator = DecimalFormatSymbols(Locale("es")).decimalSeparator
                val priceDots = text.replace(',', '.')
                val priceStr = priceDots.replace('.', separator)
                var priceFloat = 0f

                try {
                    priceFloat = priceDots.toFloat()
                    if (priceFloat == 0f)
                        throw java.lang.NumberFormatException()
                    edtPrice.setTextColor(Color.parseColor("#000000"))
                } catch (ex: Exception) {
                    edtPrice.setTextColor(Color.parseColor("#ff0000"))
                }

                val priceInt = (priceFloat * 100).toInt()
                val name = if (priceInt == model.currentProduct?.price)
                    model.currentProduct?.name
                else
                    model.currentProduct?.name + " plus"

                actualizeTicket(name, priceInt, null)

                if (text != priceStr) {
                    if (changing)
                        return

                    changing = true
                    val newEditable = SpannableStringBuilder(priceStr)
                    editable?.replace(0, editable.length, newEditable)
                    changing = false
                }
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = null

        model = ViewModelProvider(requireActivity(), InvoiceViewModelFactory(
            (requireActivity().application as BarAdminApplication).invoiceRepository,
            (requireActivity().application as BarAdminApplication).productRepository))[InvoiceViewModel::class.java]
//        model.dataItems.observe(viewLifecycleOwner) { dataItems ->
//            adapter.submitList(dataItems)
//        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            if (model.currentProduct == null) {
                with(safeArgs) {
                    model.setCurrentProduct(groupId, subgroupId, productId, serviceId)
                }
                model.currentProduct?.let {
                    with(it) {
                        model.addInvoiceLine(groupId, subgroupId, productId, serviceId, name, 1, price)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                model.dataItems.observe(viewLifecycleOwner) { dataItems ->
                    adapter.submitList(dataItems)
                }
                with(binding) {
                    val priceInt = model.getLastLine().price
                    val priceFloat = priceInt / 100f
                    val quantity = model.getLastLine().quantity

                    edtPrice.text = Editable.Factory.getInstance().newEditable(priceFloat.toString())
                    edtPrice.textLocale = Locale("es", "ES")
                    edtQuantity.text = "x$quantity"
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback(model::deleteInvoiceLine, adapter, this.requireContext())
        )

        itemTouchHelper.attachToRecyclerView(recyclerView)

        /****************************
         * Button Listener
         */
        btnPlus.setOnClickListener {onClickListener(btnPlus)}
        btnMinus.setOnClickListener {onClickListener(btnMinus)}
        btnBack.setOnClickListener {onClickListener(btnBack)}
        btnMore.setOnClickListener {onClickListener(btnMore)}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

//    override fun onResume() {
//        super.onResume()
//        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//    }

    private fun loadProduct(safeArgs: TicketFragmentArgs) {
    }

    private fun onClickListener(view: View?) {
        val quantity = binding.edtQuantity

        when(view) {
            binding.btnPlus -> {
//                var q = quantity.text.toString().toInt()
//                quantity.text = SpannableStringBuilder((++q).toString())
//                actualizeTicket(null, null, q)
                val q = model.getLastLine().quantity + 1
                quantity.text = "x$q"
                actualizeTicket(null, null, q)
            }
            binding.btnMinus -> {
                var q = model.getLastLine().quantity
                if (q > 1) {
                    quantity.text = "x${--q}"
                    actualizeTicket(null, null, q)
                }
            }
            binding.btnBack -> {
                fnUtilConfirmation(requireContext(), { _, _ ->
                    val action = TicketFragmentDirections.backAction("", "", "", "")
                    model.deleteCurrentProduct()
                    model.deleteInvoiceLine(0)
                    adapter.notifyItemChanged(0)
                    findNavController().navigate(action)
                }, R.string.cancel_product, R.string.yes, R.string.no)
            }
            binding.btnMore ->{
                val action = TicketFragmentDirections.backAction("", "", "", "")
                model.deleteCurrentProduct()
                findNavController().navigate(action)
            }
        }
    }

    private fun actualizeTicket(name: String?, price: Int?, quantity: Int?) {
        model.setInvoiceLine(name, price, quantity)
        val totalPos = model.dataItems.value!!.size
        adapter.notifyItemChanged(0)
        adapter.notifyItemChanged(totalPos - 1)
    }
}