package com.example.bricklist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.provider.Telephony
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

public class MyListAdapter(private val context: Activity, val partsList :MutableList<InventoryPart>) : BaseAdapter() {


    @SuppressLint("RestrictedApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val inflater = context.layoutInflater
            val rowView = inflater.inflate(R.layout.row_layout, null, true)
        if(position !=0 ) {
            val myDBHandler: MyDBHandler = MyDBHandler(context)

            val titleText = rowView.findViewById(R.id.textTitle) as TextView
            val textQty = rowView.findViewById(R.id.textQty) as TextView
            val textCol = rowView.findViewById(R.id.textColor) as TextView


            titleText.setText(myDBHandler.getPartName(partsList[position].itemID))
            textCol.setText(
                myDBHandler.getColor(partsList[position].colorID) + " [" + myDBHandler.getPartCode(partsList[position].itemID) + "]"
            )
            textQty.setText(partsList[position].quantityInStore.toString() + " of " + partsList[position].quantityInSet.toString())

            val imageView = rowView.findViewById(R.id.imageView) as ImageView

            val byteImage = myDBHandler.getImage(partsList[position].itemID, partsList[position].colorID)

            imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.size))

            val plusButton = rowView.findViewById(R.id.plusButton) as FloatingActionButton
            val minusButton = rowView.findViewById(R.id.minusButton) as FloatingActionButton

            plusButton.setOnClickListener {
                if (partsList[position].quantityInStore < partsList[position].quantityInSet) {
                    partsList[position].quantityInStore += 1
                    myDBHandler.setQuantity(
                        partsList[position].id,
                        partsList[position].quantityInStore
                    )
                    textQty.setText(partsList[position].quantityInStore.toString() + " of " + partsList[position].quantityInSet.toString())

                }
            }

            minusButton.setOnClickListener {
                if (partsList[position].quantityInStore > 0) {
                    partsList[position].quantityInStore -= 1
                    myDBHandler.setQuantity(
                        partsList[position].id,
                        partsList[position].quantityInStore
                    )
                    textQty.setText(partsList[position].quantityInStore.toString() + " of " + partsList[position].quantityInSet.toString())
                }
            }
        }
        else{
            val titleText = rowView.findViewById(R.id.textTitle) as TextView
            titleText.setText("ADD AND REMOVE BRICKS USING BUTTONS")
            val textCol = rowView.findViewById(R.id.textColor) as TextView
            textCol.setText("OR EXPORT MISSING BRICKS TO XML")
            val textQty = rowView.findViewById(R.id.textQty) as TextView
            textQty.setText("")
            val plusButton = rowView.findViewById(R.id.plusButton) as FloatingActionButton
            val minusButton = rowView.findViewById(R.id.minusButton) as FloatingActionButton
            plusButton.setVisibility(View.GONE)
            minusButton.setVisibility(View.GONE)

        }
        return rowView
    }

    override fun getItem(position: Int): Any {
        return partsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return partsList.size
    }
}