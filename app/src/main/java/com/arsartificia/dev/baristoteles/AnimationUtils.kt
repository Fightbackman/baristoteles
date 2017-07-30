
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import com.arsartificia.dev.baristoteles.R


object AnimationUtils {
    fun getMediumDuration(context: Context): Int {
        return context.resources.getInteger(android.R.integer.config_mediumAnimTime)
    }

    @ColorInt
    private fun getColor(context: Context, @ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    private fun registerCircularRevealAnimation(context: Context, view: View, cx: Int, cy: Int, width: Int, height: Int, startColor: Int, endColor: Int) {
            view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.removeOnLayoutChangeListener(this)

                        //Simply use the diagonal of the view
                        val finalRadius = Math.sqrt((width * width + height * height).toDouble()).toFloat()
                        val anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius)
                        anim.duration = getMediumDuration(context).toLong()
                        anim.interpolator = FastOutSlowInInterpolator()
                        anim.start()
                        startBackgroundColorAnimation(view, startColor, endColor, getMediumDuration(context))
                    }
                }
            })
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
}