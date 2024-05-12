package com.dacs.vku.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dacs.vku.domain.model.NotiItem
import com.dacs.vku.R
import com.dacs.vku.presentation.Dimension.ExtraSmallPadding
import com.dacs.vku.presentation.Dimension.SmallIconSize
import com.dacs.vku.ui.theme.VKU


@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    notiItem: NotiItem,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .clickable{ onClick?.invoke()}
            .padding(10.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .height(96.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = notiItem.title,
                style = MaterialTheme.typography.bodyMedium.copy(),
                color = colorResource(
                    id = R.color.text_title
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(  verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null,
                    modifier = Modifier.size(SmallIconSize),
                    tint = colorResource(id = R.color.body)
                )
                Spacer(modifier = Modifier.width(ExtraSmallPadding))
                Text(text = notiItem.spanText, style = MaterialTheme.typography.labelSmall)

            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ArticleCardPreview() {
    VKU{
        ArticleCard(notiItem = NotiItem(
            title = "Thông báo Đăng ký chuyên ngành cho ngành Kỹ sư/ Cử nhân Công nghệ thông tin khóa tuyển sinh 2022",
            spanText = "- 13-04-2023",
            href = "...",
            id = ""
        )
        )
    }
}