package com.example.personalapplock

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class MainActivity : FragmentActivity() {
    
    private var allApps = listOf<AppInfo>()
    private var lockedApps = listOf<AppInfo>() // We will add lock logic later
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.btnSettings).setOnClickListener { 
            startActivity(Intent(this, SettingsActivity::class.java)) 
        }

        // Fetch real apps from the device
        allApps = getInstalledApps()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewApps)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        val adapter = AppAdapter(allApps)
        recyclerView.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    adapter.updateData(allApps)
                } else {
                    adapter.updateData(lockedApps)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun getInstalledApps(): List<AppInfo> {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        
        val resolveInfos = pm.queryIntentActivities(intent, 0)
        val apps = mutableListOf<AppInfo>()
        
        for (resolveInfo in resolveInfos) {
            val packageName = resolveInfo.activityInfo.packageName
            // Don't show our own AppLock in the list to be locked
            if (packageName != this.packageName) {
                val name = resolveInfo.loadLabel(pm).toString()
                val icon = resolveInfo.loadIcon(pm)
                apps.add(AppInfo(name, packageName, icon))
            }
        }
        
        // Sort alphabetically by app name
        return apps.sortedBy { it.name.lowercase() }
    }
}
