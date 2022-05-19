package com.landable.app.ui.home.postProjectProperty.property

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.landable.app.R
import com.landable.app.common.*
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentPostPropertUploadImageBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.PropertyDetailsModel
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.postProjectProperty.filterAdapters.SelectedImagesAdapter
import java.io.File

class PostPropertyUploadImage : Fragment(), UploadImageDialogFragment.IUploadImageListener,
    AgentProfileListener {

    private lateinit var binding: FragmentPostPropertUploadImageBinding
    private var propertyId: String = ""
    private var _id: Int = 0
    private var progressDialog: CustomProgressDialog? = null
    private var uploadFileArrayList = ArrayList<String>()
    private var propertyData: PropertyDetailsModel? = null
    private var isComingForEdit: Boolean = false
    private var isClickedOnExit: Boolean = false

    companion object {
        fun newInstance() = PostPropertyUploadImage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        propertyId = requireArguments().getString("propertyid")!!
        _id = requireArguments().getInt("id")
        isComingForEdit = requireArguments().getBoolean("isComingForEdit")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_post_propert_upload_image,
                container,
                false
            )

        (activity as HomeActivity).postUserTrackingModel(
            HomeActivity.PostUserTrackingModel(
                "Post property Upload image page",
                "Visit",
                "Visit",
                "Visit",
                "",
                ""
            )
        )

        binding.buttonExit.setOnClickListener {
            isClickedOnExit = true
            val fm = requireActivity().supportFragmentManager
            val dialogFragment =
                UploadImageDialogFragment(this, LandableConstants.actionForMultipleSelection)
            dialogFragment.show(fm, "")
        }
        binding.uploadImage.setOnClickListener {
            isClickedOnExit = false
            val fm = requireActivity().supportFragmentManager
            val dialogFragment =
                UploadImageDialogFragment(this, LandableConstants.actionForMultipleSelection)
            dialogFragment.show(fm, "")
        }

        if (isComingForEdit) {
            getPropertyImages(_id, propertyId)
        }

        binding.buttonContact.setOnClickListener {
            uploadImages()
        }

        return binding.root
    }

    private fun loadHomeFragment() {
        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            HomeFragment.newInstance(),
            HomeFragment::class.java.name
        )
    }

    private fun getPropertyImages(id: Int, propertyid: String) {
        val propertyResponse = RegisterRepository().getPropertyDetails(id, propertyid)
        propertyResponse.observe(viewLifecycleOwner) {
            if (it == LandableConstants.noInternetErrorMessage) {
                //print NoInternet Error Message
                CustomAlertDialog(
                    requireContext(),
                    LandableConstants.noInternetErrorTitle,
                    it
                ).show()
            } else {
                try {
                    propertyData = ParseResponse.parsePropertyImagesResponse(it.toString())

                    if (propertyData != null) {
                        binding.tvAvailableImages.visibility = View.VISIBLE
                        binding.rvAvailableImages.layoutManager =
                            GridLayoutManager(requireContext(), 2)
                        binding.rvAvailableImages.adapter =
                            AvailablePropertyImagesAdapter(propertyData!!.propertyimages, this)
                    }
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun uploadImages() {
        if (uploadFileArrayList.size == 0) {
            loadPostPropertyPageFour(_id, propertyId)
        } else if (uploadFileArrayList.size > 0) {
            if (uploadFileArrayList.size == 1) {
                val filePath = if ("content" == Uri.parse(uploadFileArrayList[0]).scheme) {
                    Log.e("contains", "getUri.getScheme()")
                    ClsGlobal.getPathFromUri(context, Uri.parse(uploadFileArrayList[0]))
                } else {
                    Uri.parse(uploadFileArrayList[0]).path
                }
                progressDialog = CustomProgressDialog(requireContext())
                progressDialog!!.show()
                UploadImage(
                    "imageUpload", File(filePath!!), this,
                    "", _id, propertyId, "image", "", "", "", ""
                )
            } else {
             /*   progressDialog = CustomProgressDialog(requireContext())
                progressDialog!!.show()*/
                /* for (i in 0 until uploadFileArrayList.size) {
                     val filePath = if ("content" == Uri.parse(uploadFileArrayList[i]).scheme) {
                         Log.e("contains", "getUri.getScheme()")
                         ClsGlobal.getPathFromUri(context, Uri.parse(uploadFileArrayList[i]))
                     } else {
                         Uri.parse(uploadFileArrayList[i]).path
                     }
                     UploadImage(
                         "imageUpload", File(filePath!!), this,
                         "", _id, propertyId, "image", "", "", "", ""
                     )
                 }*/
                // multiple photo upload
                val intent = Intent(requireContext(), UploadService::class.java)
                intent.putStringArrayListExtra("photoList", uploadFileArrayList)
                intent.putExtra("_id", _id)
                intent.putExtra("propertyId", propertyId)
                intent.putExtra("type", "imageUpload")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        requireContext().startForegroundService(intent)
                    }
                } else {
                    requireContext().startService(intent)
                }
                Toast.makeText(
                    requireContext(),
                    "Image will be uploaded in background.",
                    Toast.LENGTH_LONG
                ).show()
                if (isClickedOnExit) {
                    loadHomeFragment()
                } else loadPostPropertyPageFour(_id, propertyId)

            }
        }
    }

    override fun onUploadImageDialogButtonClick(
        isClickedCameraButton: Boolean,
        selectedPhotoList: ArrayList<String>,
        action: String
    ) {
        if (selectedPhotoList.size > 0) {
            if (selectedPhotoList.size == 1) {
                uploadFileArrayList.add(selectedPhotoList[0])
                binding.uploadImage.text = selectedPhotoList.size.toString() + " Image Selected"
                binding.rvSelectedImages.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.rvSelectedImages.adapter = SelectedImagesAdapter(selectedPhotoList)
            } else {
                binding.uploadImage.text = selectedPhotoList.size.toString() + " Images Selected"
                binding.rvSelectedImages.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.rvSelectedImages.adapter = SelectedImagesAdapter(selectedPhotoList)
                for (i in 0 until selectedPhotoList.size) {
                    uploadFileArrayList.add(selectedPhotoList[i])
                    //   updateSelectedImageBitmap(Uri.parse(selectedPhotoList[i]))
                }
            }
        }
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
        if (uploadFileArrayList.size == 1) {
            if (isClickedOnExit) {
                loadHomeFragment()
            } else loadPostPropertyPageFour(_id, propertyId)
        }
    }

    override fun uploadStart() {
        progressDialog!!.updateProgress(0)
    }

    private fun loadPostPropertyPageFour(id: Int, propertyid: String) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("propertyid", propertyid)
        bundle.putBoolean("isComingForEdit", isComingForEdit)
        val postPropertyAdditionalFragment = PostPropertyAdditionalDetailsFragment.newInstance()
        postPropertyAdditionalFragment.arguments = bundle
        FragmentHelper().replaceFragmentAddToBackstack(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            postPropertyAdditionalFragment,
            PostPropertyAdditionalDetailsFragment::class.java.name

        )
    }

    override fun onAgentClick(action: String, id: Int) {
        deletePropertyImage(id)
    }

    private fun deletePropertyImage(id: Int) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val deleteResponse = RegisterRepository().GetDeletePropertymedia(id)
        deleteResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancelProgress()

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
                        getPropertyImages(_id, propertyId)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

}
