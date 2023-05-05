package app.sweetappleacres.ui.features.products.adapter

import android.view.ViewGroup
import app.common.base.BindingVH
import app.common.base.ImmutableBindingAdapter
import app.common.extension.load
import app.sweetappleacres.domain.model.ProductDomain
import com.outcode.sweetappleacres.databinding.ItemProductGridBinding
import kotlin.properties.Delegates

class ProductListAdapter(private val onClick: (item: ProductDomain) -> Unit) :
    ImmutableBindingAdapter<ProductDomain, ItemProductGridBinding>() {

    override fun getVB(parent: ViewGroup): ItemProductGridBinding {
        return ItemProductGridBinding.inflate(parent.inflater(), parent, false)
    }

    override var items: List<ProductDomain> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o == n }
    }

    override fun onBindViewHolder(
        holder: BindingVH<ItemProductGridBinding>,
        position: Int
    ) {
        val item = items[position]
        holder.binding.apply {
            tvPrice.text = item.price.toString()
            tvProductName.text = item.name
            ivProductImage.load(item.image)
        }
        holder.binding.root.setOnClickListener {
            onClick.invoke(item)
        }
    }
}