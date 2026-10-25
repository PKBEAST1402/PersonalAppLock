package com.example.personalapplock

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class MainActivity : FragmentActivity() {
    
    // Dummy data matching your screenshot to test the UI!
    private val allApps = listOf("ACT Fibernet", "Amazon", "Authenticator", "Bixby", "Bloomee", "Brake Time", "Calculator", "Calendar", "Camera")
    private val lockedApps = listOf("Amazon", "Camera", "Photos") // Example locked apps
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewApps)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        val adapter = AppAdapter(allApps)
        recyclerView.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    adapter.updateData(allApps) // Show all apps
                } else {
                    adapter.updateData(lockedApps) // Show only locked apps
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}
