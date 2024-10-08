package com.devspace.taskbeats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TaskDao {
    @Query("Select * From TaskEntity")
    fun getAll():List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll (taskEntities: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (taskEntities: TaskEntity)


}