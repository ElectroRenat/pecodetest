package my.app.pecodetest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2

val fragmentList: MutableList<MainFragment> = mutableListOf()

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewPager: ViewPager2
    private lateinit var mainPreferences: MainPreferences
    private lateinit var fragmentCounterText: TextView
    private lateinit var addFragmentButton: ImageButton
    private lateinit var removeFragmentButton: ImageButton
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPreferences = MainPreferences(this)

        fragmentCounterText = findViewById(R.id.fragment_counter_text)
        addFragmentButton = findViewById(R.id.add_fragment_button )
        removeFragmentButton = findViewById(R.id.remove_fragment_button)

        val fragmentsCount = mainPreferences.getFragmentsCount()
        if (fragmentsCount != 0) initFragments(fragmentsCount)

        val adapter = FragmentAdapter(fragmentList, supportFragmentManager, lifecycle)
        mainViewPager = findViewById(R.id.fragment_pager)
        mainViewPager.adapter = adapter

        if (intent.extras != null) {
            mainViewPager.setCurrentItem(intent.extras!!.getInt(INTENT_ACTION) - 1,true)
        }

        addFragmentButton.setOnClickListener {
            val newFragment = MainFragment()
            newFragment.arguments = Bundle().apply {
                putInt(ARG_FRAGMENT_ID, fragmentList.size + 1)
            }
            fragmentList.add(newFragment)

            (mainViewPager.adapter as FragmentAdapter).notifyItemInserted(fragmentList.size + 1)
            mainViewPager.setCurrentItem(fragmentList.size - 1, true)

            fragmentCounterText.text = fragmentList.size.toString()

            removeFragmentButton.visibility = View.VISIBLE
        }

        removeFragmentButton.setOnClickListener {
            if (fragmentList.size > 0) {
                fragmentList.removeLast()

                (mainViewPager.adapter as FragmentAdapter).notifyItemRemoved(fragmentList.size - 1)
                if (fragmentList.isNotEmpty()) mainViewPager.setCurrentItem(fragmentList.size - 1, true)

                fragmentCounterText.text = fragmentList.size.toString()
            }

            if (fragmentList.isEmpty()) removeFragmentButton.visibility = View.GONE
        }

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val position = intent.getIntExtra(INTENT_ACTION, 0) - 1
                if (position + 1 <= fragmentList.size) mainViewPager.setCurrentItem(position,true)
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(INTENT_ACTION) })
    }

    override fun onStop() {
        mainPreferences.saveFragmentsCount()
        super.onStop()
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun initFragments(size: Int) {
        removeFragmentButton.visibility = View.VISIBLE
        fragmentCounterText.text = size.toString()

        for (i in 1..size) {
            val newFragment = MainFragment()
            newFragment.arguments = Bundle().apply {
                putInt(ARG_FRAGMENT_ID, fragmentList.size + 1)
            }
            fragmentList.add(newFragment)
        }
    }
}