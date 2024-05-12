package com.dacs.vku.presentation.onboarding.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dacs.vku.presentation.Dimension.Heading1
import com.dacs.vku.presentation.Dimension.Heading2
import com.dacs.vku.presentation.Dimension.Heading3
import com.dacs.vku.presentation.Dimension.MediumPadding1
import com.dacs.vku.presentation.Dimension.MediumPadding2
import com.dacs.vku.presentation.Dimension.TextArea
import com.dacs.vku.presentation.onboarding.Page
import com.dacs.vku.presentation.onboarding.pages
import com.dacs.vku.R
import com.dacs.vku.ui.theme.VKU

@Composable
fun OnBoardingPage(
    page: Page,
    modifier: Modifier

) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.6f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(30.dp))
        Column(modifier = Modifier.fillMaxHeight(fraction = 0.4f)) {
            Text(
                text = page.title,
                modifier = Modifier.padding(horizontal = MediumPadding2),
                fontSize = Heading2,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = colorResource(id = R.color.text_medium)
            )
            Text(
                text = page.description,
                fontSize = TextArea,
                modifier = Modifier.padding(horizontal = MediumPadding2),
                color = colorResource(id = R.color.text_medium)
            )
        }


    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun OnBoardingPagePreview() {
    VKU {
        OnBoardingPage(page = pages[0], modifier = Modifier.padding(0.dp) )
    }
}

