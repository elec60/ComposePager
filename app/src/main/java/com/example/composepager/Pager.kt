package com.example.composepager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager(
    pagesCount: Int,
    modifier: Modifier,
    pageComposable: @Composable LazyItemScope.(index: Int, position: Float) -> Unit
) {
    val state = rememberLazyListState()
    val screenWidth = with(LocalConfiguration.current) {
        Dp(this.screenWidthDp.toFloat())
    }
    val screenWidthPx = with(LocalDensity.current) {
        screenWidth.toPx()
    }

    val visibleItemsInfo = remember { derivedStateOf { state.layoutInfo } }

    LazyRow(
        modifier = modifier,
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    ) {
        items(pagesCount) { index ->
            var position = 0f
            val info = visibleItemsInfo.value.visibleItemsInfo
            if (info.isNotEmpty()) {
                info.find { it.index == index }?.let {
                    position = it.offset.toFloat() / screenWidthPx
                }
            }
            pageComposable(index, position)
        }
    }
}

