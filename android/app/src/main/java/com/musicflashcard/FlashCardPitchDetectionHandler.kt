package com.musicflashcard

import android.util.Log
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

class FlashCardPitchDetectionHandler(reactContext: ReactApplicationContext) : PitchDetectionHandler {
    private val reactContext: ReactApplicationContext

    init {
        this.reactContext = reactContext
    }

    override fun handlePitch(result: PitchDetectionResult, e: AudioEvent?) {
        val pitchInHz: Float = result.pitch

        if (pitchInHz > 0) {
            Log.d("PitchModule", "${result.pitch.toString()} - ${result.probability} + ${result.isPitched}")
        }

        val params : WritableMap = Arguments.createMap()
        params.putDouble("pitch", pitchInHz.toDouble())
        params.putDouble("probability", result.probability.toDouble())

        sendEvent(this.reactContext, "Pitch", params)
    }

    private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }
}