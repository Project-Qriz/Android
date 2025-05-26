package com.qriz.app.core.ui.test.mapper

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.ui.test.model.GeneralOptionItem
import com.qriz.app.core.ui.test.model.OptionItem
import com.qriz.app.core.ui.test.model.SelectedAndIncorrectOptionItem
import com.qriz.app.core.ui.test.model.SelectedOrCorrectOptionItem

fun Option.toSelectedOrCorrectOptionItem() =
    SelectedOrCorrectOptionItem(id, content)

fun Option.toSelectedAndIncorrectOptionItem() =
    SelectedAndIncorrectOptionItem(id, content)

fun Option.toGeneralOptionItem() =
    GeneralOptionItem(id, content)

fun OptionItem.toOption() =
    Option(id, description)
