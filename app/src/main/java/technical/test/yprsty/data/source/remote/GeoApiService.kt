package technical.test.yprsty.data.source.remote

import retrofit2.http.GET

interface GeoApiService {
    @GET("geo.json")
    suspend fun getGeoIp(): NetworkResponse
}