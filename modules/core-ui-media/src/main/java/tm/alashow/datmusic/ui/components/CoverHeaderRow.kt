/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import tm.alashow.base.imageloading.ImageLoading
import tm.alashow.ui.components.CoverImage
import tm.alashow.ui.theme.AppTheme

object CoverHeaderDefaults {
    val height = 300.dp

    val titleStyle
        @Composable get() = MaterialTheme.typography.h5.copy(
            fontWeight = FontWeight.Black
        )
}

@Composable
fun CoverHeaderRow(
    title: String,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
    imageData: Any? = null,
    icon: VectorPainter? = null,
    height: Dp = CoverHeaderDefaults.height,
    offsetProgress: State<Float> = mutableStateOf(0f),
    titleStyle: TextStyle = CoverHeaderDefaults.titleStyle,
) {
    // scale down as the header is scrolled away
    val imageScale = 1 - offsetProgress.value.coerceAtMost(0.5f)
    val imageHeight = height * 0.825f * imageScale
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppTheme.specs.padding)
    ) {
        val painter = rememberImagePainter(imageData, builder = ImageLoading.defaultConfig)
        CoverImage(
            painter = painter,
            shape = RectangleShape,
            elevation = 10.dp,
            icon = icon ?: rememberVectorPainter(Icons.Default.MusicNote),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(imageHeight),
        )

        Text(
            text = title,
            style = titleStyle,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = titleModifier.align(Alignment.Start)
        )
    }
}
