package ru.hh.multimodule.deps

import ru.hh.di.FeatureFacade
import ru.hh.photo_picker.api.PhotoPickerFacade
import ru.hh.profile.api.ProfileFacade
import toothpick.config.Module
import toothpick.ktp.binding.bind
import kotlin.reflect.KClass


/**
 * Здесь происходит описание связей для склейки фичемодулей
 */
internal class FeatureDepsModule : Module() {

    init {
        bindFeatureDeps(ProfileFacade(), ProfileDepsImpl::class)
        bindFeatureDeps(PhotoPickerFacade(), PhotoPickerDepsImpl::class)
    }

    private inline fun <reified Deps : Any, Api : Any, reified DepsImpl : Deps, reified TFeatureFacade : FeatureFacade<Deps, Api>> bindFeatureDeps(
        featureFacade: TFeatureFacade,
        depsImpl: KClass<DepsImpl>
    ) {
        // FeatureFacade -- абстракция над Api + Deps фичемодуля
        // Задаем связь Deps фичемодуля с DepsImpl и предоставляем Api фичемодуля

        bind<Deps>().toClass(depsImpl).singleton()
        bind<TFeatureFacade>().toInstance(featureFacade)
    }

}