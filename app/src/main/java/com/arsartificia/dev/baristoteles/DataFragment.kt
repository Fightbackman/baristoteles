package com.arsartificia.dev.baristoteles

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.app.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import kotlinx.android.synthetic.main.data_fragment.*
import kotlinx.android.synthetic.main.data_fragment.view.*

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

        val recyclerView = view.recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val input = ArrayList<String>()
        for (i in 0..99) {
            input.add("Test" + i)
        }
        var adapter = DataAdapter(input)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown)
                    fab.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }
}