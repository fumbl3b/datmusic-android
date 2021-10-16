/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui.library.playlists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import tm.alashow.datmusic.domain.entities.Playlist
import tm.alashow.datmusic.ui.library.LibraryViewModel
import tm.alashow.datmusic.ui.library.R
import tm.alashow.datmusic.ui.library.items.LibraryItemAction
import tm.alashow.datmusic.ui.library.items.LibraryItemRow
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.Navigator
import tm.alashow.navigation.screens.EditPlaylistScreen
import tm.alashow.navigation.screens.LeafScreen

@Composable
fun PlaylistRow(
    playlist: Playlist,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel(),
    navigator: Navigator = LocalNavigator.current
) {
    LibraryItemRow(
        libraryItem = playlist,
        modifier = modifier,
        typeRes = R.string.playlist_title,
        onClick = {
            navigator.navigate(LeafScreen.PlaylistDetail.buildRoute(playlist.id))
        },
        imageData = playlist.artworkFile()
    ) {
        when (it) {
            is LibraryItemAction.Edit -> navigator.navigate(EditPlaylistScreen.buildRoute(playlist.id))
            is LibraryItemAction.Delete -> viewModel.deletePlaylist(playlist.id)
            is LibraryItemAction.Download -> viewModel.downloadPlaylist(playlist.id)
        }
    }
}
