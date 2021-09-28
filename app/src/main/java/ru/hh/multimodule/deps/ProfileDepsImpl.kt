package ru.hh.multimodule.deps

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Observable
import ru.hh.photo_picker.PhotoPickerArgs
import ru.hh.photo_picker.api.PhotoPickerFacade
import ru.hh.profile.api.ProfileDeps
import toothpick.InjectConstructor

@InjectConstructor
internal class ProfileDepsImpl(
    // для реализации зависимостей feature-модуля могут понадобиться зависимости из другой фичи
    // для этого инжектим feature-фасад вместо прямого инжекта Api, чтобы избежать циклических зависимостей
    private val photoPicker: PhotoPickerFacade
) : ProfileDeps {

    override fun photoPickerFragment(profileId: String): Fragment =
        photoPicker.api.photoPickerFragment(PhotoPickerArgs((profileId)))

    override fun photoSelections(profileId: String): Observable<String> =
        photoPicker.api.photoSelections()
            .filter { it.selectionId == profileId }
            .map { it.photo.url }

}