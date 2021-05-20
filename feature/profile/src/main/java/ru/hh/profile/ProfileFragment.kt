package ru.hh.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import ru.hh.android_utils.args
import ru.hh.android_utils.bind
import ru.hh.android_utils.withArgs
import ru.hh.di.DiFragmentPlugin
import ru.hh.profile.api.ProfileDeps
import ru.hh.profile.api.ProfileFacade


internal class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        fun newInstance(userProfile: UserProfile) = ProfileFragment().withArgs(userProfile)
    }

    private val userProfile by args<UserProfile>()

    private val di = DiFragmentPlugin(
        fragment = this,
        parentScope = { ProfileFacade().featureScope },
        scopeNameSuffix = { userProfile.id },
        scopeModules = { arrayOf(ProfileScreenModule(userProfile)) }
    )

    private val viewModel by lazy { di.get<ProfileViewModel>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photoView = view.findViewById<ImageView>(R.id.profile_photo)
        val nameView = view.findViewById<TextView>(R.id.profile_name)
        val changePhotoButton = view.findViewById<Button>(R.id.change_profile_photo)

        bind(viewModel.userProfile) { userProfile ->
            photoView.load(userProfile.photoUrl)
            nameView.text = userProfile.name
        }

        changePhotoButton.setOnClickListener {
            val fragment = di.scope.getInstance(ProfileDeps::class.java).photoPickerFragment(userProfile.id)
            parentFragmentManager
                .beginTransaction()
                .add(fragment, "PhotoPicker")
                .commit()
        }
    }

}