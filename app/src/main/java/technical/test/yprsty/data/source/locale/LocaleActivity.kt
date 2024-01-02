package technical.test.yprsty.data.source.locale

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity")
data class LocaleActivity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val title: String,
    val description: String,
    val isActive: Boolean,
)