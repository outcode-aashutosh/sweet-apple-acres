package app.common.base

import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import app.common.extension.ProgressDialogHelper
import app.common.extension.dismissProgress
import app.common.extension.hideKeyboard
import app.common.extension.showProgress
import app.common.utils.getBinding
import app.sweetappleacres.App
import app.sweetappleacres.data.Prefs
import com.outcode.sweetappleacres.R
import org.koin.android.ext.android.inject


abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {
    lateinit var binding: B
    private lateinit var progressDialog: Dialog
    private val prefs: Prefs by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getBinding()
        setContentView(binding.root)
        progressDialog = ProgressDialogHelper.getProgressDialog(this)
        hideKeyboard()
        App.instance.setUpActivityContext(this)
    }

    open fun resumeInternet() {}

    open fun hideProgress() {
        progressDialog.dismissProgress()
    }

    open fun showProgress(message: String = "") {
        progressDialog.showProgress(message)
    }

    open fun setBackNavigation(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    fun changeStatusBarColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = R.color.white
    }

    fun makeWindowSecure() {
        val window: Window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            val view: View? = currentFocus
            if (view != null) {
                val consumed = super.dispatchTouchEvent(ev)
                val viewNew: View = currentFocus ?: view
                if (viewNew == view) {
                    val rect = Rect()
                    val coordinates = IntArray(2)
                    view.getLocationOnScreen(coordinates)
                    rect.set(
                        coordinates[0],
                        coordinates[1],
                        coordinates[0] + view.width,
                        coordinates[1] + view.height
                    )
                    val x = ev.x.toInt()
                    val y = ev.y.toInt()
                    if (rect.contains(x, y)) {
                        return consumed
                    }
                } else if (viewNew is EditText) {
                    return consumed
                }
                val inputMethodManager: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0)
                viewNew.clearFocus()
                return consumed
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /*  override fun attachBaseContext(newBase: Context?) {
          var context: Context = AppContextWrapper.wrap(newBase, Locale("en"))
          val lang = prefs.languageCode
          if (lang == ENGLISH_EN) {
              val locale = Locale("en")
              context = AppContextWrapper.wrap(newBase, locale)
          } else if (lang == SPANISH_ES) {
              val locale = Locale("es")
              context = AppContextWrapper.wrap(newBase, locale)
          }
          super.attachBaseContext(context)
      }*/

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    fun reLoadActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }

}
