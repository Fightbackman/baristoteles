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
        return inflater!!.inflate(R.layout.star_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ma = activity as MainActivity
    }

    override fun onStart() {
        super.onStart()

        //Hide Keyboard
        view.postDelayed({
            val keyboard = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(view.windowToken, 0)
        }, 50)

        view.infoTextView.text = "Rating:"
        val summary =
                "Name: ${ma.name}\n" +
                "Grind: ${ma.grind}\n" +
                "Weight: ${ma.weight}\n" +
                "Time: ${ma.time}\n\n" +
                "${ma.note}"
        view.summaryTextView.text = summary

        val imageView : ImageView = view.findViewById(R.id.closeDialogImg)
        imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp)
        imageView.setOnClickListener({ fragmentManager.popBackStack() })

        view.buttonNext.setOnClickListener {
            try {
                val ma : MainActivity = activity as MainActivity
                ma.rating = view.ratingBar.rating
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                val fragmentTransaction = fragmentManager.beginTransaction()
                val dataFragment = DataFragment()
                fragmentTransaction.add(R.id.fragment_container, dataFragment)
                fragmentTransaction.commit()
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }
}