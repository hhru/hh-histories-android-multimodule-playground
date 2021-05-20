package ru.hh.multimodule

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.hh.di.DiFragmentPlugin
import ru.hh.profile.UserProfile
import ru.hh.profile.api.ProfileApi
import ru.hh.profile.api.ProfileFacade
import toothpick.Toothpick


internal class RootFragment : Fragment(R.layout.fragment_root) {

    private val di = DiFragmentPlugin(
        fragment = this,
        parentScope = { Toothpick.openRootScope() }
    )

    private fun getProfileFragment(userProfile: UserProfile): Fragment {
        return di.get<ProfileApi>().profileFragment(userProfile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            attachFragment("profile1") {
                getProfileFragment(UserProfile.predefined1)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.profile1 -> attachFragment("profile1") {
                    getProfileFragment(UserProfile.predefined1)
                }
                R.id.profile2 -> attachFragment("profile2") {
                    getProfileFragment(UserProfile.predefined2)
                }
            }
            true
        }
    }

    private fun attachFragment(tag: String, createFragment: () -> Fragment) {
        childFragmentManager.apply {
            val currentFragment = findFragmentById(R.id.fragment_container)
            val fragmentToAttach = findFragmentByTag(tag)

           beginTransaction()
                .apply {
                    if (currentFragment != null) {
                        detach(currentFragment)
                    }
                    if (fragmentToAttach == null) {
                        add(R.id.fragment_container, createFragment(), tag)
                    }
                    else {
                        attach(fragmentToAttach)
                    }
                }
                .commit()
        }
    }

}