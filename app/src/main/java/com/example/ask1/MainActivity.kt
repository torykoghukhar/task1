package com.example.ask1

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ask1.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var view: ActivityMainBinding
    private lateinit var imageUri: Uri

    private val launcher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                view.selfie.setImageURI(imageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityMainBinding.inflate(layoutInflater)
        setContentView(view.root)
        imageUri = createImageUri()
        view.take.setOnClickListener {
            launcher.launch(imageUri)
        }
        view.send.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("hodovychenko@op.edu.ua"))
                putExtra(Intent.EXTRA_SUBJECT, "DigiJED Kozhukhar Viktoriia")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Переглянути за посиланням: https://github.com/torykoghukhar/task1"
                )
                putExtra(Intent.EXTRA_STREAM, imageUri)
            }
            startActivity(i)
        }
    }

    private fun createImageUri(): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "selfie_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IOException("Не вдалося створити URI")
    }
}