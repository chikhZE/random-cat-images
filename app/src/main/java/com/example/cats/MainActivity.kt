package com.example.cats

import android.os.Bundle
import android.view.View
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

        binding.btn.setOnClickListener {
            val url = "https://api.thecatapi.com/v1/images/search"
            fetchCatImage(url)
        }
    }

    private fun fetchCatImage(urlString: String) {
            binding.catimg.setImageDrawable(null)
        binding.error.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseText = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(responseText)
                val imageUrl = jsonArray.getJSONObject(0).getString("url")

                runOnUiThread {
                    Picasso.get()
                        .load(imageUrl)
                        .into(binding.catimg, object : com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                binding.progressBar.visibility = View.GONE

                            }

                            override fun onError(e: Exception?) {
                                binding.error.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                            }
                        })
                }

            } catch (e: Exception) {
                runOnUiThread {
                    binding.error.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }.start()
    }
}
