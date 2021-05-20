package ru.hh.multimodule.deps

import ru.hh.di.FeatureFacade
import ru.hh.photo_picker.api.PhotoPickerFacade
import ru.hh.profile.api.ProfileFacade
import toothpick.config.Module


/**
 * Здесь происходит описание связей для склейки фичемодулей
 */
internal class FeatureDepsModule : Module() {

    init {
        bindFeatureDeps(ProfileFacade(), ProfileDepsImpl::class.java)
        bindFeatureDeps(PhotoPickerFacade(), PhotoPickerDepsImpl::class.java)
    }

    private fun <Deps, Api, DepsImpl : Deps> bindFeatureDeps(
        featureFacade: FeatureFacade<Deps, Api>, depsImpl: Class<DepsImpl>
    ) {
        // FeatureFacade -- абстракция над Api + Deps фичемодуля
        // Задаем связь Deps фичемодуля с DepsImpl и предоставляем Api фичемодуля

        bind(featureFacade.depsClass).to(depsImpl).singleton()
        bind(featureFacade.apiClass).toProviderInstance { featureFacade.api }.providesSingleton()
    }

}