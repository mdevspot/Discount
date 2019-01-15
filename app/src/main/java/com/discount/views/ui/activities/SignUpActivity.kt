package com.discount.views.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.discount.R
import com.discount.app.config.Constants
import com.discount.interactors.SignUpInteractor
import com.discount.presenters.SignUpPresenter
import com.discount.views.DiscountView
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File

class SignUpActivity : AppCompatActivity(), DiscountView {
    private val TAG = SignUpActivity::class.java.simpleName

    private val mSignUpPresenter = SignUpPresenter(this, SignUpInteractor())

    override fun progress(flag: Boolean) {
        if (flag) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    override fun <T> navigateTo(clazz: Class<T>) {
        /**
         * This method will navigate to
         * */
    }

    override fun onErrorOrInvalid(msg: String) {
        Snackbar.make(signUpActivityRoot,msg,Snackbar.LENGTH_SHORT).show()
    }

    override fun onSuccess(msg: String) {
        Snackbar.make(signUpActivityRoot,msg,Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        ivGoToBack.setOnClickListener { finish() }
        tvGoToSignIn.setOnClickListener { finish() }
        ivProfileImage.setOnClickListener {
            mSignUpPresenter.requestForStorageAccess()
        }
        btnSignUp.setOnClickListener {
            mSignUpPresenter.validate(etFirstName.text.toString(),etLastName.text.toString(),
                etEmailId.text.toString(),etPassword.text.toString(),etConfirmPassword.text.toString())
        }
    }

    override fun onDestroy() {
        mSignUpPresenter.onDestroy()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.right_to_init,R.anim.init_to_right)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mSignUpPresenter.onActivityResult(requestCode,resultCode,data)
        if (requestCode == Constants.PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                llSelectImage.alpha = 0f
            } else {
                llSelectImage.alpha = 1f
                ivProfileImage.setImageURI(null)
            }
        } else if (requestCode == Constants.CAPTURE_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                llSelectImage.alpha = 0f
            } else {
                llSelectImage.alpha = 1f
                ivProfileImage.setImageURI(null)
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                ivProfileImage.setImageURI(UCrop.getOutput(data!!))
            } else {
                ivProfileImage.setImageURI(null)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mSignUpPresenter.showPickOptionsDialog()
            } else {
                mSignUpPresenter.requestForStorageAccess()
            }
        }
    }
}
