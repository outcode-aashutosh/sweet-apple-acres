@file:Suppress("NOTHING_TO_INLINE")

package app.common.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import app.common.utils.OnSingleClickListener
import com.google.android.material.snackbar.Snackbar

inline fun View.snack(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, resId, duration).show()
}

inline fun View.snack(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, text, duration).show()
}

inline fun Fragment.snack(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    view?.snack(resId, duration)
}

fun View.setOnSingleClickListener(l: View.OnClickListener) {
    setOnClickListener(OnSingleClickListener(l))
}

fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}


/*
inline fun Fragment.customErrorSnack(
    resId: String,
    clickListneer: View.OnClickListener? = null,
    icon: Int = R.drawable.ic_error_round,
    color: Int = context?.compatColor(R.color.red)!!
) {
    view?.customActionSnack(
        resId = resId,
        color = color,
        icon = icon,
        clickListneer = clickListneer
    )
}
inline fun View.customErrorSnack(
    resId: String,
    clickListneer: View.OnClickListener? = null,
    icon: Int = R.drawable.ic_error_round,
    color: Int = context?.compatColor(R.color.red)!!
) {
    view?.customActionSnack(
        resId = resId,
        color = color,
        icon = icon,
        clickListneer = clickListneer
    )
}
*/

/*
inline fun Fragment.customSuccessSnack(
    resId: String = getString(R.string.product_save_successfully),
    clickListneer: View.OnClickListener? = null,
    icon: Int = R.drawable.ic_checked,
    color: Int = context?.compatColor(R.color.success_green)!!
) {
    view?.customActionSnack(
        resId = resId,
        color = color,
        icon = icon,
        clickListneer = clickListneer
    )
}
inline fun View.customSuccessSnack(
    resId: String = context.getString(R.string.product_save_successfully),
    clickListneer: View.OnClickListener? = null,
    icon: Int = R.drawable.ic_checked,
    color: Int = context.compatColor(R.color.success_green)!!
) {
    view?.customActionSnack(
        resId = resId,
        color = color,
        icon = icon,
        clickListneer = clickListneer
    )
}
*/

/*inline fun Activity.customErrorSnack(
    resId: String,
    clickListneer: View.OnClickListener? = null,
    color: Int = compatColor(R.color.red),
    icon: Int = R.drawable.ic_error_round

) {
    window.decorView.findViewById<View>(android.R.id.content)
        .customActionSnack(resId = resId, clickListneer = clickListneer, color = color, icon = icon)
}*/

/*
inline fun Activity.customSuccessSnack(
    resId: String = getString(R.string.product_save_successfully),
    clickListener: View.OnClickListener? = null,
    color: Int = compatColor(R.color.success_green),
    icon: Int = R.drawable.ic_checked

) {
    window.decorView.findViewById<View>(android.R.id.content)
        .customActionSnack(resId = resId, clickListneer = clickListener, color = color, icon = icon)
}
*/


inline fun Fragment.snack(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
    view?.snack(text, duration)
}

inline fun Activity.snack(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    window.decorView.findViewById<View>(android.R.id.content).snack(resId, duration)
}

inline fun Activity.snack(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
    //checkForForceLogout(text.toString())
    window.decorView.findViewById<View>(android.R.id.content).snack(text, duration)
}

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast {
    //  checkForForceLogout(text.toString())
    return Toast.makeText(this, text, duration).apply { show() }
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(this, resId, duration).apply { show() }
}

fun Fragment.toast(@StringRes messageId: Int) = context!!.toast(messageId)
fun Fragment.toast(message: String) = context!!.toast(message)

/*fun checkForForceLogout(message:String){
    if(message == Errors.UNAUTHORIZED || message == Errors.NOT_LOGGED_IN){
        App.instance.forceLogout(false)
    }}*/

