package com.landable.app.ui.home.auction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AuctionDocumentClickListener
import com.landable.app.databinding.RowAdvertisementsBinding
import com.landable.app.databinding.RowAuctionDocumentBinding
import com.landable.app.ui.home.dataModels.Auctiondocument

class AuctionDocumentAdapter(
    private val documentList: ArrayList<Auctiondocument>,
    private var auctionDocument: AuctionDocumentClickListener
) : RecyclerView.Adapter<AuctionViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val binding: RowAuctionDocumentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_auction_document,
            parent,
            false
        )
        return AuctionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        val document = documentList[position]

        holder.auctionBinding.ivDocument.setOnClickListener {
            auctionDocument.onDocumentClick("documentClick", document)
        }
    }

    override fun getItemCount(): Int {
        return documentList.size
    }
}


class AuctionViewHolder(var auctionBinding: RowAuctionDocumentBinding) :
    RecyclerView.ViewHolder(auctionBinding.root)

