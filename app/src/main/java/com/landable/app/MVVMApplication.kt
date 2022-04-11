package com.landable.app

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class MVVMApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        // bind() from singleton { MyApi() }
        bind() from singleton { /*RegisterRepository()*/ }
    }
}