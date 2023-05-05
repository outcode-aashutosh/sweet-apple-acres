package app.common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import app.common.extension.ProgressDialogHelper
import app.common.extension.dismissProgress
import app.common.extension.showProgress
import app.common.utils.getBinding
import com.outcode.sweetappleacres.R

abstract class BaseDialogFragment<B : ViewBinding> : DialogFragment() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (pageTitle.isNotEmpty()) activity?.title = pageTitle

        requireDialog().window?.setWindowAnimations(
            R.style.DialogAnimation
        )
        mContext = requireContext()
        progressDialog = ProgressDialogHelper.getProgressDialog(mContext)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

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


/*
    fun popBackStack(){
        findNavController().popBackStack()
    }*/


    /* fun popUpToInclusive(@IdRes id: Int): NavOptions? {
         return navOptions { popUpTo(id) { inclusive = true } }
     }*/


}
