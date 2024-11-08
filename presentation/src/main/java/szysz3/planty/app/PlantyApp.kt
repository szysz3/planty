package szysz3.planty.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import szysz3.planty.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class PlantyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}