package com.example.pam_project.di

import android.content.Context
import androidx.annotation.VisibleForTesting

object ApplicationContainerLocator {
    private var applicationContainer: ApplicationContainer? = null
    fun locateComponent(context: Context): ApplicationContainer? {
        if (applicationContainer == null) {
            setComponent(ProductionApplicationContainer(context))
        }
        return applicationContainer
    }

    @VisibleForTesting
    fun setComponent(applicationContainer: ApplicationContainer?) {
        ApplicationContainerLocator.applicationContainer = applicationContainer
    }
}