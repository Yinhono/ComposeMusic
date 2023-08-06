package com.ke.music.tv.ui.downloaded.music

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import com.ke.music.room.entity.MusicEntity
import com.ke.music.tv.ui.components.MusicView

@Composable
fun DownloadedMusicRoute(
    onBackButtonClick: () -> Unit,
) {

    val viewModel: DownloadedMusicViewModel = hiltViewModel()
    val list by viewModel.downloadMusicList.collectAsStateWithLifecycle()


    DownloadedMusicScreen(onBackButtonClick = onBackButtonClick, musicList = list) {
        viewModel.deleteDownloadedMusic(it)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun DownloadedMusicScreen(
    onBackButtonClick: () -> Unit,
    musicList: List<MusicEntity>,
    onDeleteButtonClick: (Long) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        items(musicList, key = {
            it.musicId
        }) {
            MusicView(
                musicList.indexOf(it),
                musicEntity = it, rightButton = {
                    IconButton(onClick = {
                        onDeleteButtonClick(it.musicId)
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                })
        }
    }

}