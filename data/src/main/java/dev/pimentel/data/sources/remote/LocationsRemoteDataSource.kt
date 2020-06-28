package dev.pimentel.data.sources.remote

import dev.pimentel.data.models.LocationModel
import dev.pimentel.data.models.PagedResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationsRemoteDataSource {

    @GET("location/")
    fun getLocations(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?
    ): Single<PagedResponse<LocationModel>>
}
