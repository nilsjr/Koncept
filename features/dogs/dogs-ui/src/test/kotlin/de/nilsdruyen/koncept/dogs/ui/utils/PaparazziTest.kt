package de.nilsdruyen.koncept.dogs.ui.utils

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import org.junit.Rule

open class PaparazziTest {

    @get:Rule
    val paparazziRule: Paparazzi = Paparazzi(
        theme = "android:Theme.Material.Light.NoActionBar",
        deviceConfig = DeviceConfig.PIXEL.copy(softButtons = false),
        renderingMode = SessionParams.RenderingMode.V_SCROLL,
        showSystemUi = false,
    )
}
