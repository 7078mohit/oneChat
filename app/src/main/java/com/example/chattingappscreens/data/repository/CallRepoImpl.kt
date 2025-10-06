package com.example.chattingappscreens.data.repository

import com.example.chattingappscreens.data.modell.CallModel
import com.example.chattingappscreens.data.modell.CallStatus
import com.example.chattingappscreens.domain.repository.CallRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CallRepoImpl(
    private val firebaseDatabase: FirebaseDatabase
) : CallRepository {

    val listeners = mutableMapOf<String, ValueEventListener>()
    val childListeners = mutableMapOf<String , ChildEventListener>()
    val callRef = firebaseDatabase.getReference("calls")


    //sendCall
    override suspend fun sendCallInvite(callModel: CallModel): Result<Unit> {
        return try {
            //generate
         //   val callId = callRef.child("${System.currentTimeMillis()}").push().key ?: UUID.randomUUID().toString()
            val channelName = callRef.child("_channel_${System.currentTimeMillis()}").push().key ?: UUID.randomUUID().toString()

            val finalCall = callModel.copy( channelName = channelName , status = CallStatus.RINGING)
            callRef.child(callModel.callId).setValue(finalCall).await()
            Result.success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    //Incoming Call
    override  fun observeIncomingCall(
        forUid: String,
        onCallReceived: (CallModel?) -> Unit
    ) {
       val ref = callRef.orderByChild("receiverId").equalTo(forUid)
            val listener = object : ChildEventListener {
                override fun onChildAdded(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                        val call = snapshot.getValue(CallModel::class.java)
                        if ( call != null && call.status == CallStatus.RINGING) {
                            onCallReceived(call)
                        }

                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {

                        val call = snapshot.getValue(CallModel::class.java)
                        if (call != null &&  call.status == CallStatus.RINGING) {
                            onCallReceived(call)
                        }
                    }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    print(error.message)
                }
            }

        ref.addChildEventListener(listener)
        childListeners[forUid] = listener

    }



    //IMP/IMP/IMP
    override suspend fun updateCallStatus(
        callId : String,
        status: CallStatus
    ) {
        try {
            callRef.child(callId).child("status").setValue(status).await()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getCallById(callId: String , callModel : (CallModel?) -> Unit ) {
          try {
            val ref = callRef.child(callId)
              val listener = object : ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {
                      val callModel = snapshot.getValue(CallModel::class.java)
                        callModel(callModel)
                   }

                   override fun onCancelled(error: DatabaseError) {
                        callModel(null)
                   }
               }

            ref.addValueEventListener(listener)
              listeners[callId] = listener
        } catch (e: Exception) {
            e.printStackTrace()
            callModel(null)
        }
    }

    override fun getCallByIdRemoveListener(callId: String){
        listeners[callId]?.let {
            callRef.child(callId).removeEventListener(it)
        }
        listeners.remove(callId)
    }

    override fun observeIncomingCallRemoveListener(forUid: String) {
        childListeners[forUid]?.let{
            callRef.orderByChild("receiverId").equalTo(forUid).removeEventListener(it)
        }
        childListeners.remove(forUid)
    }

    override suspend fun updateStartAndEndTime(callId: String, timeMode: String) {
        try {
            callRef.child(callId).child(timeMode).setValue(System.currentTimeMillis()).await()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}