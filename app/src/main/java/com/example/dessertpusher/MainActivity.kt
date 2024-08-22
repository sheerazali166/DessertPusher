package com.example.dessertpusher

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.example.dessertpusher.databinding.ActivityMainBinding
import timber.log.Timber

// TODO: onSaveInstanceState bundle keys
const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"
const val KEY_TIMER_SECONDS = "timer_seconds_key"

class MainActivity : AppCompatActivity(), LifecycleObserver {

    private var revenue = 0
    private var dessertsSold = 0
    private lateinit var dessertTimer: DessertTimer

    // TODO: Call all the views
    private lateinit var binding: ActivityMainBinding


    // TODO: DESSERT DATA

    // TODO: Simple data class that represents a dessert. includes the resource id integer associated with
    // TODO: the image, the price it's sold for, and the startProductionAmount, whitch determines when
    // TODO: the dessert starts to be produced.
    data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)

    // TODO: Creates a list of all desserts, in order of when they start being produced
    private val allDesserts = listOf(
        Dessert(R.drawable.cupcake, 5, 0),
        Dessert(R.drawable.donut, 10, 5),
        Dessert(R.drawable.eclair, 15, 20),
        Dessert(R.drawable.froyo, 30, 50),
        Dessert(R.drawable.gingerbread, 50, 100),
        Dessert(R.drawable.honeycomb, 100, 200),
        Dessert(R.drawable.icecreamsandwich, 500, 500),
        Dessert(R.drawable.jellybean, 1000, 1000),
        Dessert(R.drawable.kitkat, 2000, 2000),
        Dessert(R.drawable.lollipop, 3000, 4000),
        Dessert(R.drawable.marshmallow, 4000, 4000),
        Dessert(R.drawable.nougat, 5000, 16000),
        Dessert(R.drawable.oreo, 6000, 20000))


    private var currentDessert = allDesserts[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: setContentView(R.layout.activity_main)

        Timber.i("onCreate Called");

        // TODO: Use data binding to call reference to the views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }

        // TODO: Setup dessertTimer, passing in the lifecycle
        // dessertTimer = DessertTimer(this.lifecycle)
        dessertTimer = DessertTimer(this.lifecycle)

        // TODO: If there is a saveInstanceState bundle, then you're "restarting" the activity
        // TODO: if there isn't a bundle, then it's a "fresh" start
        if (savedInstanceState != null) {

            // TODO: Get all the game state information from the bundle, set it
            revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
            dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
            dessertTimer.secondsCount = savedInstanceState.getInt(KEY_TIMER_SECONDS, 0)

            showCurrentDessert()

        }

        // TODO: Set the textViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // TODO: Make sure the correct dessert is shwing
        binding.dessertButton.setImageResource(currentDessert.imageId)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // TODO: Updates the score when the dessert is clicked. possibly shows a new dessert.
    private fun onDessertClicked() {

        // TODO: Update the score
        revenue += currentDessert.price
        dessertsSold++

        binding.revenue = revenue
        binding.amountSold = dessertsSold

        // TODO: Show the next dessert
        showCurrentDessert()

    }

    // TODO: Determines whitch desserts to show.
    private fun showCurrentDessert() {

        var newDessert = allDesserts[0]

        for (dessert in allDesserts) {

            if (dessertsSold >= dessert.startProductionAmount) {
                newDessert = dessert
            }
            // TODO: The list of desserts is sorted by startProductionAmount. as you sell more desserts,
            // TODO: You'll start producing more expensive desserts are determined by startProductionAmount
            // TODO: We all know to break as soon as we see a desserts who's "startProductionAmount" is greater
            // TODO: than the amount sold.
            else break

        }

        // TODO: If the new dessert is actually different than the current dessert, update the image
        if (newDessert != currentDessert) {
            currentDessert = newDessert
            binding.dessertButton.setImageResource(newDessert.imageId)
        }
    }

    // TODO: Menu methods
    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(getString(R.string.share_text, dessertsSold, revenue))
            .setText("text/plain")
            .intent

        try {
            startActivity(shareIntent)

        } catch (activityNotFoundException: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.sharing_not_available), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.shareMenuButton -> onShare()
        }

        return super.onOptionsItemSelected(menuItem)
    }

    // TODO: Called when the user navigates away from the app but might come back
    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt(KEY_REVENUE, revenue)
        outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
        outState.putInt(KEY_TIMER_SECONDS, dessertTimer.secondsCount)

        Timber.i("onSaveInstanceState Called")

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState Called")
    }

    // TODO: Lifecycle Methods
    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }

}