package app.sweetappleacres.ui.features.products

import android.os.Bundle
import app.common.base.BaseFragment
import com.outcode.sweetappleacres.databinding.FragmentProductDetailBinding

class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductDetailFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}