package com.arsartificia.dev.baristoteles

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    var name: String = ""
    var grind: Int = 0
    var time: Int = 0
    var weight: Float = 0.0f
    var note: String = ""
    var rating: Float = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragmentTransaction = fragmentManager.beginTransaction()
        val dataFragment = DataFragment()
        fragmentTransaction.add(R.id.fragment_container, dataFragment)
        fragmentTransaction.addToBackStack("DataFragment")
        fragmentTransaction.commit()

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}
