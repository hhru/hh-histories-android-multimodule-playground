package ru.hh.multimodule

import android.app.Application
import ru.hh.multimodule.deps.FeatureDepsModule
import toothpick.Toothpick
import toothpick.configuration.Configuration


internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTp()
    }

    private fun initTp() {
        val tpConfig = Configuration.forDevelopment().preventMultipleRootScopes()
        Toothpick.setConfiguration(tpConfig)

        // Используем rootScope Toothpick-а в качестве AppScope
        // и устанавливаем туда зависимости для фичемодулей
        Toothpick.openRootScope()
            .installModules(FeatureDepsModule())
    }

}