package technical.test.yprsty.data.source.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert
    suspend fun insertActivity(activity: LocaleActivity)

    @Query("SELECT * FROM activity")
    fun getAllActivity(): Flow<List<LocaleActivity>>

    @Query("SELECT * FROM activity WHERE title LIKE '%' || :title|| '%'")
    fun searchByTitle(title: String): Flow<List<LocaleActivity>>
}