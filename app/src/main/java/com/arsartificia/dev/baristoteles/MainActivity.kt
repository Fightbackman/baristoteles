package com.arsartificia.dev.baristoteles

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class MainActivity : AppCompatActivity() {
    var name: String = ""
    var grind: String = ""
    var time: String = ""
    var weight: String = ""
    var note: String = ""
    var rating: Float = 0.0f
    val filename = "baristoteles.data"
    var data = ArrayList<Entry>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fragmentTransaction = fragmentManager.beginTransaction()
        val dataFragment = DataFragment()
        fragmentTransaction.add(R.id.fragment_container, dataFragment)
        fragmentTransaction.addToBackStack("DataFragment")
        fragmentTransaction.commit()

        loadData()
    }

    override fun onStop() {
        super.onStop()
        writeData()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun addData() {
        data.add(Entry(name, grind, time, weight, note, rating))
        resetTempData()
    }

    fun resetTempData() {
        name = ""
        grind = ""
        time = ""
        weight = ""
        note = ""
        rating = 0.0f
    }

    fun loadData() {
        try {
            val fis = openFileInput(filename)
            val ois = ObjectInputStream(fis)
            @Suppress("UNCHECKED_CAST")
            data = ois.readObject() as ArrayList<Entry>
            ois.close()
        } catch (error: FileNotFoundException) {
            // This is okay. Happens on the first run
        } catch (error: Exception) {
            Snackbar.make(findViewById(R.id.root_layout), error.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun writeData() {
        try {
            val fos = openFileOutput(filename, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(data)
            oos.close()
        } catch (error: Exception) {
            Snackbar.make(findViewById(R.id.root_layout), error.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}
