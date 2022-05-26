package com.musicflashcard

import android.util.Log
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.facebook.react.bridge.*
import java.util.*

class PitchModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {
    /**
     *
     * Member Variables
     *
     */
    private val reactContext: ReactApplicationContext

    override fun getName() = "PitchModule"

    @ReactMethod
    fun startRecord(promise: Promise) {
        Log.d("PitchModule", "Started recording")

        startPitchDetection()
        promise.resolve(1)
    }


    private fun startPitchDetection() {
        val dispatcher: AudioDispatcher =
            AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

        val pdh: PitchDetectionHandler = FlashCardPitchDetectionHandler(this.reactContext)
        val p: AudioProcessor = PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
            22050F, 1024, pdh)
        dispatcher.addAudioProcessor(p)
        Thread(dispatcher as Runnable, "Audio Dispatcher").start()
    }

    @ReactMethod
    fun addListener(eventName: String) {
        // Set up any upstream listeners or background tasks as necessary
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        // Remove upstream listeners, stop unnecessary background tasks
    }

    /**
     *
     * Constructor
     *
     *
     *
     * @param reactContext ReactApplicationContext
     */
    init {
        this.reactContext = reactContext
    }
}