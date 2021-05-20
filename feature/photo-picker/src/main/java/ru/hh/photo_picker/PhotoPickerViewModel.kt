package ru.hh.photo_picker

import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable
import ru.hh.photo_picker.api.PhotoPickerApi
import toothpick.InjectConstructor


@InjectConstructor
internal class PhotoPickerViewModel(
    private val photoPickerApi: PhotoPickerApi,
    private val photoPickerArgs: PhotoPickerArgs
) {

    private val _closeFragmentRelay = PublishRelay.create<Unit>()
    val closeFragment: Observable<Unit> = _closeFragmentRelay

    fun onPhotoSelected(photo: Photo) {
        photoPickerApi.postPhotoSelection(
            PhotoSelection(
                selectionId = photoPickerArgs.selectionId,
                photoUrl = photo.url
            )
        )
        _closeFragmentRelay.accept(Unit)
    }

}