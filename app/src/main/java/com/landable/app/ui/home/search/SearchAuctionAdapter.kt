package com.landable.app.ui.home.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.AuctionDetailClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowSearchAuctionsBinding
import com.landable.app.ui.home.dataModels.AuctionSearchInfoModel

class SearchAuctionAdapter(
    private val auctionsList: ArrayList<AuctionSearchInfoModel>,
    private val auctionDetailClickListener: AuctionDetailClickListener
) :
    RecyclerView.Adapter<AuctionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionViewHolder {
        val binding: RowSearchAuctionsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_search_auctions,
            parent,
            false
        )
        return AuctionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuctionViewHolder, position: Int) {
        val auction = auctionsList[position]

        holder.auctionsBinding.ivProfilePicture.load(LandableConstants.Image_URL + auction.image1)
        holder.auctionsBinding.auid.text = auction.auid
        holder.auctionsBinding.tvAuctionName.text = auction.title
        holder.auctionsBinding.tvBankName.text = auction.bankname
        holder.auctionsBinding.tvLocation.text = auction.locality
        holder.auctionsBinding.tvDescription.text = auction.locatiodesc
        holder.auctionsBinding.tvPrice.text = "\u20B9 "+ auction.reservepriceinword

        holder.auctionsBinding.llAuction.setOnClickListener {
            auctionDetailClickListener.onAuctionClick("selectedAuctionDetail", auction)
        }


    }


    override fun getItemCount(): Int {
        return auctionsList.size
    }
}


class AuctionViewHolder(var auctionsBinding: RowSearchAuctionsBinding) :
    RecyclerView.ViewHolder(auctionsBinding.root)

