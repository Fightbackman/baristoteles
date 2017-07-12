package com.arsartificia.dev.baristoteles

import android.animation.*
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.weight_dialog.view.*


class MainActivity : AppCompatActivity() {

    var weight: Float = 0f
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val weightDialogView = View.inflate(this, R.layout.weight_dialog, null)
            val imageView : ImageView = weightDialogView.findViewById(R.id.closeDialogImg)
            createDialog(view, weightDialogView, imageView, MainActivity::stepWeight)
        }
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val input = ArrayList<String>()
        for (i in 0..99) {
            input.add("Test" + i)
        }
        var adapter = DataAdapter(input)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown)
                    fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
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

    fun createDialog(view: View, dialogView: View, imageView: ImageView, stepFunc: (MainActivity, View, Boolean, Dialog) -> Unit, lastDialog: Dialog? = null) {
        val dialog = Dialog(this, R.style.AddDialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)

        imageView.setOnClickListener({ stepFunc(this, dialogView, false, dialog) })

        dialog.setOnShowListener({ stepFunc(this, dialogView, true, dialog) })

        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, i, _ ->
            if (i == KeyEvent.KEYCODE_BACK) {
                stepFunc(this, dialogView, false, dialog)
                return@OnKeyListener true
            }

            false
        })


        dialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        dialog.show()

        if (lastDialog != null) {
            val handler = Handler()
            handler.postDelayed({
                lastDialog.dismiss()
            }, 400)
        }
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

    fun stepWeight(mainView: View, b: Boolean, dialog: Dialog) {
        val dialogWeight : View = mainView.findViewById(R.id.weight_dialog)
        //Hide Keyboard
        dialogWeight.postDelayed({
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(dialogWeight.windowToken, 0)
        }, 50)

        dialogWeight.touchables.filterIsInstance<Button>().forEach { TextViewCompat.setAutoSizeTextTypeWithDefaults(it, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM) }

        dialogWeight.buttonNine.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("9") }) }
        dialogWeight.buttonEight.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("8") }) }
        dialogWeight.buttonSeven.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("7") }) }
        dialogWeight.buttonSix.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("6") }) }
        dialogWeight.buttonFive.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("5") }) }
        dialogWeight.buttonFour.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("4") }) }
        dialogWeight.buttonThree.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("3") }) }
        dialogWeight.buttonTwo.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("2") }) }
        dialogWeight.buttonOne.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("1") }) }
        dialogWeight.buttonZero.setOnClickListener { _ -> changeEditText(dialogWeight, { it.plus("0") }) }
        dialogWeight.buttonDelete.setOnClickListener { _ -> changeEditText(dialogWeight, { it.dropLast(1) }) }
        dialogWeight.buttonDot.setOnClickListener { _ -> changeEditText(dialogWeight, { txt ->
            if (!txt.contains('.')) {
                txt.plus(".")
            }
            else {
                txt
            }
        }) }
        dialogWeight.buttonNext.setOnClickListener { view ->
            try {
                weight = dialogWeight.editText.text.toString().dropLast(2).toFloat()
                val timeDialogView = View.inflate(this, R.layout.time_dialog, null)
                val imageView : ImageView = timeDialogView.findViewById(R.id.closeDialogImg)
                createDialog(view, timeDialogView, imageView, MainActivity::stepTime, dialog)
            } catch (error: NumberFormatException) {
                Snackbar.make(dialogWeight, "Please enter a proper number", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
            } catch (error: Exception) {
                Snackbar.make(dialogWeight, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }

        revealAnimation(mainView, b, dialog, fab)
    }

    private fun revealAnimation(mainView: View, b: Boolean, dialog: Dialog, center: View) {
        val w = mainView.width
        val h = mainView.height

        val endRadius = Math.hypot(w.toDouble(), h.toDouble()).toFloat()

        val cx: Int = (center.x + center.width / 2).toInt()
        val cy: Int = center.y.toInt() + center.height + 56


        if (b) {
            val revealAnimator = ViewAnimationUtils.createCircularReveal(mainView, cx, cy, 0.0f, endRadius)

            mainView.visibility = View.VISIBLE
            revealAnimator.duration = 400
            revealAnimator.start()

        } else {

            val anim = ViewAnimationUtils.createCircularReveal(mainView, cx, cy, endRadius, 0.0f)

            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    dialog.dismiss()
                    mainView.visibility =  View.INVISIBLE
                }
            })
            anim.duration = 400
            anim.start()
        }

    }

    fun stepTime(mainView: View, b: Boolean, dialog: Dialog) {
        val dialogTime : View = mainView.findViewById(R.id.time_dialog)
        when (b) {
            true -> revealAnimation(mainView, b, dialog, dialogTime)
            false -> revealAnimation(mainView, b, dialog, fab)
        }


    }
}
