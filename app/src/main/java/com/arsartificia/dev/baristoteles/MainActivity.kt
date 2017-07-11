package com.arsartificia.dev.baristoteles

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.drawable.ColorDrawable
import android.content.DialogInterface
import android.view.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.add_dialog.view.*
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.R.attr.x
import android.graphics.Point
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            createNewEntry(view)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun createNewEntry(view: View) {
        val dialogView = View.inflate(this, R.layout.add_dialog, null)

        val dialog = Dialog(this, R.style.AddDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)

        val imageView : ImageView = dialog.findViewById(R.id.closeDialogImg)
        imageView.setOnClickListener(View.OnClickListener { revealShow(dialogView, false, dialog) })

        dialog.setOnShowListener(DialogInterface.OnShowListener { revealShow(dialogView, true, dialog) })

        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, i, _ ->
            if (i == KeyEvent.KEYCODE_BACK) {
                revealShow(dialogView, false, dialog)
                return@OnKeyListener true
            }

            false
        })


        dialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        dialog.show()
        //Snackbar.make(view, "Added new Coffee !", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show()
    }

    private fun changeEditText(dialogView: View, exec: (String) -> String) {
        val edit_txt = dialogView.editText
        var txt = edit_txt.text.toString()
        if (txt.contains('g')) {
            txt = txt.dropLast(2)
        }
        txt = exec(txt)
        if (txt.length > 6) {
            txt = txt.dropLast(1)
        }
        if (txt.isNotEmpty()) {
            txt = txt.plus(" g")
        }
        edit_txt.setText(txt)
    }

    private fun revealShow(mainView: View, b: Boolean, dialog: Dialog) {
        val dialogView : View = mainView.findViewById(R.id.add_dialog)
        //Hide Keyboard
        dialogView.postDelayed(Runnable {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(dialogView.windowToken, 0)
        }, 50)

        //Set Proper fontsize
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        val percent = .05f
        dialogView.touchables.filterIsInstance<Button>().forEach { it: Button -> it.textSize = percent * size.x }

        dialogView.buttonNine.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("9") }) }
        dialogView.buttonEight.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("8") }) }
        dialogView.buttonSeven.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("7") }) }
        dialogView.buttonSix.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("6") }) }
        dialogView.buttonFive.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("5") }) }
        dialogView.buttonFour.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("4") }) }
        dialogView.buttonThree.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("3") }) }
        dialogView.buttonTwo.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("2") }) }
        dialogView.buttonOne.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("1") }) }
        dialogView.buttonZero.setOnClickListener { _ -> changeEditText(dialogView, { it.plus("0") }) }
        dialogView.buttonDelete.setOnClickListener { _ -> changeEditText(dialogView, { it.dropLast(1) }) }
        dialogView.buttonDot.setOnClickListener { _ -> changeEditText(dialogView, { txt ->
            if (!txt.contains('.')) {
                txt.plus(".")
            }
            else {
                txt
            }
        }) }

        val w = mainView.width
        val h = mainView.height

        val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toFloat()

        val cx: Int = (fab.x + fab.width / 2).toInt()
        val cy: Int = fab.y.toInt() + fab.height + 56


        if (b) {
            val revealAnimator = ViewAnimationUtils.createCircularReveal(mainView, cx, cy, 0.0f, endRadius)

            mainView.visibility = View.VISIBLE
            revealAnimator.duration = 700
            revealAnimator.start()

        } else {

            val anim = ViewAnimationUtils.createCircularReveal(mainView, cx, cy, endRadius, 0.0f)

            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    dialog.dismiss()
                    mainView.setVisibility(View.INVISIBLE)
                }
            })
            anim.duration = 700
            anim.start()
        }

    }
}
