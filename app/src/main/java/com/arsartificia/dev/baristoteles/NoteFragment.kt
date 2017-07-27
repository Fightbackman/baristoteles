package com.arsartificia.dev.baristoteles

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.app.*
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.text_fragment.view.*

class NoteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.text_fragment, container, false)
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

        view.infoTextView.text = "Notes:"
        view.mainEditText.setBackgroundColor(Color.WHITE)
        view.mainEditText.setHorizontallyScrolling(false)
        view.mainEditText.setLines(5)
        view.mainEditText.setSingleLine(false)
        view.mainEditText.textSize = view.mainEditText.textSize/3
        view.buttonNext.setOnClickListener {
            try {
                val ma : MainActivity = activity as MainActivity
                ma.note = view.mainEditText.text.toString()
                val fragmentTransaction = fragmentManager.beginTransaction()
                val ratingFragment = RatingFragment()
                fragmentTransaction.add(R.id.fragment_container, ratingFragment)
                fragmentTransaction.addToBackStack("RatingFragment")
                fragmentTransaction.commit()
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

}
