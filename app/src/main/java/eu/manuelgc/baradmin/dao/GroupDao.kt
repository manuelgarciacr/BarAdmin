package eu.manuelgc.baradmin.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.manuelgc.baradmin.model.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM groups ORDER BY sequence ASC")
    fun getGroups(): Flow<List<Group>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(group: Group)

    @Query("DELETE FROM groups")
    suspend fun deleteAll()
}