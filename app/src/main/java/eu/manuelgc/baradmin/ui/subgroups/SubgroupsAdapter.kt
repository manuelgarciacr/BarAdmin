package eu.manuelgc.baradmin.ui.subgroups

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.manuelgc.baradmin.R
import eu.manuelgc.baradmin.model.Group
import eu.manuelgc.baradmin.model.Subgroup
import eu.manuelgc.baradmin.utils.scaleBitmap
import eu.manuelgc.baradmin.utils.setImage

typealias Item = Subgroup
const val viewHolderLayout = R.layout.item_layout

/**
 * Groups RecyclerView adapter
 */
class SubgroupsAdapter(private val onClick: ((Item) -> Unit)?, private val onClick2: View.OnClickListener)
    : ListAdapter<Item, SubgroupsAdapter.ViewHolder>(DiffCallback){
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
        val view = LayoutInflater.from(parent.context)
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
        val subgroup = getItem(position)
        val uri = Uri.parse(subgroup.imageUrl?: "https://i.ibb.co/6nHLXHj/image.png")
        val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
        val lblItem: TextView = itemView.findViewById(R.id.lblItem)
        val params = itemView.layoutParams as RecyclerView.LayoutParams

        // Format
        lblItem.maxLines = numberOfLines
        params.height = itemHeight
        itemView.layoutParams = params
        // Data
        //setImage(context, uri, "ERROR TAG", imgItem, errorImage, placeholderImage)
//        var res = context?.let { ContextCompat.getDrawable(it,context.resources.getIdentifier("coffee.png", "drawable",
//            context.packageName
//        )) }
        val resourceId: Int = context.resources.getIdentifier(subgroup.imageUrl, "drawable", context.packageName)
        imgItem.setImageResource(resourceId)
Log.e("TTTTTTTTTTT", "$resourceId ${subgroup.imageUrl} $uri YYYY $subgroup")
        lblItem.text = subgroup.name
//        itemView.setOnClickListener {
//            group?.let{onClick(it)}
//        }
        itemView.setOnClickListener(onClick2)
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
Log.e("EEEEEEEEE", "$height $textHeight")
        //errorImage = scaleBitmap(itemView.context, R.drawable.coffee, viewWidth, 2 * height + 20)
        errorImage = getDrawable(itemView.context, R.drawable.drinks)
        placeholderImage = scaleBitmap(itemView.context, R.drawable.image, viewWidth, textHeight)
        itemHeight = height
        numberOfLines = textHeight / lblItem.lineHeight
    }
}

object DiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.subgroupId == newItem.subgroupId
    }
}
