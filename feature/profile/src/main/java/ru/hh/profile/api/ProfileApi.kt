package ru.hh.profile.api

import androidx.fragment.app.Fragment
import ru.hh.profile.ProfileFragment
import ru.hh.profile.UserProfile
import toothpick.InjectConstructor


@InjectConstructor
class ProfileApi {

    fun profileFragment(userProfile: UserProfile): Fragment =
        ProfileFragment.newInstance(userProfile)

}