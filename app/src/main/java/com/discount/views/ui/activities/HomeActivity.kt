package com.discount.views.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import com.discount.R
import com.discount.adapters.HomeCouponAdapter
import com.discount.app.Discount
import com.discount.app.utils.DividerItemDecoration
import com.discount.app.utils.MyUtils
import com.discount.app.utils.GridSpacingItemDecoration
import com.discount.interactors.HomeInteractor
import com.discount.presenters.HomePresenter
import com.discount.views.DiscountView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*

/**
 * Created by Avinash Kumar on 15/1/19.
 * At: Mindiii Systems Pvt. Ltd.
 * Mail: avinash.mindiii@gmail.com
 */
class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DiscountView {

    private val mHomePresenter = HomePresenter(this, HomeInteractor())

    override fun onErrorOrInvalid(msg: String) {
        Snackbar.make(alertRootLayoutHome,msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSuccess(msg: String) {
        Snackbar.make(alertRootLayoutHome,msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun progress(flag: Boolean) {
        //TODO: To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> navigateTo(clazz: Class<T>) {
        startActivity(Intent(this,clazz))
        overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbarHome)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbarHome, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val navMenuView = nav_view.getChildAt(0) as NavigationMenuView
        val divider = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.horizontal_divider)!!)
        navMenuView.addItemDecoration(divider)
        nav_view.menu.getItem(0).isChecked = true
        MyUtils.addFontToNavDrawer(nav_view.menu)

        appBarHome.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            var isShow = true
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarHome.title = "Home                   "
                    isShow = true
                } else if(isShow) {
                    collapsingToolbarHome.title = " "
                    isShow = false
                }
            }
        })

        goForCoupon.setOnClickListener { navigateTo(CouponActivity::class.java) }

        rvHomeCouponsHere.layoutManager = GridLayoutManager(this, 2)
        rvHomeCouponsHere.addItemDecoration(GridSpacingItemDecoration(2,Discount.dpToPx(this,5f),true))
        rvHomeCouponsHere.adapter = HomeCouponAdapter(this)

        mHomePresenter.studentDialogStatus()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_coupon -> {
                startActivity(Intent(this,CouponActivity::class.java))
                overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
            }
            R.id.nav_category -> {
                startActivity(Intent(this,CategoryActivity::class.java))
                overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
            }
            R.id.nav_store -> {
                startActivity(Intent(this,StoreActivity::class.java))
                overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
            }
            R.id.nav_my_account -> {
                startActivity(Intent(this,ProfileActivity::class.java))
                overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
            }
            R.id.nav_subscription -> {
                startActivity(Intent(this,SubscriptionActivity::class.java))
                overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
            }
            R.id.nav_about_discount -> {
                startActivity(Intent(this,AboutUsActivity::class.java))
                overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
            }
            R.id.nav_contact_us -> {
                startActivity(Intent(this,ContactUsActivity::class.java))
                overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        mHomePresenter.onDestroy()
        super.onDestroy()
    }
}
