package de.nilsdruyen.koncept.dogs.ui

import android.content.pm.PackageInstaller
import org.junit.Rule
import app.cash.paparazzi.Paparazzi

open class PaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.MaterialComponents.Light.NoActionBar",
        deviceConfig = DeviceConfig.NEXUS_5.copy(softButtons = false, screenHeight = 1),
        renderingMode = PackageInstaller.SessionParams.RenderingMode.V_SCROLL
    )
}