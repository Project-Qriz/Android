package com.qriz.app.core.ui.test.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.SelectedAndIncorrectOptionItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem

fun Option.toSelectedOrCorrectOptionItem() =
    SelectedOrCorrectOptionItem(description)

fun Option.toSelectedAndIncorrectOptionItem() =
    SelectedAndIncorrectOptionItem(description)

fun Option.toGeneralOptionItem() =
    GeneralOptionItem(description)

fun OptionItem.toOption() =
    Option(description)
