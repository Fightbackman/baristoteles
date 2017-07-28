package com.arsartificia.dev.baristoteles

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.TextViewCompat
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.app.*
import android.view.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.number_fragment.view.*

class GrindFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.number_fragment, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        //Hide Keyboard
        view.postDelayed({
            val keyboard = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(view.windowToken, 0)
        }, 50)

        val imageView : ImageView = view.findViewById(R.id.closeDialogImg)
        imageView.setOnClickListener({ fragmentManager.popBackStack() })



        view.touchables.filterIsInstance<Button>().forEach { TextViewCompat.setAutoSizeTextTypeWithDefaults(it, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM) }

        view.infoTextView.text = "Grind:"
        view.buttonNine.setOnClickListener { _ -> changeEditText(view, { it.plus("9") }) }
        view.buttonEight.setOnClickListener { _ -> changeEditText(view, { it.plus("8") }) }
        view.buttonSeven.setOnClickListener { _ -> changeEditText(view, { it.plus("7") }) }
        view.buttonSix.setOnClickListener { _ -> changeEditText(view, { it.plus("6") }) }
        view.buttonFive.setOnClickListener { _ -> changeEditText(view, { it.plus("5") }) }
        view.buttonFour.setOnClickListener { _ -> changeEditText(view, { it.plus("4") }) }
        view.buttonThree.setOnClickListener { _ -> changeEditText(view, { it.plus("3") }) }
        view.buttonTwo.setOnClickListener { _ -> changeEditText(view, { it.plus("2") }) }
        view.buttonOne.setOnClickListener { _ -> changeEditText(view, { it.plus("1") }) }
        view.buttonZero.setOnClickListener { _ -> changeEditText(view, { it.plus("0") }) }
        view.buttonDelete.setOnClickListener { _ -> changeEditText(view, { it.dropLast(1) }) }
        view.buttonDot.visibility = View.INVISIBLE
        view.buttonNext.setOnClickListener {
            try {
                val ma : MainActivity = activity as MainActivity
                if (view.editText.text.isNotEmpty()) {
                    ma.grind = view.editText.text.toString()
                }
                val fragmentTransaction = fragmentManager.beginTransaction()
                val weightFragment = WeightFragment()
                fragmentTransaction.add(R.id.fragment_container, weightFragment)
                fragmentTransaction.addToBackStack("WeightFragment")
                fragmentTransaction.commit()
            } catch (error: NumberFormatException) {
                Snackbar.make(view, "Please enter a proper number", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

    private fun changeEditText(dialogView: View, exec: (String) -> String) {
        val edit_txt = dialogView.editText
        var txt = edit_txt.text.toString()
        if (txt.length > 6) {
            txt = txt.dropLast(1)
        }
        txt = exec(txt)
        edit_txt.setText(txt)
    }

}