package com.qriz.app.feature.sign.findId

data class DialogState(
    val title: String,
    val message: String,
) {
    val shouldShow: Boolean = title.isNotEmpty()

    companion object {
        val EMPTY = DialogState(
            title = "",
            message = "",
        )
    }
}
