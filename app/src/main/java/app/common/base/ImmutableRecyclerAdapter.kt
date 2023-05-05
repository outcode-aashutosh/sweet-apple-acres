package app.common.base

import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.common.extension.logException

abstract class ImmutableBindingAdapter<ITEM, VB> : RecyclerView.Adapter<BindingVH<VB>>(),
    ImmutableAutoUpdatableAdapter {

    abstract var items: List<ITEM>

    abstract fun getVB(parent: ViewGroup): VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingVH<VB> {
        return BindingVH(getVB(parent))
    }

    fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(context)

    fun addItem( newItems: ITEM) {
        val list = items.toMutableList()
        list.add(list.size,newItems)
        items = list
    }

    fun addItemStart( newItems: ITEM) {
        val list = items.toMutableList()
        list.add(0, newItems)
        items = list
    }

    fun addItems(newItems: List<ITEM>) {
        val list = items.toMutableList()
        list.addAll(newItems)
        items = list
    }

    fun remove(item: ITEM) {
        val list = items.toMutableList()
        try {
            list.remove(item)
            items = list
        } catch (e: Exception) {
            e.logException()
        }
    }

    fun removeAll() {
        val list = items.toMutableList()
        try {
            list.clear()
            items = list
        } catch (e: Exception) {
            e.logException()
        }
    }

    fun updateItemAt(item: ITEM, position: Int) {
        val list = items.toMutableList()
        list[position] = item
        items = list
        notifyItemChanged(position)
    }

    fun removeAt(position: Int) {
        val list = items.toMutableList()
        list.removeAt(position)
        items = list
    }

    override fun getItemCount(): Int = items.size
}

open class BindingVH<VB>(val binding: VB) : RecyclerView.ViewHolder((binding as ViewBinding).root)

interface ImmutableAutoUpdatableAdapter {
    fun <T> RecyclerView.Adapter<*>.autoNotify(
        old: List<T>,
        new: List<T>,
        compare: (T, T) -> Boolean,
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(old[oldItemPosition], new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return old[oldItemPosition] == new[newItemPosition]
            }

            override fun getOldListSize() = old.size

            override fun getNewListSize() = new.size
        })
        diff.dispatchUpdatesTo(this)
    }
}

class RecycleViewHolder(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root)
