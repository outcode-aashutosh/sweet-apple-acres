package com.esgonsite.dialogbox

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.esgonsite.dialogbox.databinding.AlertBoxViewBinding
import java.util.*


fun showCustomDialog(
    context: Context, configModel: ConfigModel
) {
    lateinit var customAlertViewAdapter: CustomAlertViewAdapter
    val bindingAlert = AlertBoxViewBinding.inflate(LayoutInflater.from(context))
    val builder = AlertDialog.Builder(context)
        .setView(bindingAlert.root)

    val dialog = builder.create()
    bindingAlert.clAlertDialog.animateView(400, 0, R.anim.anim_zoom_in)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(configModel.isCancelable)
    dialog.show()

    bindingAlert.tvTitle.text = configModel.title
    bindingAlert.tvMessage.text = configModel.message
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        configModel.titleStyle?.let { bindingAlert.tvTitle.setTextAppearance(it) }
        configModel.messageStyle?.let { bindingAlert.tvMessage.setTextAppearance(it) }
    } else {
        configModel.titleStyle?.let { bindingAlert.tvTitle.setTextAppearance(context, it) }
        configModel.messageStyle?.let { bindingAlert.tvMessage.setTextAppearance(context, it) }
    }

    customAlertViewAdapter = CustomAlertViewAdapter(
        configModel,
        onItemClick = { it, position ->
            bindingAlert.clAlertDialog.animateView(300, 100, R.anim.anim_zoom_out)
            dialog.dismiss()
            configModel.options[position].onItemClick.invoke()
        })

    val layoutManagerGrid =
        GridLayoutManager(context, 2)
    layoutManagerGrid.spanSizeLookup =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if ((configModel.options.size) % 2 != 0 && position == (configModel.options.size - 1)) {
                    2
                } else {
                    1
                }
            }
        }
    bindingAlert.rvActionView.apply {
        layoutManager = if (configModel.isOneLineButton) {
            LinearLayoutManager(context)
        } else {
            if ((configModel.options.size) >= 3) {
                LinearLayoutManager(context)
            } else {
                layoutManagerGrid
            }

        }
        adapter = customAlertViewAdapter
    }

    customAlertViewAdapter.items = configModel.options.toMutableList()
}