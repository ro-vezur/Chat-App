package com.example.chatapp.model.apis

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.chatapp.domain.MediaInterface
import javax.inject.Inject

class MediaImpl @Inject constructor(): MediaInterface {
    override fun getImageFromServer() {

    }

    override fun uploadImageToServer(uri: Uri,onSuccess: (imageUrl: String) -> Unit) {

        MediaManager.get()
            .upload(uri)
            .unsigned("images")
            .callback(object : UploadCallback {
            override fun onStart(requestId: String?) {
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
            }

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                (resultData?.get("secure_url") as String).let(onSuccess)
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
            }
        }
        ).dispatch()
    }
}