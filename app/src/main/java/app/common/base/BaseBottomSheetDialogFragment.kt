package app.common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import app.common.extension.ProgressDialogHelper
import app.common.extension.dismissProgress
import app.common.extension.showProgress
import app.common.utils.getBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.outcode.sweetappleacres.R

abstract class BaseBottomSheetDialogFragment<B : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: B? = null

    lateinit var mContext: Context

    private lateinit var progressDialog: Dialog

    val binding: B
        get() = _binding
            ?: throw RuntimeException("Should only use binding after onCreateView and before onDestroyView")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window?.setWindowAnimations(
            R.style.DialogAnimation
        )
        mContext = requireContext()
        progressDialog = ProgressDialogHelper.getProgressDialog(mContext)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

}
