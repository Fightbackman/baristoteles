package com.arsartificia.dev.baristoteles

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.star_fragment.view.*

class RatingFragment : Fragment() {

    lateinit var ma : MainActivity

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.star_fragment, container, false)
        Util.registerCircularReveal(activity, view, arguments)
        return view
    }

    override fun onStart() {
        super.onStart()

        Util.initializeFragment(activity, view, fragmentManager, true, true)

        ma = activity as MainActivity

        view.infoTextView.text = getString(R.string.rating)
        val summary =
                "Name: ${ma.name}\n" +
                "Grind: ${ma.grind}\n" +
                "Weight: ${ma.weight}\n" +
                "Time: ${ma.time}\n\n" +
                        ma.note
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