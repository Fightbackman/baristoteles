package com.arsartificia.dev.baristoteles

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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
    lateinit var temp : Entry
    lateinit var args: Bundle

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.data_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ma = activity as MainActivity
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    private fun update() {
        adapter = DataAdapter(ma.data)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        //Hide Keyboard
        view.postDelayed({
            val keyboard = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(view.windowToken, 0)
        }, 50)

        fab.setOnClickListener { fab ->
            val fragmentTransaction = fragmentManager.beginTransaction()
            val nameFragment = NameFragment()
            val args = Bundle()
            args.putInt("centerX", (fab.x+fab.width/2).toInt())
            args.putInt("centerY", (fab.y+fab.height/2).toInt())
            args.putInt("width", view.width)
            args.putInt("height", view.height)
            nameFragment.arguments = args
            fragmentTransaction.replace(R.id.fragment_container, nameFragment)
            fragmentTransaction.addToBackStack("NameFragment")
            fragmentTransaction.commit()
        }

        recyclerView = view.recycler_view
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        update()
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