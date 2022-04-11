package com.landable.app.ui.home.property

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.landable.app.R
import com.landable.app.common.AgentProfileListener
import com.landable.app.common.AppInfo
import com.landable.app.common.LandableConstants
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.databinding.DialogPostReviewBinding
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog

class PostReviewDialogFragment(
    private var propertyID: String,
    private var agentProfileListener: AgentProfileListener,
    private var type: String
) : DialogFragment() {

    private var binding: DialogPostReviewBinding? = null
    private var progressDialog: CustomProgressDialog? = null

    override fun onStart() {
        super.onStart()

        if (dialog!!.window != null) {
            dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog!!.window!!.setGravity(Gravity.CENTER)
            dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_post_review, container, false)

        binding!!.closeButton.setOnClickListener {
            dismiss()
        }

        binding!!.buttonPostReview.setOnClickListener {
            postPropertyReview(
                PostReviewDataModel(
                    0,
                    AppInfo.getUserId().toInt(),
                    binding!!.ratingBar.rating.toInt(),
                    propertyID,
                    type,
                    "other",
                    binding!!.edReviewMessage.text.toString()
                )
            )
            agentProfileListener.onAgentClick("success", 1)
            dismiss()
        }
        return binding!!.root
    }


    private fun postPropertyReview(dataModel: PostReviewDataModel) {
        val postPropertyStep1Response =
            RegisterRepository().post_AddUpdateReview(dataModel)
        postPropertyStep1Response.observe(viewLifecycleOwner) {

            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    if (it.toString() != "null") {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                            agentProfileListener.onAgentClick("success", 1)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    class PostReviewDataModel(
        id: Int,
        userid: Int,
        star: Int,
        longid: String,
        type: String,
        userprofile: String,
        reviewmsg: String,
    ) {
        private val id: Int = id
        private val userid: Int = userid
        private val star: Int = star
        private val longid: String = longid
        private val type: String = type
        private val userprofile: String = userprofile
        private val reviewmsg: String = reviewmsg
    }
}