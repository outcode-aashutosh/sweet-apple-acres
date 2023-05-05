package app.common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import app.common.extension.*
import app.common.utils.getBinding
import com.outcode.sweetappleacres.R

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    private var _binding: B? = null

    lateinit var mContext: Context



    private lateinit var progressDialog: Dialog

    open val pageTitle by lazy { arguments?.getString(KEY_TITLE).orEmpty() }

    val binding: B
        get() = _binding
            ?: throw RuntimeException("Should only use binding after onCreateView and before onDestroyView")


    companion object {
        const val PAGE_LIMIT = 10
        private const val KEY_TITLE = "title"

        fun baseArguments(title: String) = bundleOf(KEY_TITLE to title)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        requireActivity().window?.setWindowAnimations(
            R.style.DialogAnimation
        )
        return super.onCreateAnimation(transit, enter, nextAnim)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().window?.setWindowAnimations(
            R.style.DialogAnimation
        )
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window?.setWindowAnimations(
            R.style.DialogAnimation
        )
        if (pageTitle.isNotEmpty()) activity?.title = pageTitle

        mContext = requireContext()
        progressDialog = ProgressDialogHelper.getProgressDialog(mContext)



    }

    open fun hideProgress() {
        progressDialog.dismissProgress()
    }

    open fun showProgress(message: String = "") {
        progressDialog.showProgress(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun handleError(message:String){
        if (message.equals("Game has already ended.",true)){
            mContext.showPositiveAlert(message = message, onConfirm = {
                requireActivity().finish()
            })
        }else{
            showAlert(message=message)
        }
    }


 /*   open fun onBackPress(onBakPressed: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
    }*/

    fun popFragment() {
        findNavController().popBackStack()
    }

    fun popBackStackFragment() {
        requireActivity().supportFragmentManager.popBackStack()
    }
    /**
     * Pop up to a given destination before navigating. This pops all non-matching destinations
     * from the back stack until this destination is found.
     */
    /* fun popUpToInclusive(@IdRes id: Int): NavOptions? {
         return navOptions { popUpTo(id) { inclusive = true } }
     }*/

}

interface OnSaveItem {
    fun itemSavedListener()
}