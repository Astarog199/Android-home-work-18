package com.example.androidhw18.Presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidhw18.Data.Photo
import com.example.androidhw18.Data.Sight
import com.example.androidhw18.Data.SightDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val sightDao: SightDao):ViewModel() {
val allSight = this.sightDao.getALL().stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000L),
    initialValue = emptyList()
)

    fun add(name:String, path:String){
        viewModelScope.launch {
            sightDao.insert(Photo(name = name, path = path))
        }
    }

    fun delete(){
        viewModelScope.launch {
            sightDao.delete()
        }
    }
}