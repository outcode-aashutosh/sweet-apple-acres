package com.esgonsite.dialogbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.esgonsite.dialogbox.databinding.CustomAlertViewBinding

class CustomAlertViewAdapter(
    private val configModel: ConfigModel,
    private val onItemClick: (
        type: MutableList<OptionModel>, position: Int
    ) -> Unit
) :
    RecyclerView.Adapter<CustomAlertViewAdapter.RecycleViewHolder>() {

    lateinit var binding: CustomAlertViewBinding
    var items: MutableList<OptionModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        binding =
            CustomAlertViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecycleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.itemView.apply {
            binding.btnAction.text = items[position].title
            configModel.options[position].color?.let {
                binding.btnAction.setTextColor(
                    ContextCompat.getColor(
                        context,
                        it
                    )
                )
            }
        configModel.options[position].backgroundDrawable?.let {
            binding.btnAction.background = ContextCompat.getDrawable(context, it)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            configModel.itemStyle?.let { binding.btnAction.setTextAppearance(it) }
        } else {
            configModel.itemStyle?.let { binding.btnAction.setTextAppearance(it) }
        }

        binding.btnAction.setOnClickListener {
            onItemClick.invoke(items, position)
        }
    }

}

class RecycleViewHolder(binding: CustomAlertViewBinding) :
    RecyclerView.ViewHolder(binding.root)
}
