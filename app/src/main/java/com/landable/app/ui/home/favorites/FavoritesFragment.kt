package com.landable.app.ui.home.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.landable.app.R
import com.landable.app.data.repositories.RegisterRepository
import com.landable.app.data.responses.ParseResponse
import com.landable.app.databinding.FragmentListingBinding
import com.landable.app.ui.HomeActivity
import com.landable.app.ui.dialog.CustomProgressDialog
import com.landable.app.ui.home.dataModels.FeaturePropertiesDataModel
import com.landable.app.ui.home.dataModels.ListingDataModel
import com.landable.app.ui.home.dataModels.ProjectsDataModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentListingBinding
    private val propertyFragment = FavoritePropertyFragment.newInstance()
    private val projectFragment = FavoriteProjectFragment.newInstance()
    private var propertyList = ArrayList<FeaturePropertiesDataModel>()
    private var projectList = ArrayList<ProjectsDataModel>()
    private var progressDialog: CustomProgressDialog? = null
    private var listingData: ListingDataModel? = null

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Favourites")
        (activity as HomeActivity).hideBottomNavigation()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing, container, false)

        getMyFavorites()

        return binding.root
    }


    private fun getMyFavorites() {
        // Show progress bar
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog!!.show()
        val dashboardResponse = RegisterRepository().getFavoritesList()
        dashboardResponse.observe(viewLifecycleOwner) {
            // hide progress bar
            progressDialog!!.cancelProgress()
            // parse dashboard info
            if (it.toString() != "null") {
                try {
                    listingData = ParseResponse.parseMyListingResponse(it.toString())
                    propertyList = listingData!!.featuredproperties
                    projectList = listingData!!.projects
                    setupViewPager(
                        propertyFragment,
                        projectFragment,
                        binding.pager,
                        binding.tabLayout
                    )

                } catch (
                    e: Exception
                ) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setupViewPager(
        propertyFragment: FavoritePropertyFragment, projectFragment: FavoriteProjectFragment,
        viewPager: ViewPager, tabLayout: TabLayout
    ) {
        val adapter = ViewPagerAdapter(childFragmentManager)

        val bundle = Bundle()
        bundle.putSerializable("propertyList", propertyList)
        propertyFragment.arguments = bundle

        val otherFacesBundle = Bundle()
        otherFacesBundle.putSerializable("projectList", projectList)
        projectFragment.arguments = otherFacesBundle

        adapter.addFragment(propertyFragment)
        adapter.addFragment(projectFragment)


        // setting adapter to view pager.
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    class ViewPagerAdapter// this is a secondary constructor of ViewPagerAdapter class.
        (supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager) {

        // objects of arraylist. One is of Fragment type
        private var fragmentList1: ArrayList<Fragment> = ArrayList()

        // returns which item is selected from arraylist of fragments.
        override fun getItem(position: Int): Fragment {
            return fragmentList1[position]
        }

        // returns which item is selected from arraylist of titles.
        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
            return if (position == 0) {
                "Favourite Property"
            } else {
                "Favourite Project"
            }
        }

        // returns the number of items present in arraylist.
        override fun getCount(): Int {
            return fragmentList1.size
        }

        // this function adds the fragment and title in 2 separate  arraylist.
        fun addFragment(fragment: Fragment) {
            fragmentList1.add(fragment)
        }
    }

}