package com.example.bricklist

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity()  {

    var vis = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadPreferences()
        val myDBHandler: MyDBHandler = MyDBHandler(this)
        myDBHandler.copyDB()

        settingsButton.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        addButton.setOnClickListener {
            startActivity(Intent(this, NewProjectActivity::class.java))
        }

        setsList.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            Log.d("POZYCJA", position.toString())


            val myDBHandler: MyDBHandler = MyDBHandler(this)
            Log.d("DB" , myDBHandler.findBrick().toString())



            val i = Intent(this, ProjectActivity::class.java)
            val strName: String? = null
            i.putExtra("POSITION", position.toString())
            startActivity(i)

        })

    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    fun refreshList(){
        val myDBHandler: MyDBHandler = MyDBHandler(this)
        if(vis)
            ProjectsList.projects = myDBHandler.getInventories()
        else
            ProjectsList.projects = myDBHandler.getInventoriesNotArchived()

        Log.d("REFRESH", "ODŚWIEŻAM")
        val arrayAdapter: ArrayAdapter<*>

        var tmpP: MutableList<String> = mutableListOf<String>()
        for(p in ProjectsList.projects){
            Log.d("NAME", p.name)
            tmpP.add(p.name)
        }
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, tmpP)
        setsList.adapter = arrayAdapter
    }

    fun loadPreferences() {
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        if (sharedPreferences.contains("visible")) {
            vis = sharedPreferences.getString("visible", null)!!.toBoolean()

        }
    }

}
