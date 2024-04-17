package com.example.androidhw18

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.androidhw18.Data.App
import com.example.androidhw18.Data.Sight
import com.example.androidhw18.Data.SightDao
import com.example.androidhw18.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val values = mutableListOf<Sight>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel:MainViewModel by viewModels{
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val sightDao: SightDao = (application as App).db.sightDao()
                    return MainViewModel(sightDao) as T
                }
            }
        }

        binding.button.setOnClickListener {
            val intent = Intent(this, MainActivity2()::class.java)
            startActivity(intent)
        }



        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.allSight.collect{photo ->
                    if (values.isNullOrEmpty()){
                        for (i in photo){
                            values.add(i)
                        }
                    }else{
                        values.add(photo[photo.lastIndex])
                    }

                    val sightAdapter =  SightAdapter(values)
                    binding.recyclerView.adapter = sightAdapter
                }
            }
        }
    }
}