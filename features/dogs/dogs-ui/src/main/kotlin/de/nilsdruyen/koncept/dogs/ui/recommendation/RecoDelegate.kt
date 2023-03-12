package de.nilsdruyen.koncept.dogs.ui.recommendation

import kotlinx.coroutines.flow.StateFlow

interface RecoDelegate {

    fun recoState(): StateFlow<RecoState>
    fun action(intent: RecoIntent)
}