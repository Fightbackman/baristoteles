package com.arsartificia.dev.baristoteles

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.app.*
import android.support.design.widget.Snackbar
import android.view.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.star_fragment.view.*

class RatingFragment : Fragment() {

    lateinit var ma : MainActivity

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.star_fragment, container, false)
        Util.registerCircularReveal(context, view, arguments)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ma = activity as MainActivity
    }

    override fun onStart() {
        super.onStart()

        Util.initializeFragment(activity, context, view, fragmentManager, true, true)

        view.infoTextView.text = "Rating:"
        val summary =
                "Name: ${ma.name}\n" +
                "Grind: ${ma.grind}\n" +
                "Weight: ${ma.weight}\n" +
                "Time: ${ma.time}\n\n" +
                "${ma.note}"
        view.summaryTextView.text = summary

        view.buttonNext.setOnClickListener {
            try {
                val ma : MainActivity = activity as MainActivity
                ma.rating = view.ratingBar.rating
                ma.addData()
                fragmentManager.popBackStack("DataFragment", 0)
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }
}