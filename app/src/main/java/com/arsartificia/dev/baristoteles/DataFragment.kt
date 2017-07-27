package com.arsartificia.dev.baristoteles

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.app.*
import android.view.*
import kotlinx.android.synthetic.main.data_fragment.*

class DataFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.data_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        //Hide Keyboard
        view.postDelayed({
            val keyboard = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(view.windowToken, 0)
        }, 50)

        fab.setOnClickListener { view ->
            val fragmentTransaction = fragmentManager.beginTransaction()
            val nameFragment = NameFragment()
            fragmentTransaction.add(R.id.fragment_container, nameFragment)
            fragmentTransaction.addToBackStack("NameFragment")
            fragmentTransaction.commit()
        }
    }
}