package technical.test.yprsty.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import technical.test.yprsty.data.source.locale.LocaleActivity
import technical.test.yprsty.data.source.locale.ToDoDatabase
import technical.test.yprsty.data.source.remote.GeoApiService
import technical.test.yprsty.data.source.remote.NetworkResponse
import technical.test.yprsty.model.Activity
import technical.test.yprsty.model.GeoIp

class ToDoRepository(
    toDoDatabase: ToDoDatabase,
    private val geoApiService: GeoApiService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val activityDao = toDoDatabase.activityDao()

    fun loadGeoApi(): Flow<GeoIp> = flow {
        val response = geoApiService.getGeoIp()
        emit(response.toExternal())
    }.flowOn(defaultDispatcher)

    suspend fun insertActivity(activity: Activity) {
        withContext(defaultDispatcher) {
            activityDao.insertActivity(activity.toInternal())
        }
    }

    fun searchActivityByTitle(title: String): Flow<List<Activity>> = activityDao
        .searchByTitle(title)
        .map { activities -> activities.toExternal() }
        .flowOn(defaultDispatcher)

    fun loadAllActivity(): Flow<List<Activity>> = activityDao
        .getAllActivity()
        .map { activities -> activities.toExternal() }
        .flowOn(defaultDispatcher)

}

fun NetworkResponse.toExternal() = GeoIp(
    countryCode = countryCode,
    country = country,
    region = region
)

fun LocaleActivity.toExternal() = Activity(
    id = id,
    title = title,
    description = description,
    isActive = isActive
)

fun List<LocaleActivity>.toExternal() = map(LocaleActivity::toExternal)

fun Activity.toInternal() = LocaleActivity(
    title = title,
    description = description,
    isActive = isActive,
)