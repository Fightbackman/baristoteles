package com.arsartificia.dev.baristoteles

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.app.*
import android.content.Context
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.text_fragment.view.*

class NameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.text_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        val imageView : ImageView = view.findViewById(R.id.closeDialogImg)
        imageView.setOnClickListener({ fragmentManager.popBackStack() })

        view.infoTextView.text = "Name:"
        view.mainEditText.requestFocus()
        view.mainEditText.setHorizontallyScrolling(false)
        view.mainEditText.setLines(5)
        val imm : InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view.mainEditText, InputMethodManager.SHOW_IMPLICIT)
        view.buttonNext.setOnClickListener {
            try {
                if (view.mainEditText.text.isEmpty()) {
                    throw IllegalArgumentException("Please enter a Name")
                }
                val ma : MainActivity = activity as MainActivity
                ma.name = view.mainEditText.text.toString()
                val fragmentTransaction = fragmentManager.beginTransaction()
                val grindFragment = GrindFragment()
                fragmentTransaction.add(R.id.fragment_container, grindFragment)
                fragmentTransaction.addToBackStack("GrindFragment")
                fragmentTransaction.commit()
            } catch (error: IllegalArgumentException) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_SHORT)
                        .setAction("Please Enter a Name", null).show()
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

}
