package com.landable.app.data.network

import com.landable.app.common.LandableConstants
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
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MyApi {

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.LoginUser)
    fun loginUser(
        @Body raw: LoginViewModel.LoginUserRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.LoginwithOTP)
    fun loginwithOTP(
        @Body raw: OTPLoginFragment.OTPDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_loginOTPVerification)
    fun post_loginOTPVerification(
        @Body raw: OTPLoginFragment.VerifyOtpDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Register)
    fun registerIndividualUser(
        @Body raw: IndividualSignUpViewModel.RegisterUserRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Register)
    fun registerAgencyUser(
        @Body raw: AgencySignUpViewModel.RegisterUserRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.SignupOTPVerification)
    fun verifyOtp(
        @Body raw: VerifyOtpViewModel.VerifyOtpRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.ResendSignupOTP)
    fun resendOTP(
        @Body raw: VerifyOtpFragment.ResendOtpRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.UserProfile)
    fun getUserProfileData(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetMyAgents)
    fun getAgentsListData(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_Addtofavourite)
    fun addToFavourite(
        @Body raw: PropertyDetailFragment.AddtoFavouriteDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Get_Dashboard)
    fun getDashboardInfo(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getnotification)
    fun getNotification(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Get_FilterMaster)
    fun getFilterMaster(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetMylisting)
    fun getMyListingData(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getmyleads)
    fun getMyLeads(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getpropertydetails)
    fun getPropertyDetails(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int,
        @Query("propertyid") propertyid: String,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getprojectdetails)
    fun getProjectDetails(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int,
        @Query("projectid") projectid: String,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetUserdetails)
    fun getUserDetails(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.ForgetPassword)
    fun forgetPassword(
        @HeaderMap headers: Map<String, String>,
        @Query("email") email: String
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.PostPropertysearch)
    fun propertySearch(
        @Body raw: SearchFragment.SearchRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.PostProjectsearch)
    fun projectSearch(
        @Body raw: SearchFragment.SearchRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.PostAuctionsearch)
    fun auctionSearch(
        @Body raw: FragmentAuction.SearchAuctionRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.PostAgencysearch)
    fun agencySearch(
        @Body raw: SearchFragment.SearchRequestDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_AddUpdatePropertystep1)
    fun postPropertyStep1(
        @Body raw: PostPropertyBasicInfoFragment.PostPropertyBasicInfo,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_AddUpdatePropertystep2)
    fun postPropertyStep2(
        @Body raw: PostPropertyLocationFragment.PostPropertyLocationInfo,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_AddUpdatePropertystep4)
    fun postPropertyStep4(
        @Body raw: PostPropertyAdditionalDetailsFragment.PostPropertyAdditionalInfo,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_AddUpdateProjectstep1)
    fun post_AddUpdateProjectstep1(
        @Body raw: PostProjectBasicInfoFragment.PostProjectBasicDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Multipart
    @POST(LandableConstants.Post_AddUpdateProjectConfiguration)
    fun post_AddUpdateProjectConfiguration(
        @HeaderMap headers: Map<String, String>,
        @Part("id") id: Int,
        @Part("projectid") projectid: String,
        @Part("utype") utype: String,
        @Part("usize") usize: String,
        @Part("uprice") uprice: String,
        @Part umage: MultipartBody.Part,
    ): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getauctiondetails)
    fun getAuctionDetails(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getnewslist)
    fun getNewslist(
        @HeaderMap headers: Map<String, String>,
        @Query("pageindex") pageindex: Int,
        @Query("keyword") keyword: String,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Post_Contactowner)
    fun postContactOwner(
        @HeaderMap headers: Map<String, String>,
        @Query("propertyid") propertyid: String,
        @Query("type") type: String,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.PostUserprofileupdate)
    fun postUserProfileUpdate(
        @Body raw: EditProfileFragment.UserProfileUpdate,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_AddupdateAgent)
    fun post_AddupdateAgent(
        @Body raw: AddAgentFragment.AddUpdateAgent,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_Addsavedsearch)
    fun post_Addsavedsearch(
        @Body raw: FragmentAuction.SaveSearch,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Multipart
    @POST(LandableConstants.Post_updateuserimage)
    fun post_updateuserimage(
        @HeaderMap headers: Map<String, String>,
        @Part("userid") userid: Int,
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>


    @Multipart
    @POST(LandableConstants.Post_AddUpdatePropertystep3)
    fun postPropertyImage(
        @HeaderMap headers: Map<String, String>,
        @Part("id") id: Int,
        @Part("propertyid") propertyid: String,
        @Part("type") type: String,
        @Part("link") link: String,
        @Part files: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST(LandableConstants.Post_AddProjectMedia)
    fun post_AddProjectMedia(
        @HeaderMap headers: Map<String, String>,
        @Part("id") id: Int,
        @Part("projectid") projectid: String,
        @Part("type") type: String,
        @Part files: MultipartBody.Part,
        @Part("link") link: String,
    ): Call<ResponseBody>

    @Multipart
    @POST(LandableConstants.Post_AddSupergroupMedia)
    fun post_AddSupergroupMedia(
        @HeaderMap headers: Map<String, String>,
        @Part("threadid") threadid: Int,
        @Part("mediatype") mediatype: String,
        @Part files: MultipartBody.Part,
        @Part("link") link: String,
    ): Call<ResponseBody>

    @Multipart
    @POST(LandableConstants.Post_Addchat)
    fun post_Addchat(
        @HeaderMap headers: Map<String, String>,
        @Part("id") id: Int,
        @Part("comment") comment: String,
        @Part("touserid") touserid: Int,
/*
        @Part files: MultipartBody.Part,
*/
        @Part("type") type: String
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getbloglist)
    fun getbloglist(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetMyactivity)
    fun getMyactivity(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getchatbox)
    fun getChatbox(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetMysupergroups)
    fun getMysupergroups(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetSavedsearchlist)
    fun getSavedsearchlist(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getchatuserlist)
    fun getchatuserlist(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int,
        @Query("type") type: String,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getchatcommentlist)
    fun getchatcommentlist(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int,
        @Query("touserid") touserid: Int,
        @Query("type") type: String,
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetDeleteAgent)
    fun getDeleteAgent(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetPropertyforeditByID)
    fun getPropertyforeditByID(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetDeleteShortlink)
    fun getDeleteShortlink(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetDeleteProperty)
    fun getDeleteProperty(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetDeleteProject)
    fun getDeleteProject(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.Getfavouritelist)
    fun getfavouritelist(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET(LandableConstants.GetShortlink)
    fun getShortlink(@HeaderMap headers: Map<String, String>): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_AddUpdateReview)
    fun post_AddUpdateReview(
        @Body raw: PostReviewDialogFragment.PostReviewDataModel,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_AddLocations)
    fun post_AddLocations(
        @Body raw: PropertyDetailFragment.PostAddLocation,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_Addshortlink)
    fun post_Addshortlink(
        @Body raw: ShortUrlFragment.PostShortURL,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_Supergroup)
    fun post_Supergroup(
        @Body raw: AddSuperGroupFragment.PostSuperGroup,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(LandableConstants.Post_Supergroup)
    fun post_WebSupergroup(
        @Body raw: AddSuperGroupWebFragment.PostSuperGroup,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(LandableConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}