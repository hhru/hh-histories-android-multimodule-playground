package ru.hh.photo_picker

import toothpick.config.Module
import toothpick.ktp.binding.bind


internal class PhotoPickerModule(photoPickerArgs: PhotoPickerArgs) : Module() {

    init {
        bind<PhotoPickerViewModel>().toClass<PhotoPickerViewModel>().singleton()
        bind<PhotoPickerArgs>().toInstance(photoPickerArgs)
    }

}