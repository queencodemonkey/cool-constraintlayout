package com.randomlytyping.ccl

import android.app.Application
import timber.log.Timber

/**
 * Custom application class for bootstrapping/setup.
 */
class CCLApplication() : Application() {
  override fun onCreate() {
    super.onCreate()

    Timber.plant(Timber.DebugTree())
  }
}