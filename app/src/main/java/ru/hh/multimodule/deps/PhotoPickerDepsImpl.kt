package ru.hh.multimodule.deps

import ru.hh.photo_picker.api.PhotoPickerDeps
import ru.hh.profile.api.ProfileFacade
import toothpick.InjectConstructor

@InjectConstructor
internal class PhotoPickerDepsImpl(
    // Демонстрация того, что циклические зависимости работают
    private val profileFeature: ProfileFacade
) : PhotoPickerDeps