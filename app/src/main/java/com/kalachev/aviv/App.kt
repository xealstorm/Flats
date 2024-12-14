package com.kalachev.aviv

import android.app.Application
import com.kalachev.aviv.di.coilModule
import com.kalachev.aviv.di.dataSourceModule
import com.kalachev.aviv.di.databaseModule
import com.kalachev.aviv.di.networkModule
import com.kalachev.aviv.di.repositoryModule
import com.kalachev.aviv.di.useCaseModule
import com.kalachev.aviv.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoinDi()
    }

    private fun startKoinDi() {
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    coilModule,
                    databaseModule,
                    dataSourceModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                )
            )
        }
    }
}