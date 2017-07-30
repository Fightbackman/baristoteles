package com.arsartificia.dev.baristoteles

import android.content.Context
import android.os.Bundle
import android.app.*
import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import kotlinx.android.synthetic.main.data_fragment.*
import kotlinx.android.synthetic.main.data_fragment.view.*


class DataFragment : Fragment() {

    lateinit var ma : MainActivity
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DataAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.data_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        Util.hideKeyboard(activity, view)

        fab.setOnClickListener { fab ->
            try {
                Util.transitionFragment(fragmentManager, NameFragment(), "NameFragment", fab, view)
            } catch (error: Exception) {
                println(error)
            }

        }

        ma = activity as MainActivity
        ma.dataFragment = this

        recyclerView = view.recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = DataAdapter(ma.data)
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

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    val builder = AlertDialog.Builder(ma)
                    builder.setMessage("Are you sure to delete?")

                    builder.setPositiveButton("REMOVE", DialogInterface.OnClickListener { _, _ ->
                        adapter.notifyItemRemoved(position)
                        ma.data.removeAt(position)
                        return@OnClickListener
                    }).setNegativeButton("CANCEL", DialogInterface.OnClickListener { _, _->
                        adapter.notifyItemRemoved(position + 1)
                        adapter.notifyItemRangeChanged(position, adapter.itemCount)
                        return@OnClickListener
                    }).show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}