package app.sweetappleacres.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.common.base.BaseActivity
import app.common.extension.BACK_STACK
import app.common.extension.orUnknownError
import app.common.extension.showAlert
import app.sweetappleacres.data.api.Resource
import app.sweetappleacres.ui.features.products.ProductListFragment
import app.sweetappleacres.ui.features.products.ProductsViewModel
import com.outcode.sweetappleacres.R
import com.outcode.sweetappleacres.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val productsViewModel: ProductsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        setUpFragment(ProductListFragment.newInstance())
        cacheAllProducts()
    }

    private fun cacheAllProducts() {
        productsViewModel.loadAllProducts().observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showProgress(message = "Loading Products")
                }
                is Resource.Success -> {
                    hideProgress()
                }
                is Resource.Error -> {
                    hideProgress()
                    showAlert(message = it.message.orUnknownError())
                }
            }
        }
    }

    private fun setUpFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.nav_host_fragment, fragment)
            .addToBackStack(BACK_STACK)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment, showAnim: Boolean = true) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        if (showAnim) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_slide_up,
                    R.anim.exit_slide_down,
                    R.anim.enter_slide_up,
                    R.anim.exit_slide_down
                )
                .add(R.id.nav_host_fragment, fragment)
                .addToBackStack(BACK_STACK)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, fragment)
                .addToBackStack(BACK_STACK)
                .commit()
        }
    }
}