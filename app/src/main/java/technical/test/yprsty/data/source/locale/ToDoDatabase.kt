package technical.test.yprsty.data.source.locale

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocaleActivity::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}