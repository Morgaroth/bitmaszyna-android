package io.github.morgaroth.android.bitmaszyna

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

data class Data(val transactions: List<Transaction>, val state: Ticker, val bids: List<Offer>, val asks: List<Offer>) {
    fun calculateBundle(): () -> Bundle = {
        val b = Bundle()
        b.putParcelableArrayList(BIDS_KEY, ArrayList(bids))
        b.putParcelableArrayList(ASKS_KEY, ArrayList(asks))
        b.putParcelableArrayList(TRANSACTIONS_KEY, ArrayList(transactions))
        b.putParcelable(STATE_KEY, state)
        b
    }

    val bundle = calculateBundle()()

    companion object {
        val BIDS_KEY = "io.github.morgaroth.android.bitmaszyna.Data.BIDS"
        val ASKS_KEY = "io.github.morgaroth.android.bitmaszyna.Data.ASKS"
        val TRANSACTIONS_KEY = "io.github.morgaroth.android.bitmaszyna.Data.TRANSACTIONS"
        val STATE_KEY = "io.github.morgaroth.android.bitmaszyna.Data.STATE"
    }
}

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var data = Data(ArrayList(), Ticker(.0, .0, .0, .0, .0, .0, .0, .0, .0), ArrayList(), ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            doAsync {
                val state = Tasks.getMarketState()
                val transactions = Tasks.getTransactions()
                val offers = Tasks.getASKsAndBIDs()
                uiThread {
                    data = Data(transactions, state, offers.bids, offers.asks)
                }
            }
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)

        toggle.syncState()
        replace(BIDFragment.newInstance(data.bundle))

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)


        val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val intent = Intent("BITMASZYNA_REFRESH_DATA")
        val alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmMgr.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5 * 1000,
                60000, alarmIntent)
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun replace(fragment: Fragment) {
        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.main, fragment)
        tx.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_bid) {
            replace(BIDFragment.newInstance(data.bundle))
        } else if (id == R.id.nav_ask) {
            replace(ASKFragment.newInstance(data.bundle))
        } else if (id == R.id.nav_transactions) {
            replace(TransactionsFragment.newInstance(data.bundle))
//        } else if (id == R.id.nav_camera) {
//
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i("ALARM", "$p0,$p1")
    }

}
