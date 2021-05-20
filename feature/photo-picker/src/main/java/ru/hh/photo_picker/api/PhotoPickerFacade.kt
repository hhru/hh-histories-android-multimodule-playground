package ru.hh.photo_picker.api

import ru.hh.di.FeatureFacade
import toothpick.config.Module
import toothpick.ktp.binding.bind


class PhotoPickerFacade : FeatureFacade<PhotoPickerDeps, PhotoPickerApi>(
    depsClass = PhotoPickerDeps::class.java,
    apiClass = PhotoPickerApi::class.java,
    featureScopeName = "PhotoPickerFeature",
    featureScopeModule = {
        Module().apply {
            bind<PhotoPickerApi>().singleton()
        }
    }
)