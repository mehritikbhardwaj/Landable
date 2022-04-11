package com.landable.app.ui.home.property.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.landable.app.R
import com.landable.app.common.LandableConstants
import com.landable.app.databinding.RowReviewDetailPageBinding
import com.landable.app.ui.home.dataModels.Review

class PropertyReviewAdapter (
    private val reviewList: ArrayList<Review>
) : RecyclerView.Adapter<ReviewViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding: RowReviewDetailPageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_review_detail_page,
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]


        holder.reviewBinding.tvName.text = review.name
        holder.reviewBinding.tvReview.text = review.reviewmsg
        holder.reviewBinding.circleImageView2.load(LandableConstants.Image_URL + review.logo)
        holder.reviewBinding.rating.rating = review.star.toFloat()

    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}


class ReviewViewHolder(var reviewBinding: RowReviewDetailPageBinding) :
    RecyclerView.ViewHolder(reviewBinding.root)

