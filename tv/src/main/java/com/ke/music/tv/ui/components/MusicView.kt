package com.ke.music.tv.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import com.ke.music.download.LocalDownloadManager
import com.ke.music.room.db.entity.Download
import com.ke.music.room.entity.MusicEntity
import com.ke.music.tv.LocalAppViewModel


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MusicView(
    index: Int,
    musicEntity: MusicEntity,
    rightButton: @Composable (MusicEntity) -> Unit = {
    },
    onHasFocus: (MusicEntity) -> Unit = {
    }
) {
    var hasFocus by remember {
        mutableStateOf(false)
    }

    val appViewModel = LocalAppViewModel.current

    val downloadManager = LocalDownloadManager.current

    val focusRequester = FocusRequester()
    ListItem(modifier = Modifier.onFocusChanged {
        hasFocus = it.hasFocus
        if (hasFocus) {
            onHasFocus(musicEntity)
        }
    }, selected = false, onClick = {
        focusRequester.requestFocus()
    }, headlineContent = {
        Text(text = musicEntity.name, maxLines = 1)
    }, supportingContent = {
        Text(text = musicEntity.subTitle, maxLines = 1)

    }, leadingContent = {

        val callback: (() -> Unit)? =
            when (musicEntity.downloadStatus) {
                Download.STATUS_DOWNLOADED -> {
                    //已经下载 直接播放
                    {
                        appViewModel.playMusic(musicEntity.musicId)

                    }

                }

                Download.STATUS_DOWNLOAD_ERROR -> {
                    //下载失败 需要重试
                    {
                        //                appViewModel.playMusic(musicEntity.musicId)
                        downloadManager.retry(musicEntity.musicId)
                    }
                }

                Download.STATUS_DOWNLOAD_IDLE, null -> {
                    //可以下载
                    {
                        downloadManager.downloadMusic(musicEntity.musicId)

                    }
                }

                else -> {
                    null
                }
            }



        IconButton(
            onClick = {
                callback?.invoke()
            },
            enabled = hasFocus && callback != null,
            modifier = Modifier.focusRequester(focusRequester)
        ) {
            if (hasFocus) {
                when (musicEntity.downloadStatus) {
                    Download.STATUS_DOWNLOADED -> {
                        //已经下载 可以播放
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    }

                    Download.STATUS_DOWNLOAD_IDLE, null -> {
                        //可以下载
                        Icon(imageVector = Icons.Default.Download, contentDescription = null)
                    }

                    Download.STATUS_DOWNLOAD_ERROR -> {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    }

                    Download.STATUS_DOWNLOADING -> {
                        Icon(imageVector = Icons.Default.Downloading, contentDescription = null)
                    }
                }
            } else {
                Text(text = "${index + 1}")
            }
        }
    }, trailingContent = {

        if (hasFocus) {
            Row {
                if (musicEntity.mv != 0L) {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.OndemandVideo, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                }

                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.AddToPhotos, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))


                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Album, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))
                rightButton(musicEntity)
            }
        }

//        rightButton(musicEntity)

//        if (hasFocus) {
//            Row {
//                //下载按钮
//
//                val context = LocalContext.current
//                IconButton(
//                    onClick = {
//
//                        val intent = Intent(context, MusicDownloadService::class.java)
//                        intent.action = MusicDownloadService.ACTION_DOWNLOAD_MUSIC
//                        intent.putExtra(MusicDownloadService.EXTRA_ID, musicEntity.musicId)
//                        val name = context.startService(intent)
//
//                        Logger.d("开始下载音乐 $name")
//                    },
//                    enabled = musicEntity.canDownload
//                ) {
//
//                    when (musicEntity.downloadStatus) {
//                        null, Download.STATUS_DOWNLOAD_IDLE, Download.STATUS_DOWNLOAD_ERROR -> {
//                            Icon(imageVector = Icons.Default.Download, contentDescription = null)
//                        }
//
//                        Download.STATUS_DOWNLOADED -> {
//                            Icon(
//                                imageVector = Icons.Default.DownloadDone,
//                                contentDescription = null
//                            )
//                        }
//
//                        else -> {
//                            Icon(imageVector = Icons.Default.Downloading, contentDescription = null)
//                        }
//                    }
//                }
//
//            }
//        }
    })
//    val size = 40.dp
//
//    var isFocused by remember {
//        mutableStateOf(false)
//    }

//    Row(
//        modifier = Modifier
//            .focusable()
//            .background(
//                if (isFocused) Color.White else Color.Transparent
//            )
//            .focusTarget()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//
//        AsyncImage(
//            model = musicEntity.album.imageUrl,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(size)
//                .background(MaterialTheme.colorScheme.primary)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Column(modifier = Modifier.weight(1f)) {
//            Text(text = musicEntity.name, maxLines = 1)
//            Text(
//                text = "${musicEntity.artists.joinToString("/") { it.name }}-${musicEntity.album.name}",
//                style = MaterialTheme.typography.bodySmall,
//                maxLines = 1
//            )
//        }
//
//
//        if (musicEntity.downloadStatus != null) {
//
//            val imageVector = when (musicEntity.downloadStatus) {
//                Download.STATUS_DOWNLOADED -> Icons.Default.DownloadDone
//                else -> Icons.Default.Downloading
//            }
//
//            IconButton(onClick = { }, enabled = false) {
//                Icon(imageVector = imageVector, contentDescription = null)
//            }
//        }
//
//        if (musicEntity.mv != 0L) {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)
//
//            }
//        }
//
//        rightButton()
//        Divider(modifier = Modifier.height(0.2.dp))
}

//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewIdlePreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOAD_IDLE)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewDownloadingPreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOADING)
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun MusicViewDownloadedPreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOADED, mv = 1L)
//    }
//}

//@Composable
//private fun MusicView(status: Int, mv: Long = 0) {
//    val musicEntity = MusicEntity(
//        musicId = 0L,
//        name = "漫步人生路",
//        mv = mv,
//        album = Album(0L, "最爱", ""),
//        artists = listOf(
//            Artist(0, "邓丽君")
//        ),
//        downloadStatus = status
//    )
//
//
//    MusicView(musicEntity = musicEntity, rightButton = {})
//}