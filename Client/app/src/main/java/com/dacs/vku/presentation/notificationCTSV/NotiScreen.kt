package com.dacs.vku.presentation.notificationCTSV

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.paging.compose.LazyPagingItems
import com.dacs.vku.R
import com.dacs.vku.domain.model.NotiItem
import com.dacs.vku.presentation.Dimension.BigIconSize
import com.dacs.vku.presentation.Dimension.MediumPadding1
import com.dacs.vku.presentation.common.ArticlesList
import com.dacs.vku.presentation.common.SearchBar


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotiScreen(
    notis: LazyPagingItems<NotiItem>,
    navigate:(String) -> Unit
) {

    val titles by remember {
        derivedStateOf {
            if (notis.itemCount > 10) {
                notis.itemSnapshotList.items
                    .slice(IntRange(start = 0, endInclusive = 0))
                    .joinToString(separator = " \uD83D\uDFE5 ") { it.title }
            } else {
                ""
            }
        }
    }

        Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MediumPadding1)
            .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(MediumPadding1))
            Icon(
                painter = painterResource(id = R.drawable.finallogo),
                contentDescription = null,
                modifier = Modifier.size(BigIconSize),
                tint = colorResource(id = R.color.body)
            )
        SearchBar(
            modifier = Modifier
                .padding(horizontal = MediumPadding1)
                .fillMaxWidth(),
            text = "",
            readOnly = true,
            onValueChange = {},
            onSearch = {},
            onClick =   {

            }

        )

        Spacer(modifier = Modifier.height(MediumPadding1))


        ArticlesList(
            modifier = Modifier.padding(horizontal = MediumPadding1),
            notis = notis,
            onClick = {

            }
        )
    }
}

