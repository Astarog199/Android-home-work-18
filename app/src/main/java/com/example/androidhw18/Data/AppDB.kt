package com.example.androidhw18.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Sight::class], version = 1)
abstract class AppDB: RoomDatabase(){
    abstract fun sightDao(): SightDao
}
