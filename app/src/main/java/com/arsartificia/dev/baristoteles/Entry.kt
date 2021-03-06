package com.arsartificia.dev.baristoteles

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class Entry(val name: String, val grind: String, val time: String, val weight: String, val note: String, val rating: Float) : Serializable {

    private fun writeObject(oos: ObjectOutputStream) {
        return oos.defaultWriteObject()
    }

    private fun readObject(ois: ObjectInputStream) {
        return ois.defaultReadObject()
    }

    private fun readObjectNoData() {
        return
    }
}