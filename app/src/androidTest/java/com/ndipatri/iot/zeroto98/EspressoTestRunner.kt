package com.ndipatri.iot.zeroto98

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.github.tmurakami.dexopener.DexOpener

// DO NOT RELOCATE THIS CLASS
// DexOpener needs this class to be in your root package as indicated
// by your AndroidManifest.xml

class EspressoTestRunner : AndroidJUnitRunner() {
    @Throws(
        ClassNotFoundException::class,
        IllegalAccessException::class,
        InstantiationException::class
    )
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        DexOpener.install(this) // Call me first!
        return super.newApplication(cl, className, context)
    }
}