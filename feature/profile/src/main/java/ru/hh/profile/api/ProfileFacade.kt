package ru.hh.profile.api

import ru.hh.di.FeatureFacade
import toothpick.config.Module
import toothpick.ktp.binding.bind


class ProfileFacade : FeatureFacade<ProfileDeps, ProfileApi>(
    depsClass = ProfileDeps::class.java,
    apiClass = ProfileApi::class.java,
    featureScopeName = "ProfileFeature",
    featureScopeModule = {
        Module().apply {
            bind<ProfileApi>().singleton().releasable()
        }
    }
)