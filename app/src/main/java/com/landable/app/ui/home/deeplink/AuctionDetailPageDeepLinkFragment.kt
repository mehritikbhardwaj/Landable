package com.landable.app.ui.home.deeplink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.google.firebase.analytics.FirebaseAnalytics
import com.landable.app.R
import com.landable.app.common.AdvertisementClickListener
import com.landable.app.common.AuctionDocumentClickListener
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentAuctionDetailBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.home.auction.AuctionDetailPageFragment
import com.landable.app.ui.home.auction.AuctionDocumentAdapter
import com.landable.app.ui.home.dataModels.Advertisment
import com.landable.app.ui.home.dataModels.AuctionDetailDataModel
import com.landable.app.ui.home.dataModels.Auctiondocument
import com.landable.app.ui.home.property.adapters.AdvertisementAdapter

class AuctionDetailPageDeepLinkFragment : Fragment(), AdvertisementClickListener,
    AuctionDocumentClickListener {
    private lateinit var binding: FragmentAuctionDetailBinding
    private var auctionData: AuctionDetailDataModel? = null
    private var auctionId: Int = 0
    private var linkResponse: String = ""
    private var url: String = ""

    companion object {
        fun newInstance() = AuctionDetailPageDeepLinkFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // getting album model from other fragments

        if (LandableConstants.deepLinkURL!!.isNotEmpty()) {
            auctionId = LandableConstants.deepLinkURL!!.substringAfter("-ac-").toInt()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Auction Detail")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_auction_detail, container, false)

        FirebaseAnalytics.getInstance((activity as HomeActivity))
            .setCurrentScreen((activity as HomeActivity), "Auction Detail Fragment", null)

        binding.buttonContact.setOnClickListener {
            CustomAlertDialog(
                requireContext(),
                "Contact Details",
                auctionData!!.Auction[0].contactdetails
            ).show()
        }

        binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val link = linkResponse
            val name = auctionData!!.Auction[0].title

            val shareBody = "$name \n $link"

            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.share_subject)
            )
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, "share"))
        }
        getAuctionDetails(auctionId)

        binding.tvWebsite.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        return binding.root
    }

    private fun getAuctionDetails(id: Int) {
        val auctionResponse = RegisterRepository().getAuctionDetails(id)
        auctionResponse.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    auctionData = ParseResponse.parseAuctionDetailResponse(it.toString())
                    updateUI()
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI() {

        binding.tvAuctionEnd.text = auctionData!!.Auction[0].enddate

        binding.ivProfilePicture.load(LandableConstants.Image_URL + auctionData!!.Auction[0].image1)
        binding.tvAuctionName.text = auctionData!!.Auction[0].title
        binding.tvBankName.text = auctionData!!.Auction[0].bankname
        binding.tvAuid.text = auctionData!!.Auction[0].auid
        binding.tvLocation.text = auctionData!!.Auction[0].locality

        binding.tvPrice.text = "\u20B9 " + auctionData!!.Auction[0].reservepriceinword
        binding.tvStartDate.text = "Start Date: " + auctionData!!.Auction[0].startdate


        binding.tvDescription.text = auctionData!!.Auction[0].locatiodesc
        linkResponse = LandableConstants.Image_URL + auctionData!!.Auction[0].linkurl
        binding.tvBorrowerName.text = auctionData!!.Auction[0].borrowername
        binding.tvPropertyTypeName.text = auctionData!!.Auction[0].categoryname

        if (auctionData!!.Auction[0].possessiontypename.isNullOrEmpty()) {
            binding.possessionLayout.visibility = View.GONE
        } else {
            binding.tvPossessionStatus.text = auctionData!!.Auction[0].possessiontypename
        }

        if (auctionData!!.Auction[0].act.isNullOrEmpty()) {
            binding.actLayout.visibility = View.GONE
        } else {
            binding.tvAct.text = auctionData!!.Auction[0].act
        }

        binding.tvLocality.text = auctionData!!.Auction[0].locality
        binding.tvCityName.text = auctionData!!.Auction[0].cityname
        binding.tvReservePrice.text = "\u20B9 " + auctionData!!.Auction[0].reservepriceinword
        binding.tvEMDAmount.text = auctionData!!.Auction[0].emdamountinword

        if (auctionData!!.Auction[0].emdsubmission.isNullOrEmpty()) {
            binding.emdSubmission.visibility = View.GONE
        } else {
            binding.tvEMDSubmission.text = auctionData!!.Auction[0].emdsubmission
        }

        binding.tvAuctionStart.text =
            auctionData!!.Auction[0].startdate + " " + auctionData!!.Auction[0].stime
        /*  binding.tvEndDate.text =
              auctionData!!.Auction[0].enddate + " " + auctionData!!.Auction[0].etime
  */
        if (auctionData!!.Auction[0].inspectiondate.isNullOrEmpty()) {
            binding.inspectionLayout.visibility = View.GONE
        } else {
            binding.tvInspectionDate.text = auctionData!!.Auction[0].inspectiondate
        }

//        binding.tvWebsite.text = auctionData!!.Auction[0].website

        url = auctionData!!.Auction[0].website

        binding.rvAdvertisements.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvAdvertisements.adapter = AdvertisementAdapter(auctionData!!.advertisment, this)

        if (auctionData!!.auctiondocuments.size == 0) {
            binding.tvDocuments.visibility = View.GONE
        } else {
            binding.rvAuctionDocuments.layoutManager = GridLayoutManager(requireContext(), 7)
            binding.rvAuctionDocuments.adapter =
                AuctionDocumentAdapter(auctionData!!.auctiondocuments, this)
        }


    }

    override fun onAdvertisemntClick(action: String, advertisementDataModel: Advertisment?) {
        when (action) {
            "advertisementClick" -> {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(advertisementDataModel!!.link)
                startActivity(openURL)
            }
        }
    }

    override fun onDocumentClick(action: String, auctiondocument: Auctiondocument?) {
        when (action) {
            "documentClick" -> {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(LandableConstants.Image_URL + auctiondocument!!.docpath)
                startActivity(openURL)
            }
        }
    }


}