/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui.playback

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import tm.alashow.base.ui.SnackbarAction
import tm.alashow.base.ui.SnackbarManager
import tm.alashow.base.ui.SnackbarMessage
import tm.alashow.datmusic.data.DatmusicSearchParams
import tm.alashow.datmusic.data.interactors.playlist.CreatePlaylist
import tm.alashow.datmusic.domain.entities.Playlist
import tm.alashow.datmusic.domain.entities.PlaylistId
import tm.alashow.datmusic.playback.PlaybackConnection
import tm.alashow.datmusic.playback.models.MEDIA_TYPE_ALBUM
import tm.alashow.datmusic.playback.models.MEDIA_TYPE_ARTIST
import tm.alashow.datmusic.playback.models.MEDIA_TYPE_AUDIO_FLACS_QUERY
import tm.alashow.datmusic.playback.models.MEDIA_TYPE_AUDIO_MINERVA_QUERY
import tm.alashow.datmusic.playback.models.MEDIA_TYPE_AUDIO_QUERY
import tm.alashow.datmusic.playback.models.MEDIA_TYPE_DOWNLOADS
import tm.alashow.datmusic.playback.models.MEDIA_TYPE_PLAYLIST
import tm.alashow.datmusic.playback.models.QueueTitle.Companion.asQueueTitle
import tm.alashow.datmusic.ui.coreLibrary.R
import tm.alashow.i18n.UiMessage
import tm.alashow.navigation.Navigator
import tm.alashow.navigation.screens.LeafScreen

data class SavedAsPlaylistMessage(val playlist: Playlist) :
    SnackbarMessage<PlaylistId>(
        message = UiMessage.Resource(R.string.playback_queue_saveAsPlaylist_saved, formatArgs = listOf(playlist.name)),
        action = SnackbarAction(UiMessage.Resource(R.string.playback_queue_saveAsPlaylist_open), playlist.id)
    )

@HiltViewModel
class PlaybackSheetViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val playbackConnection: PlaybackConnection,
    private val createPlaylist: CreatePlaylist,
    private val snackbarManager: SnackbarManager,
    private val navigator: Navigator,
) : ViewModel() {

    fun saveQueueAsPlaylist() = viewModelScope.launch {
        val queue = playbackConnection.playbackQueue.first()

        val params = CreatePlaylist.Params(name = queue.title.asQueueTitle().localizeValue(), audios = queue)
        val playlist = createPlaylist.execute(params)

        val savedAsPlaylist = SavedAsPlaylistMessage(playlist)
        snackbarManager.addMessage(savedAsPlaylist)
        if (snackbarManager.observeMessageAction(savedAsPlaylist) != null)
            navigator.navigate(LeafScreen.PlaylistDetail.buildRoute(playlist.id))
    }

    fun navigateToQueueSource() = viewModelScope.launch {
        val queue = playbackConnection.playbackQueue.first()
        val (sourceMediaType, sourceMediaValue) = queue.title.asQueueTitle().sourceMediaId

        when (sourceMediaType) {
            MEDIA_TYPE_PLAYLIST -> navigator.navigate(LeafScreen.PlaylistDetail.buildRoute(sourceMediaValue.toLong()))
            MEDIA_TYPE_DOWNLOADS -> navigator.navigate(LeafScreen.Downloads().createRoute())
            MEDIA_TYPE_ARTIST -> navigator.navigate(LeafScreen.ArtistDetails.buildRoute(sourceMediaValue))
            MEDIA_TYPE_ALBUM -> navigator.navigate(LeafScreen.AlbumDetails.buildRoute(sourceMediaValue.toLong()))
            MEDIA_TYPE_AUDIO_QUERY -> navigator.navigate(LeafScreen.Search.buildRoute(sourceMediaValue))
            MEDIA_TYPE_AUDIO_MINERVA_QUERY -> navigator.navigate(
                LeafScreen.Search.buildRoute(
                    sourceMediaValue,
                    DatmusicSearchParams.BackendType.MINERVA
                )
            )
            MEDIA_TYPE_AUDIO_FLACS_QUERY -> navigator.navigate(
                LeafScreen.Search.buildRoute(
                    sourceMediaValue,
                    DatmusicSearchParams.BackendType.FLACS
                )
            )
        }
    }
}
