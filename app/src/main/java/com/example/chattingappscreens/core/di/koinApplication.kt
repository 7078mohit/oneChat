package com.example.chattingappscreens.core.di

import android.app.Application
import com.example.chattingappscreens.core.common.OnlineStatusManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

class MyApplication() : Application() {


    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                onlineStatusManagerModule,
                RepoModules,
                AuthUseCaseModules,
                HomeUseCaseModules,
                viewModelModules,
                preferenceModule,
                firebaseModules,
                ChatUseCaseModules,
                networkObserverModule
            )
        }

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid !=null){

            val onlineStatusManager : OnlineStatusManager by inject { parametersOf(currentUserUid) }

            onlineStatusManager.start()
        }

    }

}


/*

       // update online/offline  status of user
            val ref = FirebaseDatabase.getInstance().reference.child(".info/connected")
            val userRef  = FirebaseDatabase.getInstance().reference.child("User").child(currentUserUid)

            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val online = snapshot.getValue(Boolean::class.java) ?: false
                    if (online){
                        CoroutineScope(Dispatchers.IO).launch {
                          //  updateOnlineStatusUseCase(isOnline = true, uid = currentUserUid)
                         //   preferenceData.onlineLastSeen(isOnline = true)

                            userRef.child("isOnline").setValue(true)
                            userRef.child("lastSeen").setValue(System.currentTimeMillis())

                            userRef.child("isOnline").onDisconnect().setValue(false)
                            userRef.child("lastSeen").onDisconnect().setValue(System.currentTimeMillis())

                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {}

            })
 */




/*
@Inject lateinit var updateOnlineStatusUseCase: UpdateOnlineStatusUseCase

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        CoroutineScope(Dispatchers.IO).launch {
            updateOnlineStatusUseCase(true, uid)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        CoroutineScope(Dispatchers.IO).launch {
            updateOnlineStatusUseCase(false, uid)
        }
    }
}
 */