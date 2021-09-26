package ru.hh.di

import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module


/**
 * Класс, который обслуживает статичные скоупы feature-модулей (FeatureScopes).
 * Является точкой входа в фичу при межмодульном взаимодействии:
 * 1. Через него происходит безопасное обращение к FeatureScope, который может предоставить наружу реализацию Api
 * feature-модуля.
 * 2. Через него feature-модуль может получить свои Deps.
 *
 * FeatureScopes могут быть открыты в любой момент, как только во внешнем модуле понадобится Api фичи
 * и не закрываются до конца работы приложения.
 *
 * Реализация Внешних зависимостей (Deps) приходит из RootScope (AppScope), который является родительским для
 * FeatureScopes.
 *
 * @param depsClass             Class-объект для интерфейса, описывающего внешние зависимости фичи
 * @param apiClass              Class-объект для интерфейса, описывающего Api фичи
 * @param featureScopeName      Имя DI-скоупа для фичи (FeatureScope)
 * @param featureScopeModule    Функция, возвращающая Toothpick-модуль для FeatureScope
 */
open class FeatureFacade<Deps, Api>(
    val depsClass: Class<Deps>,
    val apiClass: Class<Api>,
    private val featureScopeName: String,
    private val featureScopeModule: (Deps) -> Module,
) {

    /**
     * Отдает FeatureScope. При необходимости открывает его.
     * Свойство предназначено для использования внутри feature-модуля.
     */
    val featureScope: Scope
        get() = if (Toothpick.isScopeOpen(featureScopeName)) {
            Toothpick.openScope(featureScopeName)
        } else {
            val rootScope = Toothpick.openRootScope()
            val deps = rootScope.getInstance(depsClass)
            rootScope.openSubScope(featureScopeName).apply {
                installModules(featureScopeModule(deps))
            }
        }

    /**
     * Отдает Api фичи. При необходимости будет открыт FeatureScope.
     * Свойство предназначенно для использования в app-модуле.
     */
    val api: Api
        get() = featureScope.getInstance(apiClass)

    /**
     * Lazy обёртка над api, чтобы не обращаться в Di каждый раз для получения api
     */
    val lazyApi: Lazy<Api>
        get() = lazy { api }

}
