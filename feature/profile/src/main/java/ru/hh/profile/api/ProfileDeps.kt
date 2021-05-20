package ru.hh.profile.api

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Observable


interface ProfileDeps {

    fun photoPickerFragment(profileId: String): Fragment

    fun photoSelections(profileId: String): Observable<String>

}