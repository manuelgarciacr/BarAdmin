package eu.manuelgc.baradmin.ui.Ticket

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import eu.manuelgc.baradmin.R

class SwipeToDeleteCallback(private val deleteLine: (Int) -> Unit, private val adapter: TicketAdapter, private val context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.baseline_delete_sweep_48)!!
    private val deleteBackground = ColorDrawable(Color.RED)

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 0

        // Icon
        val iconMargin: Int = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - deleteIcon.getIntrinsicHeight()) / 2
        val iconBottom: Int = iconTop + deleteIcon.getIntrinsicHeight()

        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin
            val iconRight: Int = itemView.left + iconMargin + deleteIcon.intrinsicWidth
            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            deleteBackground.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt() + backgroundCornerOffset,
                itemView.bottom
            )
        } else { // view is unSwiped
            deleteBackground.setBounds(0, 0, 0, 0)
        }

        if (dX > 0) {
            deleteBackground.draw(c)
            deleteIcon.draw(c)
        }

//        if (dX > 0) {
//            deleteBackground.draw(c)
//            deleteIcon.draw(c)
//        } else {
//            shoppingBackground.draw(c)
//            shoppingIcon.draw(c)
//        }

    }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val line = adapter.itemCount

        if (direction == ItemTouchHelper.RIGHT) {
            deleteLine(position)
            //adapter.notifyItemChanged(position)
            adapter.notifyDataSetChanged()
        }
    }

//    override fun getMovementFlags(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder
//    ): Int {
//        val last = adapter.itemCount - 1
//        val fixed = viewHolder.layoutPosition == 0 || viewHolder.layoutPosition == last
//        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
//        //return super.getMovementFlags(recyclerView, viewHolder)
//        return makeMovementFlags(dragFlags, if (fixed) 0 else swipeFlags );
//    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val last = adapter.itemCount - 1
        val fixed = viewHolder.layoutPosition == 0 || viewHolder.layoutPosition == last
        return if (fixed) 0 else super.getSwipeDirs(recyclerView, viewHolder)
    }
}