/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui.library.playlists.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import tm.alashow.base.util.extensions.stateInDefault
import tm.alashow.datmusic.data.observers.playlist.ObservePlaylist
import tm.alashow.datmusic.data.observers.playlist.ObservePlaylistDetails
import tm.alashow.datmusic.domain.entities.PlaylistId
import tm.alashow.datmusic.ui.utils.AudiosCountDuration
import tm.alashow.navigation.Navigator
import tm.alashow.navigation.screens.PLAYLIST_ID_KEY
import tm.alashow.navigation.screens.RootScreen

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val playlist: ObservePlaylist,
    private val playlistDetails: ObservePlaylistDetails,
    private val navigator: Navigator
) : ViewModel() {

    private val playlistId = handle.get<Long>(PLAYLIST_ID_KEY) as PlaylistId

    val state = combine(playlist.flow, playlistDetails.flow, ::PlaylistDetailViewState)
        .distinctUntilChanged()
        .map {
            if (it.playlistDetails.complete && !it.isEmpty) {
                it.copy(audiosCountDuration = AudiosCountDuration.from(it.playlistDetails.invoke()?.audios.orEmpty()))
            } else it
        }
        .stateInDefault(viewModelScope, PlaylistDetailViewState.Empty)

    init {
        load()
    }

    private fun load() {
        playlist(playlistId)
        playlistDetails(playlistId)
    }

    fun refresh() = load()
    fun addSongs() = navigator.navigate(RootScreen.Search.route)
}
