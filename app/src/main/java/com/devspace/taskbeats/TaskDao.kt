package com.devspace.taskbeats

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {
    @Query("Select * From TaskEntity")
    fun getAll():List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll (taskEntities: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (taskEntities: TaskEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update (taskEntity: TaskEntity)

    @Delete
    fun delete(taskEntity: TaskEntity)

}
