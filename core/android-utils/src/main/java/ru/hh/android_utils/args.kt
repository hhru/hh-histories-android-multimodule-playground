package ru.hh.android_utils

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

// Полезности для работы с arguments фрагмента

inline fun <reified T> Fragment.args(): Lazy<T> = lazy {
    requireArguments().getParcelable("args")!!
}

inline fun <reified T : Parcelable> Fragment.withArgs(args: T): Fragment {
    return this.apply {
        arguments = Bundle().apply {
            putParcelable("args", args)
        }
    }
}
