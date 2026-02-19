package com.rigobertods.rdscore

import android.app.Application
import android.content.Context
import com.rigobertods.rdscore.core.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RDScoreApplication : Application() {
    
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }
}
