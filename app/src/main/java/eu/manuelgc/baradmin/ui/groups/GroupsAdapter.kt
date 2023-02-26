package eu.manuelgc.baradmin.ui.groups

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
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
import eu.manuelgc.baradmin.model.Product
import eu.manuelgc.baradmin.utils.scaleBitmap

typealias Item = Product
const val viewHolderLayout = R.layout.item_layout

/**
 * Groups RecyclerView adapter
 */
class GroupsAdapter(private val onClick: ((Item) -> Unit)?, private val onClick2: View.OnClickListener)
    : ListAdapter<Item, GroupsAdapter.ViewHolder>(DiffCallback){
    // Data to format the itemView (from getDimensions())
    private var errorImage: Drawable? = null
    private var placeholderImage: Drawable? = null
    private var numberOfLines = 0
    private var itemHeight = 0

    /**
     *  ViewHolder for Museum, takes in the inflated view and the onClick behavior.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * Creates and inflates the ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {

        val view = if (viewType == IS_FINAL)
                LayoutInflater.from(parent.context)
                    .inflate(viewHolderLayout, parent, false)
            else
                LayoutInflater.from(parent.context)
                    .inflate(viewHolderLayout, parent, false)

        // Get data to format the itemView
        if (errorImage == null) // If errorImage == null, dimensions have not yet been calculated
            getDimensions(view, parent)

        return ViewHolder(view)
    }

    /**
     * Gets current item and uses it to bind view.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val context = itemView.context
        val dataItem = getItem(position)

        val uri = Uri.parse(dataItem.imageUrl?: "https://i.ibb.co/6nHLXHj/image.png")
        val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
        val lblItem: TextView = itemView.findViewById(R.id.lblItem)
        val params = itemView.layoutParams as RecyclerView.LayoutParams

        configureProductView(itemView, context, dataItem)
        /*// Format
        lblItem.maxLines = numberOfLines
        params.height = itemHeight
        itemView.layoutParams = params
        // Data
        //setImage(context, uri, "ERROR TAG", imgItem, errorImage, placeholderImage)
//        var res = context?.let { ContextCompat.getDrawable(it,context.resources.getIdentifier("coffee.png", "drawable",
//            context.packageName
//        )) }
        val resourceId: Int = context.resources.getIdentifier(dataItem.imageUrl, "drawable", context.packageName)
        imgItem.setImageResource(resourceId)
Log.e("TTTTTTTTTTT", "${dataItem.groupId} ${dataItem.subgroupId} ${dataItem.productId} $dataItem")
        lblItem.text = dataItem.name
//        itemView.setOnClickListener {
//            group?.let{onClick(it)}
//        }
        itemView.tag = Triple(dataItem.groupId, dataItem.subgroupId, dataItem.productId)
        itemView.setOnClickListener(onClick2)*/
    }

    /**
     * Calculate dimensions to format the itemView
     */
    private fun getDimensions(itemView: View, parent: ViewGroup) {
        val viewWidth = parent.width
        val viewHeight = parent.height
        // Item height less than 420 pixels
        val calcItemHeight: (Int) -> Int = {
            var i = 0
            var h: Int
            do {
                h = it / ++i
            } while (h > 420)
            h
        }

        val height = calcItemHeight(viewHeight)
        val textHeight = height / 2 - 20
        val lblItem: TextView = itemView.findViewById(R.id.lblItem)
//Log.e("EEEEEEEEE", "$height $textHeight")
        //errorImage = scaleBitmap(itemView.context, R.drawable.coffee, viewWidth, 2 * height + 20)
        errorImage = getDrawable(itemView.context, R.drawable.drinks)
        placeholderImage = scaleBitmap(itemView.context, R.drawable.image, viewWidth, textHeight)
        itemHeight = height
        numberOfLines = textHeight / lblItem.lineHeight
    }

    fun configureInvoiceView(itemView: View, context: Context, dataItem: Item) {
        val btnPlus = itemView.findViewById<Button>(R.id.btnPlus)
        val btnMinus = itemView.findViewById<Button>(R.id.btnMinus)
        val lblQuantity = itemView.findViewById<TextView>(R.id.lblQuantity)
        val edtPrice = itemView.findViewById<EditText>(R.id.edtPrice)
        val lblName = itemView.findViewById<TextView>(R.id.lblName)
        val lblTotal = itemView.findViewById<TextView>(R.id.lblTotal)
        val params = itemView.layoutParams as RecyclerView.LayoutParams

        // Format

        lblName.maxLines = numberOfLines
        params.height = itemHeight
        itemView.layoutParams = params

        // Data
        lblQuantity.text = 1.toString()
        edtPrice.setText(dataItem.name)
        lblTotal.text = dataItem.price.toString()
    }
    fun configureProductView(itemView: View, context: Context, dataItem: Item) {
        val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
        val lblItem: TextView = itemView.findViewById(R.id.lblItem)
        val params = itemView.layoutParams as RecyclerView.LayoutParams

        // Format
        lblItem.maxLines = numberOfLines
        params.height = itemHeight
        itemView.layoutParams = params

        // Data
        val resourceId: Int = context.resources.getIdentifier(dataItem.imageUrl, "drawable", context.packageName)
        imgItem.setImageResource(resourceId)
        lblItem.text = dataItem.name
        itemView.tag = Triple(dataItem.groupId, dataItem.subgroupId, Pair(dataItem.productId, dataItem.serviceId))
        itemView.setOnClickListener(onClick2)
    }

//    override fun getItemViewType(position: Int) = when (getItem(position).isFinal) {
//        is true-> GROUP_ITEM
//        is DataItem.ProductItem -> PRODUCT_ITEM
//        else -> 0
//    }
    override fun getItemViewType(position: Int) = if (getItem(position).isFinal) IS_FINAL
        else IS_NOT_FINAL

    companion object {
        private const val IS_FINAL = 1
        private const val IS_NOT_FINAL = 2
    }
}

object DiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.groupId == newItem.groupId && oldItem.subgroupId == newItem.subgroupId &&
                oldItem.productId == newItem.productId && oldItem.serviceId == newItem.serviceId
    }
}
