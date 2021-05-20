package ru.hh.android_utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

// Экстеншены для биндинга rx-cтримов (например, из ViewModel) к жизненному циклу View фрагмента

fun <T> Fragment.bind(obs: Observable<T>, binding: (T) -> Unit) {
    val disposable = CompositeDisposable()
    val observer = DisposableLifecycleObserver(disposable)
    viewLifecycleOwner.lifecycle.addObserver(observer)
    obs.subscribe(binding).apply(disposable::add)
}

internal class DisposableLifecycleObserver(
    private val disposable: Disposable
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        disposable.dispose()
    }

}