@file:Suppress("NOTHING_TO_INLINE")

package app.common.extension

import android.widget.EditText
import androidx.appcompat.widget.SearchView

inline fun SearchView.setSelection(index: Int) {
    findViewById<EditText>(androidx.appcompat.R.id.search_src_text).setSelection(index)
}

inline fun SearchView.onQueryTextListener(
    returnOnTextSubmit: Boolean = false,
    returnOnTextChange: Boolean = false,
    crossinline onQueryTextSubmit: (query: String) -> Unit = {},
    crossinline onQueryTextChange: (newText: String) -> Unit
) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            onQueryTextSubmit.invoke(query.orEmpty())
            return returnOnTextSubmit
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            onQueryTextChange.invoke(newText.orEmpty())
            return returnOnTextChange
        }
    })
}