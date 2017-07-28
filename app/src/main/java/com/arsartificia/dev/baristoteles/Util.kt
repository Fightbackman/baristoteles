package com.arsartificia.dev.baristoteles

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.text_fragment.view.*

object Util {
    fun registerCircularReveal(context: Context, view: View, arguments: Bundle) {
        AnimationUtils.registerCircularRevealAnimation(
                context,
                view,
                arguments.getInt("centerX"),
                arguments.getInt("centerY"),
                arguments.getInt("width"),
                arguments.getInt("height"),
                context.getColor(R.color.colorPrimary),
                context.getColor(R.color.colorAccent))
    }

    fun  initializeFragment(activity: Activity, context: Context, view: View, fragmentManager: FragmentManager, backIcon: Boolean, hideInput: Boolean) {
        val imageView : ImageView = view.findViewById(R.id.closeDialogImg)
        imageView.setOnClickListener({ fragmentManager.popBackStack() })
        if (backIcon){
            imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp)
        } else {
            imageView.setImageResource(R.drawable.ic_close_black_24dp)
        }

        if (hideInput) {
            hideKeyboard(activity, view)
        } else {
            showKeyboard(500, view, context)
        }
    }

    fun showKeyboard(delay: Long, view:View, context: Context) {
        val handler = Handler()
        handler.postDelayed(Runnable {
            view.mainEditText.requestFocus()
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view.mainEditText, InputMethodManager.SHOW_IMPLICIT)
        }, delay)
    }

    fun hideKeyboard(activity: Activity, view:View ) {
        //Hide Keyboard
        view.postDelayed({
            val keyboard = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(view.windowToken, 0)
        }, 50)
    }

    fun transitionFragment(fragmentManager: FragmentManager, newFragment: Fragment, name: String, animationCenter: View, view: View) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        val args = Bundle()
        args.putInt("centerX", (animationCenter.x+animationCenter.width/2).toInt())
        args.putInt("centerY", (animationCenter.y+animationCenter.height/2).toInt())
        args.putInt("width", view.width)
        args.putInt("height", view.height)
        newFragment.arguments = args
        fragmentTransaction.replace(R.id.fragment_container, newFragment)
        fragmentTransaction.addToBackStack(name)
        fragmentTransaction.commit()
    }
}