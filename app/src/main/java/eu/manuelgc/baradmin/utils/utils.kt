@file:Suppress("unused")

package eu.manuelgc.baradmin.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.signature.ObjectKey
import com.google.android.material.snackbar.Snackbar
//import edu.uoc.android.BuildConfig.APPLICATION_ID
import eu.manuelgc.baradmin.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Returns a string with the package and the line of code to use in the Log.x functions
 *      Usage: private val tag = { classTag(this.javaClass.canonicalName!!)
 * Search stackTrace for the line before (in reverse order) the call to the tag() function itself
 */
//fun classTag (pack: String): String {
//    val trace = Thread.currentThread().stackTrace
//    val traceElement = "$pack\$tag\$"
//    val packageLen = APPLICATION_ID.length
//    val tag = pack.substring(packageLen + 1)
//    var found = false
//
//    for (it in trace) {
//        // Might be more than one line for the tag function
//        if (it.toString().startsWith(traceElement)) {
//            found = true
//            continue
//        }
//        if (found) {
//            val array = it.toString().split(".")
//            return tag + " " + array.takeLast(2).toTypedArray().joinToString(".")
//        }
//    }
//    return pack
//}

/**
 * Ex: minVersion(Build.VERSION_CODES.LOLLIPOP) or minVersion(21)
 */
val minVersion: (Int) -> Boolean = { Build.VERSION.SDK_INT >= it }

/**
 * Scale drawable squared image to fit in with and height (width > height)
 */
fun scaleBitmap(context: Context, id: Int, width: Int, height: Int): Drawable {
    val foreImage = ContextCompat.getDrawable(context, id)
    val foreBitmap = (foreImage as BitmapDrawable).bitmap
    val newForeBitmap = Bitmap.createScaledBitmap(foreBitmap, height, height, true)

    // White background
    val backImage = ContextCompat.getDrawable(context, R.drawable.empty_white)
    val backBitmap = (backImage as BitmapDrawable).bitmap
    val newBackBitmap = Bitmap.createScaledBitmap(backBitmap, width, height, true)

    // Writes foreground centered over th background
    val canvas = Canvas(newBackBitmap)
    val x = (width - height) / 2F
    canvas.drawBitmap(newForeBitmap, x,0F,null)

    // Returns the new drawable
    return BitmapDrawable(context.resources, newBackBitmap)
}

/**
 * Sets an image with Glide
 */
fun setImage(context: Context, uri: Uri, tag: String,
             view: ImageView, cache: Boolean = true, error: Drawable? = null,
             placeholder: Drawable? = null) {

    var glide = Glide
        .with(context)
        .load(uri)
        .listener(object: RequestListener<Drawable> {

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                // Log the GlideException here (locally or with a remote logging framework):
                Log.e(tag, "Load failed $uri ${e?.message}")
                return false // Allow calling onLoadFailed on the Target.
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                // Log successes here or use DataSource to keep track of cache hits and misses.
                return false // Allow calling onResourceReady on the Target.
            }
        })

    glide = if (!cache)
        glide.signature(ObjectKey(System.currentTimeMillis()))
    else
        glide

    glide = if (error != null)
        glide.error(error)
    else
        glide.error(R.drawable.ko)

    glide = if (placeholder != null)
        glide.placeholder(placeholder)
    else
        glide.placeholder(R.drawable.image)

    glide.into(view)
}

/**
 * Sets an image with Glide
 */
@JvmOverloads
fun setImage(context: Context, uri: Uri, tag: String,
             view: ImageView, error: Drawable? = null,
             placeholder: Drawable? = null) {
    setImage(context, uri, tag, view, true, error, placeholder)
}

/**
 * Get image size and orientation
 */
fun Uri.getImageInfo(context: Context): Pair<Pair<Int, Int>, Int> {
    val inputStream = context.contentResolver.openInputStream(this)!!
    val exif = ExifInterface(inputStream)

    val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
    val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)
    val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
    return Pair(Pair(width, height), orientation)
}

/**
 * Get uri of images from camera function
 */
fun getImageUri(context: Context, inImage: Bitmap, name: String, path: String? = null): Uri {
    var dir = context.getExternalFilesDir(null)

    if (path != null) {
        dir = File(dir, path)

        if (!dir.exists()) dir.mkdirs()
    }

    val tempFile = File( dir, name)

    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
    val bitmapData = bytes.toByteArray()

    val fileOutPut = FileOutputStream(tempFile)
    fileOutPut.write(bitmapData)
    fileOutPut.flush()
    fileOutPut.close()
    return Uri.fromFile(tempFile)
}

/**
 * Concat strings formatted as SpannableStringBuilder
 */
@Suppress("NAME_SHADOWING")
fun spannable(ssBuilder: SpannableStringBuilder, text: String, styleSpan: Int = Typeface.NORMAL, relativeSizeSpan: Float = 1.0f, foregroundColorSpan: String? = null ): SpannableStringBuilder {
    val ssBuilder = ssBuilder
    val start = ssBuilder.length
    val styleSpan = StyleSpan(styleSpan)
    val relativeSizeSpan = RelativeSizeSpan(relativeSizeSpan)

    ssBuilder.append(text)
    ssBuilder.setSpan( styleSpan, start, ssBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    ssBuilder.setSpan( relativeSizeSpan, start, ssBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (foregroundColorSpan != null){
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor(foregroundColorSpan))
        ssBuilder.setSpan( foregroundColorSpan, start, ssBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return ssBuilder
}

/**
 * Concat strings formatted as SpannableStringBuilder
 */
@Suppress("NAME_SHADOWING")
fun spannable(text: String, styleSpan: Int = Typeface.NORMAL, relativeSizeSpan: Float = 1.0f, foregroundColorSpan: String? = null ): SpannableStringBuilder {
    val ssBuilder = SpannableStringBuilder(text)
    val styleSpan = StyleSpan(styleSpan)
    val relativeSizeSpan = RelativeSizeSpan(relativeSizeSpan)

    ssBuilder.setSpan( styleSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    ssBuilder.setSpan( relativeSizeSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    if (foregroundColorSpan != null){
        val foregroundColorSpan = ForegroundColorSpan(Color.parseColor(foregroundColorSpan))
        ssBuilder.setSpan( foregroundColorSpan, 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return ssBuilder
}

/**
 * Sets Snackbar max lines
 */
fun Snackbar.setMaxLines(lines: Int): Snackbar = apply {
    view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = lines
}

/**
 * Click Listener to access app settings
 */
class SettingsListener : View.OnClickListener {

    override fun onClick(v: View) {

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", v.context.packageName, null)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = uri
        v.context.startActivity(intent)
    }
}
