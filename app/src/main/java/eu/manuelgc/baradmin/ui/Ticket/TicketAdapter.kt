package eu.manuelgc.baradmin.ui.Ticket

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.icu.text.NumberFormat
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.manuelgc.baradmin.R
import eu.manuelgc.baradmin.model.Group
import eu.manuelgc.baradmin.model.DataItem
import eu.manuelgc.baradmin.model.InvoiceDetail
import eu.manuelgc.baradmin.model.Product
import eu.manuelgc.baradmin.utils.scaleBitmap
import java.util.*

typealias Item = InvoiceDetail
const val viewHolderLayout = R.layout.ticket_layout

/**
 * Groups RecyclerView adapter
 */
class TicketAdapter(private val onClick: View.OnClickListener)
    : ListAdapter<Item, TicketAdapter.ViewHolder>(DiffCallback){

    /**
     *  ViewHolder for Museum, takes in the inflated view and the onClick behavior.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * Creates and inflates the ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(viewHolderLayout, parent, false)

        return ViewHolder(view)
    }

    /**
     * Gets current item and uses it to bind view.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val context = itemView.context
        val dataItem = getItem(position)
        // API Level 30
        //val formatter = NumberFormatter.withLocale(Locale("es")).precision(Precision.fixedFraction(3));
        // API Level 24
//        val nf = NumberFormat.getInstance(Locale("es"))
//        nf.isGroupingUsed = false
//        nf.maximumFractionDigits = 2
//        nf.minimumFractionDigits = 2

        val lblName: TextView = itemView.findViewById(R.id.lblName)
        val lblPrice: TextView = itemView.findViewById(R.id.lblPrice)
        val lblTotal: TextView = itemView.findViewById(R.id.lblTotal)
        val params = itemView.layoutParams as RecyclerView.LayoutParams
        val price = dataItem.price
        val quantity = dataItem.quantity
        val total = quantity * price
        val totalFloat = total / 100f
        val last = getItem(0).line
        val card = itemView.findViewById<View>(R.id.cardView)

        if (dataItem.line == 0) {
            lblName.gravity = Gravity.END
            lblPrice.visibility = View.INVISIBLE
            card.setBackgroundColor(Color.parseColor("#e0ffffff"))
        }else if (dataItem.line == last){
            lblName.gravity = Gravity.START
            lblPrice.visibility = View.VISIBLE
            card.setBackgroundColor(Color.parseColor("#ffffffff"))
        } else {
            lblName.gravity = Gravity.START
            lblPrice.visibility = View.VISIBLE
            card.setBackgroundColor(Color.parseColor("#e0ffffff"))
        }

        // Data
        lblName.text = dataItem.name
        lblPrice.text = if (quantity == 1)
                "%.2f".format(price/100f)
            else
                "%dx%.2f".format(quantity, price/100f)
        lblTotal.text = "%.2f".format(totalFloat)
    }

 }

object DiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.invoiceId == newItem.invoiceId && oldItem.line == newItem.line
    }
}
