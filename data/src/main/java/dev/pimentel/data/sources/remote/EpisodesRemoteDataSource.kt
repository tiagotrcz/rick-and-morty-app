package dev.pimentel.data.sources.remote

import dev.pimentel.data.models.EpisodeModel
import dev.pimentel.data.models.PagedResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodesRemoteDataSource {

    @GET("episode/")
    fun getEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") number: String?
    ): Single<PagedResponse<EpisodeModel>>
}
