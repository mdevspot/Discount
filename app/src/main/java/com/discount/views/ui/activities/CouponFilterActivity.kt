package com.discount.views.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.discount.R
import com.discount.adapters.CouponFilterAdapter
import com.discount.app.Discount
import com.discount.app.config.Constants
import com.discount.app.utils.GridSpacingItemDecoration
import com.discount.models.Category
import com.discount.presenters.CouponFilterPresenter
import com.discount.views.DiscountView
import kotlinx.android.synthetic.main.activity_coupon_filter.*

class CouponFilterActivity : AppCompatActivity(), DiscountView{
    private val categories = mutableListOf<Category>()
    private var mAdapter: CouponFilterAdapter? = null
    private val mPresenter = CouponFilterPresenter(this)

    override fun onErrorOrInvalid(msg: String) {
        Snackbar.make(alertRootLayoutCouponFilter,msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSuccess(msg: String) {
        Snackbar.make(alertRootLayoutCouponFilter,msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun progress(flag: Boolean) {
        progressBar.visibility = if (flag) View.VISIBLE else View.GONE
    }

    override fun <T> navigateTo(clazz: Class<T>, bundle: Bundle?) {
        val mIntent = Intent(this,clazz)
        if (bundle != null) mIntent.putExtra(Constants.KEY_BUNDLE_PARAM,bundle)
        startActivity(mIntent)
        overridePendingTransition(R.anim.init_to_left,R.anim.left_to_init)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_filter)
        rvCategory.layoutManager = GridLayoutManager(this,2)
        rvCategory.addItemDecoration(GridSpacingItemDecoration(2,Discount.dpToPx(this,5f),true))
        mAdapter = CouponFilterAdapter(this,categories)
        rvCategory.adapter = mAdapter

        mPresenter.getCategoryList()
        ivGoToBack.setOnClickListener { finish() }
        tvBtnDone.setOnClickListener { applyFilter() }
    }

    fun onSuccessCategoryList(categories: MutableList<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        mAdapter?.notifyDataSetChanged()

        if (this.categories.size == 0) {
            tvNoDataAlert.visibility = View.VISIBLE
        } else {
            tvNoDataAlert.visibility = View.GONE
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.right_to_init,R.anim.init_to_right)
    }

    private fun applyFilter() {
        if(categories.size > 0) {
            var selectedCategories: String? = ""
            for (category in categories) {
                if (category.isSelected) {
                    selectedCategories+="${category.categoryName},"
                }
            }
            val mIntent = Intent()
            mIntent.putExtra(Constants.KEY_RESULT_EXTRA,selectedCategories)
            setResult(Activity.RESULT_OK,mIntent)
        }
        finish()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }
}
