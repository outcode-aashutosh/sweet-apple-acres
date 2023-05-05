package app.common.extension

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.outcode.sweetappleacres.R

object ProgressDialogHelper {

    fun getProgressDialog(context: Context): Dialog {
        return Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }
}

@SuppressLint("InflateParams")
fun Dialog.showProgress(message: String) {
    if (!isShowing) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_progress_dialog, null)
        if (message.isBlank()){
            view.findViewById<TextView>(R.id.tv_message).gone()

        }else{
            view.findViewById<TextView>(R.id.tv_message).text = message

        }
        setContentView(view)
        show()
    }
}

fun Dialog.dismissProgress() {
    if (isShowing) {
        dismiss()
    }
}