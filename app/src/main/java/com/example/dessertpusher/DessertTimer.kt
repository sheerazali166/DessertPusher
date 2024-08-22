package com.example.dessertpusher

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import timber.log.Timber

class DessertTimer(lifecycle: Lifecycle): LifecycleObserver {

    // TODO: Number of seconds counted since the timer started
    var secondsCount = 0

    // TODO: Handle is class meant to process a queue of messages (known as [android.os.Message]s)
    // TODO: or actions (known as [Runnable]s)
    private var handler = Handler()
    private lateinit var runnable: Runnable

    init {
        // TODO: Add this as a lifecycle observer, which allows for the class to react to changes in this
        // TODO: activity's lifecycle state
        lifecycle.addObserver(this)
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//    fun dummyMethod() {
//        Timber.i("I was called")
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startTimer() {

        // TODO: Create the runnable action, whitch prints out a long and increaments the seconds counter
        runnable = Runnable {

            secondsCount++
            Timber.i("Timer is at: $secondsCount")

            // TODO: postDelayed re-adds the action to the queue of actions the handler is cycling
            // TODO: through. the delayMillis param tells the handler to run the runnable in
            // TODO: 1 second (1000ms)
            handler.postDelayed(runnable, 1000)
        }

        // TODO: This is what initially starts the timer
        handler.postDelayed(runnable, 1000)

        // TODO: Note that the thread the handler runs on is determined by a class called looper.
        // TODO: In this case, no looper is defined, and it defaults to the main or UI thread
    }

    // TODO: @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTimer() {

        // TODO: Removes all pending posts of runnable from the handler's queue, effectively stopping the
        // TODO: timer
        handler.removeCallbacks(runnable)

    }

}