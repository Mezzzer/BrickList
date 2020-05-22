package com.example.bricklist

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.util.Xml
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_project.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xmlpull.v1.XmlSerializer
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class ProjectActivity : AppCompatActivity(){

    var position: Int = 0
    var partsList: MutableList<InventoryPart>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        position = intent.extras!!.getString("POSITION")!!.toInt()


        val myDBHandler: MyDBHandler = MyDBHandler(this)
        partsList = myDBHandler.getParts(ProjectsList.projects[position].id)

        val myListAdapter: MyListAdapter = MyListAdapter(this, partsList!!)
        listView.adapter =  myListAdapter

        exportButton.setOnClickListener {
            partsList = myDBHandler.getParts(ProjectsList.projects[position].id)
            val xmlSerializer: XmlSerializer = Xml.newSerializer()
            val writer: StringWriter = StringWriter()
            try{
                xmlSerializer.setOutput(writer)
                xmlSerializer.startDocument("UTF-8", true)
                xmlSerializer.startTag("", "INVENTORY")
                for(p in partsList!!){
                    if(p.quantityInSet != p.quantityInStore) {
                        xmlSerializer.startTag("", "ITEM")

                        xmlSerializer.startTag("", "ITEMTYPE")
                        xmlSerializer.text(myDBHandler.getItemType(p.typeID))
                        xmlSerializer.endTag("", "ITEMTYPE")

                        xmlSerializer.startTag("", "ITEMID")
                        xmlSerializer.text(myDBHandler.getPartCode(p.itemID))
                        xmlSerializer.endTag("", "ITEMID")

                        xmlSerializer.startTag("", "COLOR")
                        xmlSerializer.text(myDBHandler.getColorCode(p.colorID))
                        xmlSerializer.endTag("", "COLOR")

                        xmlSerializer.startTag("", "QTYFILLED")
                        xmlSerializer.text((p.quantityInSet - p.quantityInStore).toString())
                        xmlSerializer.endTag("", "QTYFILLED")

                        xmlSerializer.endTag("", "ITEM")
                    }
                }
                xmlSerializer.endTag("", "INVENTORY")
                xmlSerializer.endDocument()
                var result: String = writer.toString()
                this.saveToFile(ProjectsList.projects[position].name + ".xml", result)
            }catch (e: Exception){
            }
        }

        archiveButton.setOnClickListener {
            myDBHandler.archiveInventory(ProjectsList.projects[position].id)
        }
    }


    fun saveToFile(fileName: String, result: String){
        try{
            val fos: FileOutputStream = this.openFileOutput(fileName, Context.MODE_PRIVATE)
            fos.write(result.toByteArray(), 0, result.length)
            fos.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }


}