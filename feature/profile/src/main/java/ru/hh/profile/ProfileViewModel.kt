package ru.hh.profile

import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.hh.profile.api.ProfileDeps
import toothpick.InjectConstructor


@InjectConstructor
internal class ProfileViewModel(
    private val initialUserProfile: UserProfile,
    private val deps: ProfileDeps,
    private val disposable: CompositeDisposable
) {

    private val _userProfileRelay = BehaviorRelay.create<UserProfile>()
    val userProfile: Observable<UserProfile> = _userProfileRelay.hide().distinctUntilChanged()

    init {
        with(disposable) {
            deps.photoSelections(initialUserProfile.id)
                .scan(initialUserProfile) { userProfile, newPhotoUrl ->
                    userProfile.copy(photoUrl = newPhotoUrl)
                }
                .subscribe(_userProfileRelay)
                .apply(::add)
        }
    }

}