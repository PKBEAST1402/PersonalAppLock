package com.example.personalapplock

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// A data class to hold the real app details
data class AppInfo(val name: String, val packageName: String, val icon: Drawable)

class AppAdapter(private var appList: List<AppInfo>) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName: TextView = view.findViewById(R.id.appName)
        val appIcon: ImageView = view.findViewById(R.id.appIcon)
        val lockIcon: ImageView = view.findViewById(R.id.lockIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = appList[position]
        holder.appName.text = app.name
        holder.appIcon.setImageDrawable(app.icon) // Set the real app icon!
    }

    override fun getItemCount() = appList.size
    
    fun updateData(newList: List<AppInfo>) {
        appList = newList
        notifyDataSetChanged()
    }
}
