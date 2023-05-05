package app.common.extension

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.outcode.sweetappleacres.R

fun View.animateView(animTime: Long, startOffset: Long, anim: Int) {
    val anim = AnimationUtils.loadAnimation(context, anim).apply {
        duration = animTime
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(anim)
}

fun Context.showRecyclerAnimation(view: View) {
    val slideDown: Animation =
        AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down)
        view.startAnimation(slideDown)
}