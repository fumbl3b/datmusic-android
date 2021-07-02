/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.data.repos.search

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.flow.map
import tm.alashow.datmusic.domain.entities.Album
import tm.alashow.datmusic.domain.entities.Artist
import tm.alashow.datmusic.domain.entities.Audio

typealias DatmusicSearchStore<T> = Store<DatmusicSearchParams, List<T>>
typealias DatmusicSearchAudioStore = DatmusicSearchStore<Audio>

@InstallIn(SingletonComponent::class)
@Module
object DatmusicSearchStoreModule {

    @Provides
    @Singleton
    fun datmusicSearchAudioStore(
        search: DatmusicSearchDataSource
    ): DatmusicSearchStore<Audio> = StoreBuilder.from(
        fetcher = Fetcher.of { params: DatmusicSearchParams ->
            search(params).map { it.data.audios }.getOrThrow()
        }
    ).build()

    @Provides
    @Singleton
    fun datmusicSearchArtistsStore(
        search: DatmusicSearchDataSource
    ): DatmusicSearchStore<Artist> = StoreBuilder.from(
        fetcher = Fetcher.of { params: DatmusicSearchParams ->
            search(params).map { it.data.artists }.getOrThrow()
        }
    ).build()

    @Provides
    @Singleton
    fun datmusicSearchAlbumsStore(
        search: DatmusicSearchDataSource
    ): DatmusicSearchStore<Album> = StoreBuilder.from(
        fetcher = Fetcher.of { params: DatmusicSearchParams ->
            search(params).map { it.data.albums }.getOrThrow()
        }
    ).build()

    // @Provides
    // @Singleton
    // fun datmusicSearchAudioStoreDiskCaching(
    //     search: DatmusicSearchDataSource,
    //     dao: AudiosDao
    // ): DatmusicSearchAudioStore = StoreBuilder.from(
    //     fetcher = Fetcher.of { params: DatmusicSearchParams ->
    //         search(params).map { it.data.audios }.getOrThrow()
    //     },
    //     sourceOfTruth = SourceOfTruth.of(
    //         reader = { params: DatmusicSearchParams ->
    //             dao.entriesObservable(params).map { entries ->
    //                 when {
    //                     entries.isEmpty() -> null
    //                     else -> entries
    //                 }
    //             }
    //         },
    //         writer = { params, response ->
    //             dao.withTransaction {
    //                 val entries = response.map { it.copy(params = params.toString(), page = params.page) }
    //                 if (params.page == 0) {
    //                     // If we've requested page 0, remove any existing entries first
    //                     dao.deleteAll()
    //                     dao.insertAll(entries)
    //                 } else {
    //                     dao.update(params, entries)
    //                 }
    //             }
    //         },
    //         delete = dao::delete,
    //         deleteAll = dao::deleteAll
    //     )
    // ).build()
}
