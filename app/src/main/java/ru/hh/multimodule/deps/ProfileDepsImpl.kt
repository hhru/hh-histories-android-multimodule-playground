package ru.hh.multimodule.deps

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Observable
import ru.hh.photo_picker.PhotoPickerArgs
import ru.hh.photo_picker.api.PhotoPickerApi
import ru.hh.profile.api.ProfileDeps
import toothpick.InjectConstructor


@InjectConstructor
internal class ProfileDepsImpl(
    // для реализации зависимостей фичемодуля, может понадобиться Api другого фичемодуля
    private val photoPickerApi: PhotoPickerApi
) : ProfileDeps {

    override fun photoPickerFragment(profileId: String): Fragment =
        photoPickerApi.photoPickerFragment(PhotoPickerArgs((profileId)))

    override fun photoSelections(profileId: String): Observable<String> =
        photoPickerApi.photoSelections()
            .filter { it.selectionId == profileId }
            .map { it.photoUrl }

}