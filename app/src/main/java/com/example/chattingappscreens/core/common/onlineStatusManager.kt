package com.example.chattingappscreens.core.common

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnlineStatusManager(
    private val firebaseDatabase: FirebaseDatabase,
    private val uid : String
) : DefaultLifecycleObserver {

    companion object {
        var isAppInForeground = false
    }

    private var started = false
    private val connectedRef = firebaseDatabase.getReference(".info/connected")
    private var listeners = mutableListOf<ValueEventListener>()

    fun start(){
        isAppInForeground = true
        if (started) return
        started = true
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        attachConnectedListener()
    }

    fun stop(){
        listeners.forEach { connectedRef.removeEventListener(it) }
        listeners.clear()
    }


    override fun onStart(owner: LifecycleOwner) {
        isAppInForeground = true
        connect(true)
    }

    override fun onStop(owner: LifecycleOwner) {
        isAppInForeground = false
        connect(false)
    }


    //for on App start and off
    private fun attachConnectedListener(){

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                val isOnline = snapshot.getValue(Boolean::class.java) ?: false

                if (isOnline) {
                    val userRef = firebaseDatabase.getReference("User").child(uid)

                    CoroutineScope(Dispatchers.IO).launch{
                        userRef.child("isOnline").setValue(true)
                        userRef.child("lastSeen").setValue(System.currentTimeMillis())
                        userRef.child("isOnline").onDisconnect().setValue(false)
                        userRef.child("lastSeen").onDisconnect().setValue(System.currentTimeMillis())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        connectedRef.addValueEventListener(listener)
        listeners.add(listener)

    }


    //for manually connect when in the background app or foreground
    fun connect(isOnline : Boolean){
        val userRef = firebaseDatabase.getReference("User").child(uid)
        CoroutineScope(Dispatchers.IO).launch {
            userRef.child("isOnline").setValue(isOnline)
            userRef.child("lastSeen").setValue(System.currentTimeMillis())
        }
    }
}