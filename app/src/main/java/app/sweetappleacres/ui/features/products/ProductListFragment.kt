package app.sweetappleacres.ui.features.products

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import app.common.base.BaseFragment
import app.common.extension.logger
import app.sweetappleacres.data.api.Resource
import app.sweetappleacres.domain.model.ProductDomain
import app.sweetappleacres.ui.MainActivity
import app.sweetappleacres.ui.features.products.adapter.ProductListAdapter
import com.outcode.sweetappleacres.databinding.FragmentProductListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ProductListFragment : BaseFragment<FragmentProductListBinding>() {
    private val viewModel: ProductsViewModel by sharedViewModel()
    private lateinit var productListAdapter: ProductListAdapter

    companion object {
        @JvmStatic
        fun newInstance() =
            ProductListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productListAdapter = ProductListAdapter {
            (activity as MainActivity).addFragment(ProductDetailFragment.newInstance(it.id))
        }
        with(binding) {
            rvProductsList.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                adapter = productListAdapter
            }
            etSearch.addTextChangedListener {
                it?.let {
                    val count = it.count()
                    if (count >= 3) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            viewModel.browseProduct(it.toString()).observe(viewLifecycleOwner) {
                                when (it) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        it.data?.let {
                                            productListAdapter.items = it.map {
                                                ProductDomain(
                                                    it.description.orEmpty(),
                                                    it.id,
                                                    it.image.orEmpty(),
                                                    it.isAvailable,
                                                    it.name.orEmpty(),
                                                    it.price,
                                                    it.rating
                                                )
                                            }
                                        }
                                    }
                                    is Resource.Error -> {}
                                }
                            }
                        }, 2000)
                    } else if (count == 0) {
                        loadCachedProducts()
                    }
                }
            }
        }
        loadCachedProducts()
    }

    private fun loadCachedProducts() {
        viewModel.getCachedProducts().observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                logger("Obtained Search Results")
                it.data?.let {
                    productListAdapter.items = it
                }
            }
        }
    }
}