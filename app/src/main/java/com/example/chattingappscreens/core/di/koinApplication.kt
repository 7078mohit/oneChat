package com.example.chattingappscreens.core.di

import android.app.Application
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
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
import java.lang.IllegalArgumentException

class MyApplication() : Application() {
    private var currentOnlineManager: OnlineStatusManager? = null

    companion object {
        private var instance: MyApplication? = null
        fun getInstance(): MyApplication {
            return instance ?: throw IllegalArgumentException("MyApplication not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
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

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (currentUserUid.isNotEmpty()) {
            startOnlineStatus(currentUserUid)
        }
    }

//
//        val imageLoader = ImageLoader.Builder(this)
//            .memoryCache {
//                MemoryCache.Builder(this)
//                    .maxSizePercent(0.25) // 25% of app memory
//                    .build()
//            }
//            .diskCache {
//                DiskCache.Builder()
//                    .directory(cacheDir.resolve("image_cache"))
//                    .maxSizeBytes(512*1024*1024) // 512MB
//                    .build()
//            }.respectCacheHeaders(false)
//            .build()
//        Coil.setImageLoader(imageLoader)

        fun startOnlineStatus(uid: String) {
            currentOnlineManager?.stop()
            currentOnlineManager = null

            if (uid.isNotEmpty()) {
                val firebaseDatabase: FirebaseDatabase by inject()
                currentOnlineManager = OnlineStatusManager(firebaseDatabase, uid)
                currentOnlineManager?.start()
            }
        }

        fun stopOnlineStatus() {
            currentOnlineManager?.stop()
            currentOnlineManager = null
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