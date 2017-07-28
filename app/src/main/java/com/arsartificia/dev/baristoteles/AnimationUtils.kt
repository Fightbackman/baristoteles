
import android.animation.ValueAnimator
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.ViewAnimationUtils
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.annotation.ColorRes
import android.support.annotation.ColorInt
import android.view.View
import com.arsartificia.dev.baristoteles.R


object AnimationUtils {
    interface AnimationFinishedListener {
        fun onAnimationFinished()
    }

    fun getMediumDuration(context: Context): Int {
        return context.getResources().getInteger(android.R.integer.config_mediumAnimTime)
    }

    @ColorInt
    private fun getColor(context: Context, @ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    private fun registerCircularRevealAnimation(context: Context, view: View, cx: Int, cy: Int, width: Int, height: Int, startColor: Int, endColor: Int) {
            view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                    v.removeOnLayoutChangeListener(this)

                    //Simply use the diagonal of the view
                    val finalRadius = Math.sqrt((width * width + height * height).toDouble()).toFloat()
                    val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius)
                    anim.duration = getMediumDuration(context).toLong()
                    anim.interpolator = FastOutSlowInInterpolator()
                    anim.start()
                    startBackgroundColorAnimation(view, startColor, endColor, getMediumDuration(context))
                }
            })
    }

    private fun startCircularRevealExitAnimation(context: Context, view: View, cx: Int, cy: Int, width: Int, height: Int, startColor: Int, endColor: Int) {

            val initRadius = Math.sqrt((width * width + height * height).toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initRadius, 0f)
            anim.duration = getMediumDuration(context).toLong()
            anim.interpolator = FastOutSlowInInterpolator()
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    //Important: This will prevent the view's flashing (visible between the finished animation and the Fragment remove)
                    view.setVisibility(View.GONE)
                }
            })
            anim.start()
            startBackgroundColorAnimation(view, startColor, endColor, getMediumDuration(context))
    }

    private fun startBackgroundColorAnimation(view: View, startColor: Int, endColor: Int, duration: Int) {
        val anim = ValueAnimator()
        anim.setIntValues(startColor, endColor)
        anim.setEvaluator(ArgbEvaluator())
        anim.duration = duration.toLong()
        anim.addUpdateListener { valueAnimator -> view.setBackgroundColor(valueAnimator.animatedValue as Int) }
        anim.start()
    }

    fun registerCreateShareLinkCircularRevealAnimation(context: Context, view: View, cx: Int, cy: Int, width: Int, height: Int) {
        registerCircularRevealAnimation(context, view, cx, cy, width, height, Color.WHITE, getColor(context, R.color.colorPrimaryDark))
    }

    fun startCreateShareLinkCircularRevealExitAnimation(context: Context, view: View, cx: Int, cy: Int, width: Int, height: Int) {
        startCircularRevealExitAnimation(context, view, cx, cy, width, height, getColor(context, R.color.colorAccent), getColor(context, R.color.colorPrimary))
    }
}