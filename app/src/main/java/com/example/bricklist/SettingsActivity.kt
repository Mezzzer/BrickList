package com.example.bricklist

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_project.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(){

    var vis = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        val actionBar = supportActionBar
        actionBar!!.title = "Settings"

        archiveSwitch.setChecked(this.vis)

        textUrl.setText("URL prefix:")
        editUrl.setText(ProjectsList.projectsUrl)
        saveButton.setOnClickListener{
            ProjectsList.projectsUrl = editUrl.getText().toString()
            editUrl.setText(ProjectsList.projectsUrl)
            this.saveSettings("visible", archiveSwitch.isChecked.toString())
            loadPreferences()
        }


    }


    fun saveSettings(key: String, value: String) {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }


    fun loadPreferences() {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        if (sharedPreferences.contains("visible")) {
            vis = sharedPreferences.getString("visible", null)!!.toBoolean()

        }
    }
}