package com.landable.app.ui.home.blogs

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
import com.landable.app.ui.home.dataModels.BlogDataModel
import com.landable.app.ui.home.dataModels.NewsDataModelItem
import com.landable.app.ui.home.news.NewsFragment

class BlogNewsFragment : Fragment() {

    private lateinit var binding: FragmentListingBinding
    private val blogsFragment = BlogFragment.newInstance()
    private val newsFragment = NewsFragment.newInstance()
    private var progressDialog: CustomProgressDialog? = null
    private var newsListing: ArrayList<NewsDataModelItem>? = null
    private var blogsListing: ArrayList<BlogDataModel>? = null

    companion object {
        fun newInstance() = BlogNewsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as HomeActivity).showTopBar()
        (activity as HomeActivity).enableBackButton("Blogs & News")
        (activity as HomeActivity).hideBottomNavigation()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listing, container, false)

        setupViewPager(
            blogsFragment,
            newsFragment,
            binding.pager,
            binding.tabLayout
        )

        return binding.root
    }


    private fun setupViewPager(
        propertyFragment: BlogFragment, projectFragment: NewsFragment,
        viewPager: ViewPager, tabLayout: TabLayout
    ) {
        val adapter = ViewPagerAdapter(childFragmentManager)

     /*   val bundle = Bundle()
        bundle.putSerializable("newsList", newsListing)
        propertyFragment.arguments = bundle

        val otherFacesBundle = Bundle()
        otherFacesBundle.putSerializable("blogsList", blogsListing)
        projectFragment.arguments = otherFacesBundle*/

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
                "Blogs"
            } else {
                "News"
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