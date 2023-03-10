package com.landable.app.ui.home.postProjectProperty.project

import android.app.Activity
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
import com.landable.app.databinding.FragmentProjectUploadDocumentsBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomAlertDialog
import com.landable.app.ui.dialog.CustomConfirmationDialog
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.SupergroupMasterMediaModel
import com.landable.app.ui.home.dataModels.SupergroupMediaModel
import com.landable.app.ui.home.homeUI.HomeFragment
import com.landable.app.ui.home.postProjectProperty.filterAdapters.SelectedImagesAdapter
import com.landable.app.ui.home.supergroups.AvailableSupergroupMediaAdapter
import java.io.File


class ProjectUploadDocumentsFragment : Fragment(),
    UploadTypeMediaDialogFragment.UploadTypeListener,
    UploadImageDialogFragment.IUploadImageListener,
    CustomConfirmationDialog.ICustomConfirmationDialogListener, AgentProfileListener {

    private lateinit var binding: FragmentProjectUploadDocumentsBinding
    private var progressDialog: CustomProgressDialog? = null
    private var projectID: String = ""
    private var _id: Int = 0
    private var uri: Uri? = null
    private var uploadFileArrayList = ArrayList<String>()
    private var selectedUploadType: String = ""
    private var comingFor: String = ""
    private var count: Int = 0

    private var details: SupergroupMasterMediaModel? = null
    private var dataMedia:ArrayList<SupergroupMediaModel>?=null

    companion object {
        fun newInstance() = ProjectUploadDocumentsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectID = requireArguments().getString("projectID")!!
        _id = requireArguments().getInt("id")
        comingFor = requireArguments().getString("comingFromMedia")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Upload Documents")
        (activity as HomeActivity).hideBottomNavigation()
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_project_upload_documents,
                container,
                false
            )

        if (comingFor == "supergroup") {
            getsupergroupMedia(id)
        }

        binding.uploadType.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val dialogFragment = UploadTypeMediaDialogFragment(this)
            dialogFragment.show(fm, "")
        }

        binding.buttonUpload.setOnClickListener {
            if (selectedUploadType == "Doc") {
                uploadImages("Doc")
            } else uploadImages("")
        }


        return binding.root
    }

    private fun getsupergroupMedia(id: Int) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val userDataResponse = RegisterRepository().getSupergroupMedia(id)
        userDataResponse.observe(viewLifecycleOwner) {
            progressDialog!!.cancel()
            if (it.toString() != "null") {
                try {
                    details = ParseResponse.parseSuperGroupMediaList(it.toString())

                    if(details!!.image.isNotEmpty() || details!!.video.isNotEmpty() || details!!.document.isNotEmpty()){
                        dataMedia = details!!.image
                        dataMedia = details!!.video
                        dataMedia = details!!.document
                        binding.rvAvailableImages.layoutManager =
                            GridLayoutManager(requireContext(), 2)
                        binding.rvAvailableImages.adapter =
                            AvailableSupergroupMediaAdapter(dataMedia!!, this)

                    }
                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun uploadImages(type: String) {
        if (uploadFileArrayList.size == 0) {
            // loadPostPropertyPageFour(_id, propertyId)
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
                if (comingFor == "supergroup") {
                    if (type == "Doc") {
                        UploadImage(
                            "AddSupergroupMedia",
                            File(filePath!!),
                            this,
                            "", _id, "",
                            "Document", "",
                            "", "", ""
                        )
                    } else if (type == "FloorPlan") {
                        UploadImage(
                            "AddSupergroupMedia",
                            File(filePath!!),
                            this,
                            "", _id, "",
                            "Floorplan",
                            "", "", "", ""
                        )
                    } else UploadImage(
                        "AddSupergroupMedia",
                        File(filePath!!),
                        this,
                        "",
                        _id,
                        "",
                        "Image",
                        "",
                        "",
                        "",
                        ""
                    )
                } else {
                    if (type == "Doc") {
                        UploadImage(
                            "ProjectImageUpload",
                            File(filePath!!), this,
                            "", _id, projectID,
                            "Document", "",
                            "", "", ""
                        )
                    } else if (type == "FloorPlan") {
                        UploadImage(
                            "ProjectImageUpload", File(filePath!!), this,
                            "", _id, projectID, "Floorplan", "", "", "", ""
                        )
                    } else {
                        UploadImage(
                            "ProjectImageUpload", File(filePath!!), this,
                            "", _id, projectID, "Image", "", "", "", ""
                        )
                    }
                }

            } else {
                if (comingFor == "supergroup") {
                    val intent = Intent(requireContext(), UploadService::class.java)
                    intent.putStringArrayListExtra("photoList", uploadFileArrayList)
                    intent.putExtra("_id", _id)
                    intent.putExtra("propertyId", "")
                    intent.putExtra("isComingFor", "AddSupergroupMedia")
                    intent.putExtra("type", type)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        requireContext().startForegroundService(intent)
                    } else {
                        requireContext().startService(intent)
                    }
                } else {
                    val intent = Intent(requireContext(), UploadService::class.java)
                    intent.putStringArrayListExtra("photoList", uploadFileArrayList)
                    intent.putExtra("_id", _id)
                    intent.putExtra("propertyId", projectID)
                    intent.putExtra("isComingFor", "ProjectImageUpload")
                    intent.putExtra("type", type)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        requireContext().startForegroundService(intent)
                    } else {
                        requireContext().startService(intent)
                    }
                }

            }
        }
    }

    override fun onClickButtonForUploadType(typeOfUpload: String) {
        when (typeOfUpload) {
            "UploadImage" -> {
                uploadFileArrayList.clear()
                selectedUploadType = "Image"
                val fm = requireActivity().supportFragmentManager
                val dialogFragment =
                    UploadImageDialogFragment(this, LandableConstants.actionForMultipleSelection)
                dialogFragment.show(fm, "")
            }
            "UploadVideo" -> {
                uploadFileArrayList.clear()
                selectedUploadType = "FloorPlan"
                val fm = requireActivity().supportFragmentManager
                val dialogFragment =
                    UploadImageDialogFragment(this, LandableConstants.actionForMultipleSelection)
                dialogFragment.show(fm, "")

            }
            "UploadDocument" -> {
                uploadFileArrayList.clear()
                selectedUploadType = "Doc"
                val videoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
                videoPickerIntent.type = "application/pdf"
                startActivityForResult(videoPickerIntent, LandableConstants.galleryRequestCode)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val arrayList = ArrayList<String>()
            if (requestCode == LandableConstants.galleryRequestCode) {
                uri = data!!.data as Uri
                arrayList.add(uri.toString())
                uploadFileArrayList.add(arrayList[0])
                binding.uploadType.text = uploadFileArrayList.size.toString() + " item Selected"
            } else {
                arrayList.add(uri.toString())
                uploadFileArrayList.add(arrayList[0])
                binding.uploadType.text = uploadFileArrayList.size.toString() + " item Selected"
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onUploadImageDialogButtonClick(
        isClickedCameraButton: Boolean,
        selectedPhotoList: ArrayList<String>,
        action: String
    ) {
        if (selectedPhotoList.size > 0) {
            count = selectedPhotoList.size
            if (selectedPhotoList.size == 1) {
                uploadFileArrayList.add(selectedPhotoList[0])
                binding.uploadType.text = selectedPhotoList.size.toString() + " Image Selected"
                binding.rvSelectedImages.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.rvSelectedImages.adapter = SelectedImagesAdapter(selectedPhotoList)
            } else {
                for (i in 0 until selectedPhotoList.size) {
                    uploadFileArrayList.add(selectedPhotoList[i])
                }
                binding.uploadType.text = selectedPhotoList.size.toString() + " Images Selected"
                binding.rvSelectedImages.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.rvSelectedImages.adapter = SelectedImagesAdapter(selectedPhotoList)

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
        //    Toast.makeText(context, "Uploaded successfully...", Toast.LENGTH_LONG).show()
        if (uploadFileArrayList.size == 1) {
            CustomConfirmationDialog(
                requireContext(),
                "Alert",
                "Uploaded successfully...",
                "success",
                true,
                this
            ).show()
        } else {
            Toast.makeText(context, "Uploaded successfully...", Toast.LENGTH_LONG).show()

        }
    }

    override fun uploadStart() {
        progressDialog!!.updateProgress(0)
    }

    override fun onPressedCustomDialogButton(pressedButtonName: String?, action: String?) {
        when (action) {
            "success" -> {
                loadHomeFragment()
            }
        }
    }

    private fun loadHomeFragment() {
        FragmentHelper().replaceFragment(
            requireActivity().supportFragmentManager,
            (activity as HomeActivity).getHomePageContainerId(),
            HomeFragment.newInstance(),
            HomeFragment::class.java.name
        )
    }

    override fun onAgentClick(action: String, id: Int) {
        deleteSupergroupMedia(id,_id)

    }
    private fun deleteSupergroupMedia(id: Int,threadID:Int) {
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val deleteResponse = RegisterRepository().getDeleteSupergroupMedia(threadID,id)
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
                       getsupergroupMedia(threadID)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }



}