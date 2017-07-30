package com.arsartificia.dev.baristoteles

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.text_fragment.view.*

class NoteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.text_fragment, container, false)
        Util.registerCircularReveal(activity, view, arguments)
        return view
    }

    override fun onStart() {
        super.onStart()

        Util.initializeFragment(activity, view, fragmentManager, true, true)

        view.infoTextView.text = getString(R.string.notes)
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
