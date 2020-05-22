package com.example.bricklist

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.media.Image
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_project.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.*
import java.net.MalformedURLException
import java.net.Socket
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory


class NewProjectActivity : AppCompatActivity(){

    var brickList: MutableList<Brick>? = null
    var check: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_project)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        val actionBar = supportActionBar
        actionBar!!.title = "New Project"

        addButton.setEnabled(false);


        addButton.setOnClickListener {
            val myDBHandler: MyDBHandler = MyDBHandler(this)
            myDBHandler.addInventory(ProjectsList.projects.size + 1, nameInput.editText?.text.toString(), ProjectsList.projects.size + 1)


            loadData()

            val partsList = myDBHandler.getParts(ProjectsList.projects.size + 1)


            for(b in partsList){
                val iD = ImageDownloader()
                var sufix = myDBHandler.getCode(b.itemID, b.colorID)
                if (sufix != "") {
                    iD.tmpUrl = "https://www.lego.com/service/bricks/5/2/" + sufix
                    iD.itemID = b.itemID
                    iD.colorID = b.colorID
                    iD.execute()
                }
            }

            startActivity(Intent(this, MainActivity::class.java))
        }

        checkButton.setOnClickListener {
            var testUrl = "http://fcds.cs.put.poznan.pl/MyWeb/BL/615.xml"
            downloadData(ProjectsList.projectsUrl + numberInput.editText?.text.toString() + ".xml")
            Thread.sleep(1000)
            if(check){
                addButton.setEnabled(true);
            }
        }
    }




    fun downloadData(vUrl: String){
        val bd = BricksDownloader()
        bd.tmpUrl = vUrl
        bd.execute()
    }



    fun loadData(){
        Log.d("XML","LOADING")

        brickList = mutableListOf()

        val filename = "lego.xml"
        val path = filesDir
        val inDir = File(path, "XML")

        if(inDir.exists()){
            val file = File(inDir, filename)
            if(file.exists()){
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

                val items: NodeList = xmlDoc.getElementsByTagName("ITEM")
                Log.d("BRICK", items.length.toString())

                for (i in 0..items.length - 1){
                    val itemNode: Node = items.item(i)

                    if(itemNode.getNodeType() == Node.ELEMENT_NODE){
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        var currentType: String? = null
                        var currentId: String? = null
                        var currentQty: String? = null
                        var currentColor: String? = null
                        var currentExtra: String? = null
                        var currentAltenate: String? = null
                        var currentMat: String? = null
                        var currentCounter: String? = null

                        for (j in 0..children.length - 1){
                            val node = children.item(j)
                            if(node is Element){
                                when(node.nodeName){
                                    "ITEMTYPE" -> {currentType = node.textContent}
                                    "ITEMID" -> {currentId = node.textContent}
                                    "QTY" -> {currentQty =  node.textContent}
                                    "COLOR" -> {currentColor = node.textContent}
                                    "EXTRA" -> {currentExtra = node.textContent}
                                    "ALTERNATE" -> {currentAltenate = node.textContent}
                                }
                            }
                        }
                        Log.d("BRICK", "ZALADOWALEM")
                        Log.d("TYP", currentType)

                        if(currentType!=null && currentId!= null && currentQty!=null && currentColor !=null && currentExtra!=null && currentAltenate == "N"){
                            val b=Brick(currentType, currentId.toString(),currentQty.toString(),currentColor.toString(), currentExtra.toString(), currentAltenate.toString())
                            Log.d("BRICK", b.id)
                            val myDBHandler: MyDBHandler = MyDBHandler(this)
                            myDBHandler.addInventoryPart(ProjectsList.projects.size + 1, b)
                            brickList!!.add(b)
                        }
                    }
                }
            }
        }
    }



    private inner class BricksDownloader: AsyncTask<String, Int, String>() {

        var tmpUrl: String? = null

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }



        override fun doInBackground(vararg params: String?): String {
            try {
                Log.d("XML","TRYING")

                val url = URL(tmpUrl)
                val connection = url.openConnection()
                val lengthOfFile = connection.contentLength
                val isStream = url.openStream()
                val testDirectory = File("$filesDir/XML")
                if (!testDirectory.exists()) testDirectory.mkdir()
                val fos = FileOutputStream("$testDirectory/lego.xml")
                val data = ByteArray(1024)
                var count = 0
                var total: Long = 0
                var progress = 0
                count = isStream.read(data)
                while (count != -1) {
                    total += count.toLong()
                    val progress_temp = total.toInt() * 100 / lengthOfFile
                    if (progress_temp % 10 == 0 && progress != progress_temp) {
                        progress = progress_temp
                    }
                    fos.write(data, 0, count)
                    count = isStream.read(data)
                }
                isStream.close()
                fos.close()
            } catch (e: MalformedURLException){
                Log.d("XML","Malformed URL")
                return "Malformed URL"
            } catch (e: FileNotFoundException){
                Log.d("XML", "File not found")
                return "File not found"
            } catch (e: IOException){
                return "IO Exception"
            }
            Log.d("XML", "SUCCESS")
            check = true

            return "success"
        }
    }

    private inner class ImageDownloader: AsyncTask<String, Int, String>(){
        var tmpUrl: String? = null
        var itemID: Int = 0
        var colorID: Int = 0

        override fun doInBackground(vararg params: String?): String? {
            Log.d("URL", tmpUrl)


            var url: URL = URL(tmpUrl)
            var bmp: Bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            val stream = ByteArrayOutputStream()
            bmp.compress(CompressFormat.PNG, 0, stream)
            val byte = stream.toByteArray()

            val myDBHandler: MyDBHandler = MyDBHandler(this@NewProjectActivity)
            myDBHandler.setImage(itemID, colorID, byte)
            return "success"
        }

    }

}