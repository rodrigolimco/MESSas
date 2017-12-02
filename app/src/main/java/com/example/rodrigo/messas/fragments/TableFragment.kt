package com.example.rodrigo.messas.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.rodrigo.messas.R
import com.example.rodrigo.messas.activity.PlateDetailActivity
import com.example.rodrigo.messas.model.Plate
import com.example.rodrigo.messas.model.Table
import com.example.rodrigo.messas.model.Tables

class TableFragment: Fragment() {

    companion object {

        private val EXTRA_TABLE_FRAGMENT = "EXTRA_TABLE_FRAGMENT"
        private val EXTRA_POSITION_FRAGMENT = "EXTRA_POSITION_FRAGMENT"

        fun newInstance(table: Table, position: Int): TableFragment{
            val fragment = TableFragment()
            val arguments = Bundle()
            arguments.putSerializable(EXTRA_TABLE_FRAGMENT, table)
            arguments.putInt(EXTRA_POSITION_FRAGMENT, position)
            fragment.arguments = arguments

            return  fragment
        }
    }

    lateinit var root: View
    lateinit var platesList: ListView
    lateinit var tablePlates: MutableList<Plate>
    lateinit var totalBill: MutableList<Float>
    lateinit var billButton: MenuItem

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        if (inflater != null) {
            root = inflater.inflate(R.layout.fragment_table, container, false)
            val arg = arguments
            val position = arg.getInt(EXTRA_POSITION_FRAGMENT)
            val table = Tables.get(position)
            tablePlates = table.plates
            totalBill = table.totalBill

            activity.actionBar.title = table.name
            activity.actionBar.setDisplayHomeAsUpEnabled(true)

            platesList = root.findViewById(R.id.table_plates_list)
            platesList.adapter = ArrayAdapter<Plate>(activity, android.R.layout.simple_list_item_1, tablePlates.toTypedArray())
            platesList.setOnItemClickListener { parent, view, position, id ->
                val plate = tablePlates.get(position)
                startActivity(PlateDetailActivity.intent(activity, plate, position))
            }
        }
        return root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            val resultPlate = data?.getSerializableExtra("EXTRA_PLATE_RESULT") as Plate
            updateData(resultPlate)
            platesList.adapter = ArrayAdapter<Plate>(activity, android.R.layout.simple_list_item_1, tablePlates.toTypedArray())
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_bill, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId){
        R.id.bill_button -> {
            AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.bill_button_text, totalBill.sum()))
                    .setMessage(getString(R.string.alert_dialog_message))
                    .setPositiveButton("Ok", {dialog, _ ->
                        dialog.dismiss()
                        tablePlates.clear()
                        totalBill.clear()
                        activity.finish()
                    })
                    .setNegativeButton("Cancelar",{ dialog, _ -> dialog.dismiss()})
                    .show()
            true
        }
        android.R.id.home -> {
            activity.finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        billButton = menu!!.findItem(R.id.bill_button)
        billButton.title = getString(R.string.bill_button_text, totalBill.sum())
    }


    fun updateData(plate: Plate) {
        tablePlates.add(plate)
        totalBill.add(plate.price)
        billButton.setTitle(getString(R.string.bill_button_text, totalBill.sum()))
    }
}