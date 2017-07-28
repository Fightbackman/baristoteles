package com.arsartificia.dev.baristoteles

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.app.*
import android.view.*
import kotlinx.android.synthetic.main.text_fragment.view.*
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter



class NameFragment : Fragment() {
    lateinit var ma: MainActivity

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.text_fragment, container, false)
        Util.registerCircularReveal(context, view, arguments)
        ma = activity as MainActivity
        return view
    }

    override fun onStart() {
        super.onStart()

        Util.initializeFragment(activity, context, view, fragmentManager, false, false)

        view.infoTextView.text = "Name:"
        view.mainEditText.setHorizontallyScrolling(false)
        view.mainEditText.setLines(5)

        val names = ArrayList<String>()
        for (d in ma.data) {
            names.add(d.name)
        }
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, names)
        val textView = view.mainEditText as AutoCompleteTextView
        textView.setAdapter(adapter)


        view.buttonNext.setOnClickListener {
            try {
                if (view.mainEditText.text.isEmpty()) {
                    throw IllegalArgumentException("Please enter a Name")
                }
                val ma : MainActivity = activity as MainActivity
                ma.name = view.mainEditText.text.toString()
                Util.transitionFragment(fragmentManager, GrindFragment(), "GrindFragment", view.buttonNext, view)
            } catch (error: IllegalArgumentException) {
                Snackbar.make(view, "Please Enter a name", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
            } catch (error: Exception) {
                Snackbar.make(view, error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
    }

}
