package eu.manuelgc.baradmin.utils

import android.text.Editable
import android.text.TextWatcher

class TextWatcherUtil(): TextWatcher {
    var _ignore = false // indicates if the change was made by the TextWatcher itself.
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }

    override fun afterTextChanged(p0: Editable?) {
        if (_ignore)
            return
        _ignore = true

    }

    companion object {
        private const val IS_INTEGER = 1
        private const val IS_NOT_FINAL = 2
        private const val IS_NOT_FINAL2 = 4
        fun is_included_in(option: Int, allOptions: Int): Boolean {
            var num = allOptions
            var i = 1
            val values = mutableListOf<Int>()

            while (2 * i < num) {
                i *= 2
            }

            do {
                if (i <= num) {
                    values.add(i)
                    num -= i
                }
                i /= 2
            } while (num > 0)

            return values.indexOf(option) >= 0
        }
    }
}