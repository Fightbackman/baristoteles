package com.arsartificia.dev.baristoteles

import android.animation.ValueAnimator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.ViewAnimationUtils
import android.animation.ArgbEvaluator
import android.content.Context
import android.view.View



object AnimationUtils {
    fun registerCircularRevealAnimation(context: Context, view: View, cx: Int, cy: Int, width: Int, height: Int, startColor: Int, endColor: Int) {
        view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                v.removeOnLayoutChangeListener(this)
                val duration = context.resources.getInteger(android.R.integer.config_mediumAnimTime)

                //Simply use the diagonal of the view
                val finalRadius = Math.sqrt((width * width + height * height).toDouble()).toFloat()
                val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius).setDuration(duration.toLong())
                anim.interpolator = FastOutSlowInInterpolator()
                anim.start()
                startColorAnimation(view, startColor, endColor, duration)
            }
        })
    }

    internal fun startColorAnimation(view: View, startColor: Int, endColor: Int, duration: Int) {
        val anim = ValueAnimator()
        anim.setIntValues(startColor, endColor)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator -> view.setBackgroundColor(valueAnimator.animatedValue as Int) }
        anim.duration = duration.toLong()
        anim.start()
    }
}
