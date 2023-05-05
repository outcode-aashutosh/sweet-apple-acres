package app.sweetappleacres.ui.features.products

import android.os.Bundle
import android.view.View
import app.common.base.BaseFragment
import app.common.extension.load
import app.sweetappleacres.data.api.Resource
import com.outcode.sweetappleacres.databinding.FragmentProductDetailBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {
    private val viewModel: ProductsViewModel by sharedViewModel()
    private var productId: String? = null

    companion object {
        @JvmStatic
        fun newInstance(productId: String) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_ID, productId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener {
                popBackStackFragment()
            }
        }
        loadCachedProductDetail()
    }

    private fun loadCachedProductDetail() {
        viewModel.getCachedProductDetail(productId!!).observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                it.data?.let {
                    with(binding) {
                        toolbar.title = it.name
                        tvDescription.text = it.description
                        ratings.rating = it.rating.toFloat()
                        tvPrice.text = it.price.toString()
                        ivProductImage.load(it.image)
                    }
                }
            }
        }
    }
}