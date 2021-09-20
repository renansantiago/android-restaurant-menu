package com.renansantiago.restaurantmenu

import android.app.Application
import com.renansantiago.restaurantmenu.di.appModule
import com.renansantiago.restaurantmenu.di.dataModule
import com.renansantiago.restaurantmenu.di.domainModule
import com.renansantiago.restaurantmenu.di.presentationModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            androidContext = this,
            modules = listOf(
                appModule,
                dataModule,
                domainModule,
                presentationModule
            ),
            loadPropertiesFromFile = true
        )
    }
}