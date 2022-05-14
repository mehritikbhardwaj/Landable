package com.landable.app.ui.home.postProjectProperty.project

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.databinding.FragmentProjectConfigurationBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.ProjectDetailDataModel
import com.landable.app.ui.home.dataModels.Projectconfiguration
import com.landable.app.ui.home.project.adapter.ConfigurationAdapter
import java.io.File

class PostProjectConfigurationFragment : Fragment(),
    UploadImageDialogFragment.IUploadImageListener, ConfigurationImageClickListener {

    private lateinit var binding: FragmentProjectConfigurationBinding
    private var progressDialog: CustomProgressDialog? = null
    private var projectID: String = ""
    private var _id: Int = 0
    private var uri: Uri? = null
    private var projectData: ProjectDetailDataModel? = null
    private var isComingForEdit: Boolean = false

    companion object {
        fun newInstance() = PostProjectConfigurationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectID = requireArguments().getString("projectID")!!
        _id = requireArguments().getInt("id")
        isComingForEdit = requireArguments().getBoolean("isComingForEdit")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Add Configuration")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_project_configuration,
                container,
                false
            )


        if (isComingForEdit) {
            projectData =
                requireArguments().getSerializable("projectDataModel") as ProjectDetailDataModel

            if (projectData!!.projectconfiguration.size != 0) {
                binding.configurationHEader.visibility = View.VISIBLE
                binding.rvConfiguration.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvConfiguration.adapter =
                    ConfigurationAdapter(projectData!!.projectconfiguration, this)

            }
        }

        binding.edPrice.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(binding.edPrice.text.toString().isEmpty()){
                    binding.tvPriceIndicator.text = "\u20B9 0"
                }else setPriceText(binding.edPrice.text.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                // set oid value now

            }
        })

        binding.uploadImage.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment =
                UploadImageDialogFragment(this, "uploadOneImage")
            dialogFragment.show(fm, "")
        }

        binding.buttonAdd.setOnClickListener {
            if (uri == null) {
                CustomAlertDialog(requireContext(), "Alert", "Please Select Image").show()
            } else {
                val filePath = if ("content" == uri!!.scheme) {
                    Log.e("contains", "getUri.getScheme()")
                    ClsGlobal.getPathFromUri(context, uri)
                } else {
                    uri!!.path
                }
                progressDialog = CustomProgressDialog(requireContext())
                progressDialog!!.show()

                UploadImage(
                    "AddConfiguration",
                    File(filePath),
                    this, "", _id, projectID, "", "",
                    binding.edType.text.toString(),
                    binding.edSize.text.toString(),
                    binding.edPrice.text.toString()
                )
            }

        }

        binding.buttonSubmit.setOnClickListener {
            loadPostProjectMediaUpload(_id, projectID)
        }

        return binding.root
    }

    private fun setPriceText(priceInWords:String){
        val first = priceInWords[0]

        when {
            priceInWords.length<4 -> {
                binding.tvPriceIndicator.text = "\u20B9 $priceInWords"
            }
            priceInWords.length==4 -> {
                val second = priceInWords[1]
                binding.tvPriceIndicator.text = "\u20B9 $first.$second k"
            }
            priceInWords.length==5 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first$second.$third k"
            }
            priceInWords.length==6 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first.$second$third L"
            }priceInWords.length==7 -> {
            val second = priceInWords[1]
            val third = priceInWords[2]
            binding.tvPriceIndicator.text = "\u20B9 $first$second.$third L"
        }
            priceInWords.length==8 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first.$second$third Cr"
            }
            priceInWords.length==9 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                binding.tvPriceIndicator.text = "\u20B9 $first$second.$third Cr"
            }
            priceInWords.length==10 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                val fourth = priceInWords[3]
                binding.tvPriceIndicator.text = "\u20B9 $first$second$third.$fourth Cr"
            }
            priceInWords.length==11 -> {
                val second = priceInWords[1]
                val third = priceInWords[2]
                val fourth = priceInWords[3]
                val fifth = priceInWords[4]
                binding.tvPriceIndicator.text = "\u20B9 $first$second$third$fourth.$fifth Cr"
            }
        }
    }


    private fun updateSelectedImageBitmap(uri: Uri) {
        this.uri = uri
    }

    private fun loadPostProjectMediaUpload(id: Int, projectid: String) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("projectID", projectid)
        bundle.putString("comingFromMedia", "")
        val postProjectConfigurationFragment = ProjectUploadDocumentsFragment.newInstance()
        postProjectConfigurationFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            postProjectConfigurationFragment,
            ProjectUploadDocumentsFragment::class.java.name

        )
    }


    override fun onUploadImageDialogButtonClick(
        isClickedCameraButton: Boolean,
        selectedPhotoList: ArrayList<String>,
        action: String
    ) {
        updateSelectedImageBitmap(Uri.parse(selectedPhotoList[0]))
        binding.unitimageconfig.visibility = View.VISIBLE
        binding.unitimageconfig.load(Uri.parse(selectedPhotoList[0]))
        binding.uploadImage.text = "1 selected"
    }

    override fun onProgressUpdate(percentage: Int) {
        progressDialog!!.updateProgress(percentage)
    }

    override fun onError(errorMessage: String) {
        progressDialog!!.cancelProgress()
        CustomAlertDialog(requireContext(), "Alert", "$errorMessage Please try again").show()
    }

    override fun onFinish(response: String) {
        progressDialog!!.cancelProgress()
        Toast.makeText(context, "Configuration added successfully...", Toast.LENGTH_LONG).show()
    }

    override fun uploadStart() {
        progressDialog!!.updateProgress(0)
    }

    override fun onImageClick(action: String, configuration: Projectconfiguration?) {
    }


}