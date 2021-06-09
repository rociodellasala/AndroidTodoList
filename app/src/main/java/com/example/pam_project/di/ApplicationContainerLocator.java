package com.example.pam_project.di;

import android.content.Context;

import androidx.annotation.VisibleForTesting;

public class ApplicationContainerLocator {

    private static ApplicationContainer applicationContainer;

    private ApplicationContainerLocator() {}

    public static ApplicationContainer locateComponent(final Context context) {
        if (applicationContainer == null) {
            setComponent(new ProductionApplicationContainer(context));
        }

        return applicationContainer;
    }

    @VisibleForTesting
    public static void setComponent(final ApplicationContainer applicationContainer) {
        ApplicationContainerLocator.applicationContainer = applicationContainer;
    }
}
