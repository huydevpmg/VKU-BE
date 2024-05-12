package com.dacs.vku.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dacs.vku.domain.model.NotiItem
import com.dacs.vku.presentation.Dimension.ExtraSmallPadding2
import com.dacs.vku.presentation.Dimension.MediumPadding1

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    notis: List<NotiItem>,
    onClick: (NotiItem) -> Unit
) {
    if (notis.isEmpty()){
        EmptyScreen()
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MediumPadding1),
        contentPadding = PaddingValues(all = ExtraSmallPadding2)
    ) {
        items(
            count = notis.size,
        ) {
            notis[it]?.let { noti ->
                ArticleCard(notiItem= noti, onClick = {onClick })
            }
        }
    }

}

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    notis: LazyPagingItems<NotiItem>,
    onClick: (NotiItem) -> Unit
) {

    val handlePagingResult = handlePagingResult(notis)


    if (handlePagingResult) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
            contentPadding = PaddingValues(all = ExtraSmallPadding2)
        ) {
            items(
                count = notis.itemCount,
            ) {
                notis[it]?.let { notis ->
                    ArticleCard(notiItem = notis, onClick = { onClick(notis) })
                }
            }
        }
    }
}

@Composable
fun handlePagingResult(articles: LazyPagingItems<NotiItem>): Boolean {
    val loadState = articles.loadState
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            ShimmerEffect()
            false
        }

        error != null -> {
            EmptyScreen(error = error)
            false
        }

        else -> {
            true
        }
    }
}

@Composable
fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)) {
        repeat(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }
    }
}