package app.sweetappleacres.ui.features.products

import android.os.Bundle
import android.view.View
import app.common.base.BaseFragment
import app.common.extension.logger
import app.sweetappleacres.data.api.Resource
import com.outcode.sweetappleacres.databinding.FragmentProductListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ProductListFragment : BaseFragment<FragmentProductListBinding>() {
    private val viewModel: ProductsViewModel by sharedViewModel()

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
        loadCachedProducts()
    }

    private fun loadCachedProducts() {
        viewModel.getCachedProducts().observe(viewLifecycleOwner) {
            if (it is Resource.Success) {
                logger(it.data.orEmpty().toString())
            }
        }
    }
}