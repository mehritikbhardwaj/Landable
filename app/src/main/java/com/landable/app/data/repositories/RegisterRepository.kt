package com.landable.app.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.landable.app.common.AppInfo
import com.landable.app.common.IUploadImageListener
import com.landable.app.data.network.MyApi
import com.landable.app.data.network.NetworkConnectionInterceptor
import com.landable.app.data.network.SafeApiRequest
import com.landable.app.ui.home.agent.AddAgentFragment
import com.landable.app.ui.home.auction.FragmentAuction
import com.landable.app.ui.home.browser.AddSuperGroupWebFragment
import com.landable.app.ui.home.login.LoginViewModel
import com.landable.app.ui.home.login.OTPLoginFragment
import com.landable.app.ui.home.postProjectProperty.project.PostProjectBasicInfoFragment
import com.landable.app.ui.home.postProjectProperty.property.PostPropertyAdditionalDetailsFragment
import com.landable.app.ui.home.postProjectProperty.property.PostPropertyBasicInfoFragment
import com.landable.app.ui.home.postProjectProperty.property.PostPropertyLocationFragment
import com.landable.app.ui.home.profile.EditProfileFragment
import com.landable.app.ui.home.property.PostReviewDialogFragment
import com.landable.app.ui.home.property.PropertyDetailFragment
import com.landable.app.ui.home.search.SearchFragment
import com.landable.app.ui.home.shortUrl.ShortUrlFragment
import com.landable.app.ui.home.signUp.AgencySignUpViewModel
import com.landable.app.ui.home.signUp.IndividualSignUpViewModel
import com.landable.app.ui.home.supergroups.AddSuperGroupFragment
import com.landable.app.ui.home.verifyOTP.VerifyOtpFragment
import com.landable.app.ui.home.verifyOTP.VerifyOtpViewModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository : SafeApiRequest() {

    fun userLogin(body: LoginViewModel.LoginUserRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).loginUser(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun loginwithOTP(body: OTPLoginFragment.OTPDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).loginwithOTP(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun post_loginOTPVerification(body: OTPLoginFragment.VerifyOtpDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_loginOTPVerification(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun individualUserserRegister(body: IndividualSignUpViewModel.RegisterUserRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).registerIndividualUser(
            body,
            getRegisterUserHeaderMap1()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun agencyUserRegister(body: AgencySignUpViewModel.RegisterUserRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).registerAgencyUser(
            body,
            getRegisterUserHeaderMap1()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun verifyOtp(body: VerifyOtpViewModel.VerifyOtpRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).verifyOtp(
            body,
            getVerifyUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun resendSignupOtp(body: VerifyOtpFragment.ResendOtpRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).resendOTP(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getUserProfileData(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getUserProfileData(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getAgentsListData(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getAgentsListData(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun addToFavorite(body: PropertyDetailFragment.AddtoFavouriteDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).addToFavourite(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getDashboardInfo(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getDashboardInfo(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getNotification(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getNotification(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }


    fun getBlogsList(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getbloglist(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getMyActivity(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getMyactivity(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getChatbox(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getChatbox(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getMySuperGroups(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getMysupergroups(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getSavedsearchlist(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getSavedsearchlist(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getFavoritesList(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getfavouritelist(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getShortlink(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getShortlink(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getFilterMaster(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getFilterMaster(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getMyListingData(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getMyListingData(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getMyLeads(): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getMyLeads(
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()!!.string()
                    } else {
                        apiResponse.value = response.errorBody()!!.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getPropertyDetails(id: Int, propertyid: String): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getPropertyDetails(
            getRegisterUserHeaderMap(),
            id, propertyid
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getProjectDetails(id: Int, projectid: String): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getProjectDetails(
            getRegisterUserHeaderMap(),
            id, projectid
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getUserDetails(id: Int): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getUserDetails(
            getRegisterUserHeaderMap(),
            id
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun forgetPassword(email: String): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).forgetPassword(
            getRegisterUserHeaderMap(),
            email
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getPropertySearch(body: SearchFragment.SearchRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).propertySearch(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getProjectSearch(body: SearchFragment.SearchRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).projectSearch(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getAuctionSearch(body: FragmentAuction.SearchAuctionRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).auctionSearch(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getAgencySearch(body: SearchFragment.SearchRequestDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).agencySearch(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun postPropertyStep1(body: PostPropertyBasicInfoFragment.PostPropertyBasicInfo): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).postPropertyStep1(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }


    fun postPropertyLocationInfo(body: PostPropertyLocationFragment.PostPropertyLocationInfo): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).postPropertyStep2(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun postAddLocationInfo(body: PropertyDetailFragment.PostAddLocation): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_AddLocations(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun postPropertyAdditionalDetailsInfo(body: PostPropertyAdditionalDetailsFragment.PostPropertyAdditionalInfo): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).postPropertyStep4(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }


    fun post_AddUpdateProjectstep1(body: PostProjectBasicInfoFragment.PostProjectBasicDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_AddUpdateProjectstep1(
            body,
            getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun post_AddUpdateProjectConfiguration(
        listener: IUploadImageListener,
        id: Int,
        projectid: String,
        utype: String,
        usize: String,
        uprice: String,
        umage: MultipartBody.Part,
    ) {
        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_AddUpdateProjectConfiguration(
            getRegisterUserHeaderMap(),
            id,
            projectid,
            utype,
            usize,
            uprice,
            umage,
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        listener.onFinish(response.body()!!.string())
                    } else {
                        listener.onError(response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.message?.let { listener.onError(it) }
                }
            })
    }


    fun getAuctionDetails(id: Int): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getAuctionDetails(
            getRegisterUserHeaderMap(),
            id
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getchatuserlist(id: Int, type: String): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getchatuserlist(
            getRegisterUserHeaderMap(),
            id, type
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getchatcommentlist(id: Int, touserid: Int, type: String): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getchatcommentlist(
            getRegisterUserHeaderMap(),
            id, touserid, type
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getDeleteAgent(id: Int): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getDeleteAgent(
            getRegisterUserHeaderMap(),
            id
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }
    fun getPropertyforeditByID(id: Int): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getPropertyforeditByID(
            getRegisterUserHeaderMap(),
            id
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getDeleteShortlink(id: Int): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getDeleteShortlink(
            getRegisterUserHeaderMap(),
            id
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getDeleteProperty(id: Int): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getDeleteProperty(
            getRegisterUserHeaderMap(),
            id
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun getDeleteProject(id: Int): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getDeleteProject(
            getRegisterUserHeaderMap(),
            id
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }


    fun getNewsList(pageIndex: Int, keyword: String): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).getNewslist(
            getRegisterUserHeaderMap(),
            pageIndex, keyword
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }

    fun postContactOwner(propertyid: String, type: String): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).postContactOwner(
            getRegisterUserHeaderMap(),
            propertyid, type
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
        return apiResponse
    }
    fun postUserProfileUpdate(body: EditProfileFragment.UserProfileUpdate): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).postUserProfileUpdate(
            body, getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }
    fun postPropertyImage(
        listener: IUploadImageListener,
        id: Int,
        propertyid: String,
        type: String,
        link: String,
        files: MultipartBody.Part
    ) {
        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).postPropertyImage(
            getRegisterUserHeaderMap(), id, propertyid, type, link, files
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        listener.onFinish(response.body()!!.string())
                    } else {
                        listener.onError(response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.message?.let { listener.onError(it) }
                }
            })
    }

    fun post_updateuserimage(
        listener: IUploadImageListener,
        userid: Int,
        image: MultipartBody.Part
    ) {
        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_updateuserimage(
            getRegisterUserHeaderMap(), userid, image
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        listener.onFinish(response.body()!!.string())
                    } else {
                        listener.onError(response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.message?.let { listener.onError(it) }
                }
            })
    }


    fun post_AddProjectMedia(
        listener: IUploadImageListener,
        id: Int,
        projectid: String,
        type: String,
        files: MultipartBody.Part,
        link: String
    ) {
        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_AddProjectMedia(
            getRegisterUserHeaderMap(), id, projectid, type, files, link
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        listener.onFinish(response.body()!!.string())
                    } else {
                        listener.onError(response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.message?.let { listener.onError(it) }
                }
            })
    }

    fun post_AddSupergroupMedia(
        listener: IUploadImageListener,
        threadid: Int,
        mediatype: String,
        files: MultipartBody.Part,
        link: String
    ) {
        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_AddSupergroupMedia(
            getRegisterUserHeaderMap(), threadid, mediatype, files, link
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        listener.onFinish(response.body()!!.string())
                    } else {
                        listener.onError(response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.message?.let { listener.onError(it) }
                }
            })
    }


    fun post_AddupdateAgent(body: AddAgentFragment.AddUpdateAgent): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_AddupdateAgent(
            body, getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun post_Addsavedsearch(body: FragmentAuction.SaveSearch): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_Addsavedsearch(
            body, getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun post_Addchat(
        id: Int,
        comment: String,
        touserid: Int,
/*
        files: MultipartBody.Part,
*/
        type: String
    ) {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_Addchat(
            getRegisterUserHeaderMap(), id, comment, touserid /*files*/, type
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.body()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })
    }

    fun post_AddUpdateReview(body: PostReviewDialogFragment.PostReviewDataModel): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_AddUpdateReview(
            body, getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun post_Addshortlink(body: ShortUrlFragment.PostShortURL): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_Addshortlink(
            body, getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun post_Supergroup(body: AddSuperGroupFragment.PostSuperGroup): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_Supergroup(
            body, getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    fun post_WebSupergroup(body: AddSuperGroupWebFragment.PostSuperGroup): LiveData<String> {
        val apiResponse = MutableLiveData<String>()

        MyApi(NetworkConnectionInterceptor(AppInfo.getContext())).post_WebSupergroup(
            body, getRegisterUserHeaderMap()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        apiResponse.value = response.body()?.string()
                    } else {
                        apiResponse.value = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    apiResponse.value = t.message
                }
            })

        return apiResponse
    }

    private fun getRegisterUserHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["apiusername"] = "lbus8830082tree"
        headerMap["apipassword"] = "NDU0dDctZmRhajcta2Zkc2ozLWZkYXNr"
        headerMap["uid"] = AppInfo.getUserId() /*"18981"*/
        headerMap["scode"] = AppInfo.getSCode() /*"F0CE309C-0845-4753-8FAC-D5E36E06ECAF"*/
        return headerMap
    }

    private fun getVerifyUserHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["apiusername"] = "lbus8830082tree"
        headerMap["apipassword"] = "NDU0dDctZmRhajcta2Zkc2ozLWZkYXNr"
        headerMap["uid"] = AppInfo.getUserId()
        return headerMap
    }

    private fun getRegisterUserHeaderMap1(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["apiusername"] = "lbus8830082tree"
        headerMap["apipassword"] = "NDU0dDctZmRhajcta2Zkc2ozLWZkYXNr"
        return headerMap
    }
}