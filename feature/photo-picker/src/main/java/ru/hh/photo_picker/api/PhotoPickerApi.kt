package ru.hh.photo_picker.api

import androidx.fragment.app.Fragment
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import ru.hh.photo_picker.PhotoPickerArgs
import ru.hh.photo_picker.PhotoPickerFragment
import ru.hh.photo_picker.PhotoSelection
import toothpick.InjectConstructor


@InjectConstructor
class PhotoPickerApi {

    private val photoSelectionRelay =
        PublishRelay.create<PhotoSelection>()

    fun photoPickerFragment(args: PhotoPickerArgs): Fragment =
        PhotoPickerFragment.newInstance(args)

    fun photoSelections(): Observable<PhotoSelection> =
        photoSelectionRelay.hide()

    internal fun postPhotoSelection(photoSelection: PhotoSelection) =
        photoSelectionRelay.accept(photoSelection)

}