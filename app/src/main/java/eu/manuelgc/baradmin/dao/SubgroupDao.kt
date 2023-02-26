package eu.manuelgc.baradmin.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.manuelgc.baradmin.model.Subgroup
import kotlinx.coroutines.flow.Flow

@Dao
interface SubgroupDao {

    @Query("SELECT * FROM subgroups ORDER BY groupId, sequence ASC")
    fun getSubgroups(): Flow<List<Subgroup>>

    @Query("SELECT * FROM subgroups WHERE groupId = :groupId ORDER BY groupId, sequence ASC")
    fun getSubgroupsWithGroup(groupId: String): Flow<List<Subgroup>>

    @Query("SELECT * FROM subgroups WHERE single = 1 ORDER BY groupId, sequence ASC")
    fun getSingleSubgroups(): Flow<List<Subgroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subgroup: Subgroup)

    @Query("DELETE FROM subgroups")
    suspend fun deleteAll()
}