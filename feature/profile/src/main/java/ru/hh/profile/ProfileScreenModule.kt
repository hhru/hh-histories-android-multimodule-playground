package ru.hh.profile

import toothpick.config.Module
import toothpick.ktp.binding.bind


internal class ProfileScreenModule(initialUserProfile: UserProfile) : Module() {

    init {
        bind<ProfileViewModel>().singleton()
        bind<UserProfile>().toInstance(initialUserProfile)
    }

}