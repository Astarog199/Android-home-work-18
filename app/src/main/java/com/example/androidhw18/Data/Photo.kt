package com.example.androidhw18.Data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Photo(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "path") val path: String
)
