package com.discount.presenters

import android.content.Context
import android.util.Patterns
import com.discount.R
import com.discount.app.config.Constants
import com.discount.app.prefs.PrefHelper
import com.discount.app.utils.RememberLestener
import com.discount.interactors.SignInInteractor
import com.discount.views.DiscountView
import com.discount.views.ui.activities.ForgotPasswordActivity
import com.discount.views.ui.activities.HomeActivity
import com.discount.views.ui.activities.SignUpActivity

/**
 * Created by Avinash Kumar on 11/1/19.
 * At: Mindiii Systems Pvt. Ltd.
 * Mail: avinash.mindiii@gmail.com
 */
class SignInPresenter(var mDiscountView: DiscountView?, var mInteractor: SignInInteractor,
                      var listenet: RememberLestener): SignInInteractor.OnLoginFinishedListener {
    val TAG = SignInPresenter::class.java.simpleName


    override fun onError(msg: String) {
        mDiscountView?.let {
            it.progress(false)
            it.onErrorOrInvalid(msg)
        }
    }

    override fun onSuccess(msg: String) {
        mDiscountView?.let {
            it.progress(false)
            it.onSuccess(msg)
            it.navigateTo(HomeActivity::class.java)
        }
    }

    fun onDestroy() {
        mDiscountView = null
    }

    fun goToSignUp() {
        mDiscountView?.navigateTo(SignUpActivity::class.java)
    }

    fun goToForgotPassword() {
        mDiscountView?.navigateTo(ForgotPasswordActivity::class.java)
    }

    fun validate(email: String, password: String) {
        mDiscountView?.let {
            if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                it.onErrorOrInvalid((it as Context).resources.getString(R.string.invalid_email_id))
                return
            }

            if(password.isEmpty() || password.length < 6) {
                it.onErrorOrInvalid((it as Context).resources.getString(R.string.invalid_password))
                return
            }

            mInteractor.login(email = email,password = password,mListener = this)
            it.progress(true)
        }

    }

    fun setRememberMe(isRememberMe: Boolean) {
        PrefHelper.instance?.savePref(Constants.REMEMBER_ME,isRememberMe)
    }

    fun checkRemembered() {
        PrefHelper.instance?.run {
            if (getPref(Constants.REMEMBER_ME,false)) {
                val email = getPref(Constants.EMAIL,"")
                val password = getPref(Constants.PASSWORD,"")
                listenet.onRemember(email,password)
            }
        }
    }
}