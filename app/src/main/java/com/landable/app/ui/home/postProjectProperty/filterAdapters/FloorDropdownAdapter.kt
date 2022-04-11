package com.landable.app.ui.home.postProjectProperty.filterAdapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.landable.app.R
import com.landable.app.common.DropDownListClickListener
import com.landable.app.ui.home.dataModels.Floormaster

class FloorDropdownAdapter(
    var activity: Activity?, var list: ArrayList<Floormaster>?,
    private var dropDownListClickListener: DropDownListClickListener
) : BaseAdapter() {

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItem(position: Int): Floormaster {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        if (convertView == null) {
            val inflater =
                activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.dropdown_row_layout, parent, false)
        }
        val textView: TextView = convertView!!.findViewById(R.id.textView)
        textView.text = getItem(position).floor
        return convertView
    }

    fun updateList(list1: ArrayList<Floormaster>?) {
        list = list1
        notifyDataSetChanged()
    }
}