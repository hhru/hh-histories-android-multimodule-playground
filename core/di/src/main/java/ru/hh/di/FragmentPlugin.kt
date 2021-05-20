package ru.hh.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind


/**
 * Создает и поддерживает Toothpick-скоуп, который автоматически связан с жизненным циклом фрагмента
 *
 * @param fragment      Фрагмент, жизненный цикл которого используется для скоупа
 * @param parentScope   Предоставляет родительский скоуп (этом может быть FeatureScope или скоуп родительского фрагмента)
 * @param scopeNameSuffix   Дополнительный суффикс для имени скоупа при открытии одинаковых фрагментов
 * @param scopeModules  Список Toothpick-модулей, которые будут предоставлены для скоупа фрагмента
 */
class DiFragmentPlugin(
    private val fragment: Fragment,
    private val parentScope: () -> Scope,
    private val scopeNameSuffix: () -> String = { "" },
    private val scopeModules: () -> Array<Module> = { emptyArray() }
) : LifecycleObserver {

    private val scopeName by lazy { "${fragment.javaClass.name}_${scopeNameSuffix()}" }

    /**
     * Для каждого скоупа фрагмента создаем Disposable, который будет закрыт при уничтожении скоупа.
     */
    private val disposable = CompositeDisposable()

    init {
        fragment.lifecycle.addObserver(this)
    }

    val scope: Scope by lazy {
        if (!Toothpick.isScopeOpen(scopeName)) {
            parentScope().openSubScope(scopeName).apply {
                installModules(*scopeModules(), object : Module() {
                    init {
                        bind<CompositeDisposable>().toInstance(disposable)
                    }
                })
            }
        } else {
            Toothpick.openScope(scopeName)
        }
    }

    inline fun <reified T> get(): T = scope.getInstance(T::class.java)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (!fragment.requireActivity().isChangingConfigurations) {
            disposable.dispose()
            Toothpick.closeScope(scopeName)
        }
    }

}
