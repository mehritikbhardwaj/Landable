package com.landable.app.ui.home.search

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.databinding.RowCategoriesFilterBinding
import com.landable.app.ui.home.dataModels.Arbitragemaster
import com.landable.app.ui.home.dataModels.Bedroom

class BedBathBalParAdapter (
    private val arrayList: ArrayList<Bedroom>,
    private var agentProfileListener: AgentProfileListener,
    private var isComingFromSale:String
) :
    RecyclerView.Adapter<BedBAthHolder>() {

    private var selectedView: LinearLayout? = null
    private var selectedTextView: TextView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BedBAthHolder {
        val bedBAthBinding: RowCategoriesFilterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_categories_filter,
            parent,
            false
        )
        return BedBAthHolder(bedBAthBinding)
    }

    override fun onBindViewHolder(holder: BedBAthHolder, position: Int) {
        val list = arrayList[position]
       /* if (position == 0) {
            selectedView = holder.bedBAthBinding.linearLayout3
            selectedTextView = holder.bedBAthBinding.tvCategoryName
            changeBackground(selectedView!!, holder.bedBAthBinding.tvCategoryName)
        }

        holder.bedBAthBinding.tvCategoryName.text = list.value
        holder.bedBAthBinding.linearLayout3.setOnClickListener {

            selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
            selectedTextView!!.setTextColor(Color.BLACK)
            changeBackground(
                holder.bedBAthBinding.linearLayout3,
                holder.bedBAthBinding.tvCategoryName
            )
            selectedView = holder.bedBAthBinding.linearLayout3
            selectedTextView = holder.bedBAthBinding.tvCategoryName
            if (isComingFromSale=="bedroom"){
                agentProfileListener.onAgentClick("bedroom",list.id)
            } else if(isComingFromSale=="bathroom"){
                agentProfileListener.onAgentClick("bathroom",list.id)
            }
            else if(isComingFromSale=="balcony"){
                agentProfileListener.onAgentClick("balcony",list.id)
            }
            else if(isComingFromSale=="parking"){
                agentProfileListener.onAgentClick("parking",list.id)
            }

        }*/

        holder.bedBAthBinding.tvCategoryName.text = list.value
        holder.bedBAthBinding.linearLayout3.setOnClickListener {
            if (selectedView == null) {
                selectedView = holder.bedBAthBinding.linearLayout3
                selectedTextView = holder.bedBAthBinding.tvCategoryName
               changeBackground(
                    selectedView!!,
                    selectedTextView!!
                )
                if (isComingFromSale=="bedroom"){
                    agentProfileListener.onAgentClick("bedroom",list.id)
                } else if(isComingFromSale=="bathroom"){
                    agentProfileListener.onAgentClick("bathroom",list.id)
                }
                else if(isComingFromSale=="balcony"){
                    agentProfileListener.onAgentClick("balcony",list.id)
                }
                else if(isComingFromSale=="parking"){
                    agentProfileListener.onAgentClick("parking",list.id)
                }
            } else if (selectedView == holder.bedBAthBinding.linearLayout3) {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                selectedView = null
                if (isComingFromSale=="bedroom"){
                    agentProfileListener.onAgentClick("Nobedroom",list.id)
                } else if(isComingFromSale=="bathroom"){
                    agentProfileListener.onAgentClick("Nobathroom",list.id)
                }
                else if(isComingFromSale=="balcony"){
                    agentProfileListener.onAgentClick("Nobalcony",list.id)
                }
                else if(isComingFromSale=="parking"){
                    agentProfileListener.onAgentClick("Noparking",list.id)
                }
            } else {
                selectedView!!.setBackgroundResource(R.drawable.alert_dialog_bg)
                selectedTextView!!.setTextColor(Color.BLACK)
                changeBackground(
                    holder.bedBAthBinding.linearLayout3,
                    holder.bedBAthBinding.tvCategoryName
                )
                selectedView = holder.bedBAthBinding.linearLayout3
                selectedTextView = holder.bedBAthBinding.tvCategoryName
                if (isComingFromSale=="bedroom"){
                    agentProfileListener.onAgentClick("bedroom",list.id)
                } else if(isComingFromSale=="bathroom"){
                    agentProfileListener.onAgentClick("bathroom",list.id)
                }
                else if(isComingFromSale=="balcony"){
                    agentProfileListener.onAgentClick("balcony",list.id)
                }
                else if(isComingFromSale=="parking"){
                    agentProfileListener.onAgentClick("parking",list.id)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}

private fun changeBackground(layout: LinearLayout, tvCategoryName: TextView) {
    layout.setBackgroundResource(R.drawable.free_home_page_bg)
    tvCategoryName.setTextColor(Color.WHITE)
}


class BedBAthHolder(var bedBAthBinding: RowCategoriesFilterBinding) :
    RecyclerView.ViewHolder(bedBAthBinding.root)