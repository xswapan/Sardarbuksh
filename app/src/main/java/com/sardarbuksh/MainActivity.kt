package com.sardarbuksh

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Rect
import android.os.Handler
import android.widget.EditText
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private val successMessage = "Number Stored Successfully"
    private val errorMessage = "Error Storing Number"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
    }

    private fun initializeViews() {
        mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (mobileNumber.text.isEmpty()) {
                    mobileNumberWrapper?.error = null
                    submit.isEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!validateMobileNumber(mobileNumber.text.toString())) {
                    mobileNumberWrapper?.error = "Invalid Mobile Number"
                    submit.isEnabled = false
                } else {
                    mobileNumberWrapper?.error = null
                    submit.isEnabled = true
                }
            }

        })

        submit.setOnClickListener { view -> onClick(view) }
    }

    private fun onClick(view: View) {
        progressBar.visibility = View.VISIBLE

        val handler = Handler()
        handler.postDelayed({
            if (randomNumberGenerator() in 1..7) {
                handleSuccess(view)
            } else {
                handleFailure(view)
            }
        }, 3000)
    }

    private fun handleSuccess(view: View) {
        mobileNumber.setText("")
        progressBar.visibility = View.GONE
        showSnackbar(view, successMessage)
    }

    private fun handleFailure(view: View) {
        mobileNumberWrapper?.error = "Failed to store"
        progressBar.visibility = View.GONE
        showSnackbar(view, errorMessage)
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show()
    }

    private fun validateMobileNumber(mobileNumber: String): Boolean {
        val regex = Regex(pattern = "^[789]\\d{9}\$")
        return regex.containsMatchIn(input = mobileNumber)
    }

    private fun randomNumberGenerator(): Int {
        val random = Random()
        return random.nextInt(10)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }

}
