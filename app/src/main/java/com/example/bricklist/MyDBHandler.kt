package com.example.bricklist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDate

class MyDBHandler {
    private lateinit var sqLiteDatabase: SQLiteDatabase
    var context: Context
    var DATABASE_PATH = ""

    constructor(tmpContext :Context){
        context = tmpContext
        this.DATABASE_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "BrickList"
        val TABLE_INVENTORIES = "INVENTORIES"
        val COLUMN_ID = "id"
        val COLUMN_NAME = "Name"
        val COLUMN_ACTIVE = "Active"
        val COLUMN_LASTACCESSED = "LastAccessed"
        val TABLE_INVETORIES_PARTS = "InventoriesParts"
        val COLUMN_INVENTORYID = "inventoryID"
        val COLUMN_TYPEID = "TypeID"
        val COLUMN_ITEMID = "ItemID"
        val COLUMN_QUANTITYINSET = "QuantityInSet"
        val COLUMN_QUANTITYINSTORE = "QuantityInStore"
        val COLUMN_COLORID = "ColorID"
        val COLUMN_EXTRA = "Extra"
        val TABLE_PARTS = "parts"
        val TABLE_TEST = "test"
        val COLUMN_CODE = "Code"
        val TABLE_COLORS = "Colors"
        val TABLE_ITEM_TYPES = "ItemTypes"
        val TABLE_CODES = "Codes"
        val COLUMN_IMAGE = "Image"
    }


    fun findBrick(): Int {
        this.openDB()
        val query = "SELECT $COLUMN_ID FROM $TABLE_PARTS WHERE $COLUMN_ID = 1"
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpid = -1
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpid = Integer.parseInt(cursor.getString(0))
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpid
    }

    fun findBrickID(vCode: String): Int {
        this.openDB()
        val query = "SELECT $COLUMN_ID FROM $TABLE_PARTS WHERE $COLUMN_CODE = \"" + vCode + "\""

        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpid = -1
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpid = Integer.parseInt(cursor.getString(0))
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpid
    }

    fun findColorID(vCode: String): Int{
        this.openDB()
        val query = "SELECT $COLUMN_ID FROM $TABLE_COLORS WHERE $COLUMN_CODE = \"" + vCode + "\""

        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpid = -1
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpid = Integer.parseInt(cursor.getString(0))
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpid
    }

    fun findTypeID(vCode: String): Int{
        this.openDB()
        val query = "SELECT $COLUMN_ID FROM $TABLE_ITEM_TYPES WHERE $COLUMN_CODE = \"" + vCode + "\""

        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpid = -1
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpid = Integer.parseInt(cursor.getString(0))
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpid
    }

    fun getNumberOfInventoryParts(): Int{
        this.openDB()
        val query = "SELECT COUNT(*) from $TABLE_INVETORIES_PARTS"

        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpid = -1
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpid = Integer.parseInt(cursor.getString(0))
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpid
    }

    fun getPartName(id: Int):String{
        this.openDB()
        val query = "SELECT $COLUMN_NAME FROM $TABLE_PARTS WHERE id = " + id.toString()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpName = ""
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpName = cursor.getString(0)
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpName
    }

    fun getPartCode(id: Int): String{
        this.openDB()
        val query = "SELECT $COLUMN_CODE FROM $TABLE_PARTS WHERE id = " + id.toString()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpName = ""
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpName = cursor.getString(0)
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpName
    }

    fun getColor(id: Int): String{
        this.openDB()
        val query = "SELECT $COLUMN_NAME FROM $TABLE_COLORS WHERE id = " + id.toString()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpName = ""
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpName = cursor.getString(0)
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpName
    }

    fun getColorCode(id: Int): String{
        this.openDB()
        val query = "SELECT $COLUMN_CODE FROM $TABLE_COLORS WHERE id = " + id.toString()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpName = ""
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpName = cursor.getString(0)
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpName
    }

    fun getCode(itemID: Int, colorID: Int) :String{
        this.openDB()
        val query = "SELECT $COLUMN_CODE FROM $TABLE_CODES WHERE ItemID = " + itemID.toString() + " and ColorID = " + colorID.toString()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpName = ""
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpName = cursor.getString(0)
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpName
    }

    fun getImage(itemID: Int, colorID: Int) :ByteArray{
        this.openDB()
        val query = "SELECT $COLUMN_IMAGE FROM $TABLE_CODES WHERE ItemID = " + itemID.toString() + " and ColorID = " + colorID.toString()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpName: ByteArray = byteArrayOf()
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpName = cursor.getBlob(0)
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpName
    }

    fun getItemType(typeID: Int): String{
        this.openDB()
        val query = "SELECT $COLUMN_CODE FROM $TABLE_ITEM_TYPES WHERE id = " + typeID.toString()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        var tmpName = ""
        if(cursor!= null) {
            if (cursor.moveToFirst()) {
                tmpName = cursor.getString(0)
                cursor.close()
            }
        }
        sqLiteDatabase.close()
        return tmpName
    }

    fun setImage(itemID: Int, colorID: Int, bImage: ByteArray){
        this.openDB()
        val values = ContentValues()
        values.put(COLUMN_IMAGE, bImage)
        sqLiteDatabase.update(TABLE_CODES, values, "ItemID = " + itemID.toString() + " AND ColorID = " + colorID.toString(), arrayOf())
        sqLiteDatabase.close()
    }



    fun setQuantity(id: Int, qty: Int){
        this.openDB()
        val query = "UPDATE $TABLE_INVETORIES_PARTS SET $COLUMN_QUANTITYINSTORE = " + qty.toString() + " WHERE $COLUMN_ID = " + id.toString()
        sqLiteDatabase.execSQL(query)
        sqLiteDatabase.close()
    }


    fun addInventoryPart(invId: Int, b: Brick){
        Log.d("NParts", this.getNumberOfInventoryParts().toString())
        val values = ContentValues()
        values.put(COLUMN_ID, this.getNumberOfInventoryParts().toString())
        values.put(COLUMN_INVENTORYID, invId)
        values.put(COLUMN_TYPEID, this.findTypeID(b.type))
        values.put(COLUMN_ITEMID, this.findBrickID(b.id))
        values.put(COLUMN_QUANTITYINSET, b.qty.toInt())
        values.put(COLUMN_QUANTITYINSTORE, 0)
        values.put(COLUMN_COLORID, this.findColorID(b.color))
        values.put(COLUMN_EXTRA, 1)
        this.openDB()
        sqLiteDatabase.insert(TABLE_INVETORIES_PARTS, null, values)
        sqLiteDatabase.close()
        Log.d("DB", "added inventory part")
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun addInventory(id: Int, name: String, dateInt: Int){
        val today = LocalDate.now()
        var dateInt2 = (today.year * 10000) + (today.monthValue * 100) + today.dayOfMonth
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_ACTIVE, 1)
        values.put(COLUMN_LASTACCESSED, dateInt2)
        this.openDB()
        sqLiteDatabase.insert(TABLE_INVENTORIES, null, values)
        sqLiteDatabase.close()
        Log.d("DB", "added inventory")
    }




    fun copyDB() {
        var tmp = File (DATABASE_PATH)
        if(!tmp.exists()) tmp.mkdirs()
        val myInput: InputStream = context.getAssets().open("BrickList")
        val outFileName: String = DATABASE_PATH + DATABASE_NAME
        val myOutput: OutputStream = FileOutputStream(outFileName)
        val buffer = ByteArray(10)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }


    fun cleanDB(){
        val query1 = "DELETE FROM $TABLE_INVENTORIES"
        val query2 = "DELETE FROM $TABLE_INVETORIES_PARTS"

        this.openDB()
        sqLiteDatabase.execSQL(query1)
        sqLiteDatabase.execSQL(query2)
        sqLiteDatabase.close()
    }

    fun getInventories(): MutableList<Project> {
        var tmpProjects: MutableList<Project> = mutableListOf<Project>()

        val query = "SELECT * FROM $TABLE_INVENTORIES"
        this.openDB()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        if (cursor != null){
            while(cursor.moveToNext()){
                var tmpProject:Project
                val id = Integer.parseInt((cursor.getString(0)))
                val name = cursor.getString(1)
                val active = Integer.parseInt((cursor.getString(2)))
                val lastAccessed = Integer.parseInt((cursor.getString(3)))
                tmpProject = Project(id, name, active, lastAccessed)
                tmpProjects.add(tmpProject)
                Log.d("DB", name)
            }
        }
        return tmpProjects
    }


    fun getInventoriesNotArchived(): MutableList<Project> {
        var tmpProjects: MutableList<Project> = mutableListOf<Project>()

        val query = "SELECT * FROM $TABLE_INVENTORIES WHERE $COLUMN_ACTIVE != 0"
        this.openDB()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        if (cursor != null){
            while(cursor.moveToNext()){
                var tmpProject:Project
                val id = Integer.parseInt((cursor.getString(0)))
                val name = cursor.getString(1)
                val active = Integer.parseInt((cursor.getString(2)))
                val lastAccessed = Integer.parseInt((cursor.getString(3)))
                tmpProject = Project(id, name, active, lastAccessed)
                tmpProjects.add(tmpProject)
                Log.d("DB", name)
            }
        }
        return tmpProjects
    }

    fun archiveInventory(id: Int){
        this.openDB()
        val query = "UPDATE $TABLE_INVENTORIES SET $COLUMN_ACTIVE = 0 WHERE $COLUMN_ID = " + id.toString()
        sqLiteDatabase.execSQL(query)
        sqLiteDatabase.close()
    }

    fun getParts(inventoryID: Int): MutableList<InventoryPart>{
        var tmpParts: MutableList<InventoryPart> = mutableListOf<InventoryPart>()

        val query = "SELECT * FROM $TABLE_INVETORIES_PARTS WHERE $COLUMN_INVENTORYID = " + inventoryID.toString()
        this.openDB()
        var cursor: Cursor? = sqLiteDatabase.rawQuery(query,null)
        if (cursor != null){
            cursor.moveToFirst()
            var tmpPart:InventoryPart
            var id = Integer.parseInt((cursor.getString(0)))
            var inventoryID = Integer.parseInt((cursor.getString(1)))
            var typeID = Integer.parseInt((cursor.getString(2)))
            var itemID = Integer.parseInt((cursor.getString(3)))
            var quantityInSet = Integer.parseInt((cursor.getString(4)))
            var quantityInStore = Integer.parseInt((cursor.getString(5)))
            var colorID = Integer.parseInt((cursor.getString(6)))
            var extra = Integer.parseInt((cursor.getString(7)))
            tmpPart = InventoryPart(id, inventoryID, typeID, itemID, quantityInSet, quantityInStore, colorID, extra)
            tmpParts.add(tmpPart)
            Log.d("PART", itemID.toString())
            while(cursor.moveToNext()){
                var tmpPart:InventoryPart
                var id = Integer.parseInt((cursor.getString(0)))
                var inventoryID = Integer.parseInt((cursor.getString(1)))
                var typeID = Integer.parseInt((cursor.getString(2)))
                var itemID = Integer.parseInt((cursor.getString(3)))
                var quantityInSet = Integer.parseInt((cursor.getString(4)))
                var quantityInStore = Integer.parseInt((cursor.getString(5)))
                var colorID = Integer.parseInt((cursor.getString(6)))
                var extra = Integer.parseInt((cursor.getString(7)))
                tmpPart = InventoryPart(id, inventoryID, typeID, itemID, quantityInSet, quantityInStore, colorID, extra)
                tmpParts.add(tmpPart)
                Log.d("PART", itemID.toString())
            }
        }
        return tmpParts
    }

    fun createDB(){
        var exist: Boolean = checkDB()
        if (!exist){
            try {
                copyDB();
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun checkDB(): Boolean {
        var checkDB = File(DATABASE_PATH + DATABASE_NAME)
        Log.i("path", DATABASE_PATH + DATABASE_NAME)
        return checkDB.exists()
    }


    fun openDB(){
        val myPath: String = DATABASE_PATH+ DATABASE_NAME
        sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE)
    }


}