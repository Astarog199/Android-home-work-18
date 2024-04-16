package com.example.androidhw18.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SightDao {
@Query("SELECT * FROM Sight")
fun getALL(): Flow<List<Sight>>

@Insert(entity = Sight::class)
suspend fun insert(photo: Photo)

@Query("DELETE FROM Sight")
suspend fun delete()
}