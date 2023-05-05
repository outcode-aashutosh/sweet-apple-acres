package app.common.extension

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

fun <T> Spinner.load(context: Context, items: List<T>, item: T? = null) {
    adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items).apply {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    if (items.isNotEmpty() && item != null) setSelection(items.indexOf(item))
}

inline fun Spinner.onItemSelected(
    crossinline itemSelected: (
        parent: AdapterView<*>,
        view: View,
        position: Int,
        id: Long
    ) -> Unit
) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            itemSelected.invoke(parent, view, position, id)
        }
    }
}