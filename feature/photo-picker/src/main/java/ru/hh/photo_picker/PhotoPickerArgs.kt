package ru.hh.photo_picker

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PhotoPickerArgs(
    val selectionId: String
) : Parcelable