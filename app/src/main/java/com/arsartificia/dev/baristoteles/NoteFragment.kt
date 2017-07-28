package com.arsartificia.dev.baristoteles

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.app.*
import android.content.Context
import android.graphics.Color
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.text_fragment.view.*

class NoteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.text_fragment, container, false)
        Util.registerCircularReveal(context, view, arguments)
        return view
    }

    override fun onStart() {
        super.onStart()

        Util.initializeFragment(activity, context, view, fragmentManager, true, true)

        view.infoTextView.text = "Notes:"
        view.mainEditText.setBackgroundColor(Color.WHITE)
        view.mainEditText.setHorizontallyScrolling(true)
        view.mainEditText.setLines(5)
        view.mainEditText.setSingleLine(false)
        view.mainEditText.textSize = view.mainEditText.textSize/3
        view.buttonNext.setOnClickListener {
            try {
                val ma : MainActivity = activity as MainActivity
                ma.note = view.mainEditText.text.toString()
                Util.transitionFragment(fragmentManager, RatingFragment(), "RatingFragment", view.buttonNext, view)
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

}
