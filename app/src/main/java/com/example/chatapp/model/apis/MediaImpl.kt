package com.example.chatapp.model.apis

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.chatapp.domain.MediaInterface
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class MediaImpl @Inject constructor(): MediaInterface {
    override fun getImageFromServer() {

    }

    override suspend fun uploadImageToServer(uri: Uri) = suspendCancellableCoroutine { cont ->

        MediaManager.get()
            .upload(uri)
            .unsigned("images")
            .callback(object : UploadCallback {
            override fun onStart(requestId: String?) {
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
            }

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                (resultData?.get("secure_url") as String).let { imageUrl -> cont.resume(imageUrl) }
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
            }
        }
        ).dispatch()
    }
}