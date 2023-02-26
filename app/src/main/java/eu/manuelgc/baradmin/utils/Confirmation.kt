package eu.manuelgc.baradmin.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

fun fnUtilConfirmation(context: Context, callBack: (DialogInterface, Int) -> Unit, messageId: Int, positiveId: Int, negativeId: Int) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(messageId)
        .setCancelable(false)
//        .setPositiveButton("Yes") { dialog, id ->
//        }
        .setPositiveButton(positiveId, callBack)
        .setNegativeButton(negativeId) { dialog, id ->
            // Dismiss the dialog
            dialog.dismiss()
        }
    val alert = builder.create()
    alert.show()
}