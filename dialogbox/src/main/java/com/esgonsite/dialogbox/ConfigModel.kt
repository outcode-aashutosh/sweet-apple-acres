package com.esgonsite.dialogbox

data class ConfigModel(
    var title: String? = null,
    var showTitle: Boolean? = false,
    var titleStyle: Int? = null,
    var messageStyle: Int? = null,
    var itemStyle: Int? = null,
    var isCancelable: Boolean = false,
    val message: String,
    val options: List<OptionModel>,
    val isOneLineButton: Boolean = false
)

data class OptionModel(
    var title: String,
    var backgroundDrawable: Int? = null,
    var color: Int? = null,
    var onItemClick: () -> Unit
)
