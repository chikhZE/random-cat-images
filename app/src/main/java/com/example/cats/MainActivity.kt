package com.example.cats

import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import com.example.cats.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Allow network call on main thread for simplicity (not recommended for production!)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        binding.btn.setOnClickListener {
            fetchCatImage()
        }
    }

    private fun fetchCatImage() {
        val urlString = "https://api.thecatapi.com/v1/images/search"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(stream)
                val imageUrl = jsonArray.getJSONObject(0).getString("url")

                Picasso.get().load(imageUrl).into(binding.catimg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }
}
