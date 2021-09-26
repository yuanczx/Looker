package com.yuan.looker.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.yuan.looker.model.Content
import com.yuan.looker.ui.theme.settingBg

fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@ExperimentalCoilApi
@Composable
fun NewsList(
    newscast: List<Content>,
    listState: LazyListState = rememberLazyListState(),
    lastEvent: ((Boolean) -> Unit)? = null,
    itemClick: (url: String) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        items(newscast) { item ->
            NewsItem(newsItem = item, itemClick)
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp), horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(25.dp), strokeWidth = 3.dp)
                Text(
                    text = "Loading",
                    Modifier
                        .padding(start = 10.dp)
                        .fillMaxHeight(), textAlign = TextAlign.Center, fontWeight = W700
                )
            }

        }

    }
    lastEvent?.let {
        it(listState.isScrolledToTheEnd())
    }
}

@ExperimentalCoilApi
@Composable
fun NewsItem(newsItem: Content, itemClick: (url: String) -> Unit) {
    Card(backgroundColor = MaterialTheme.colors.settingBg,
        modifier = Modifier
            .fillMaxWidth()
            .width(120.dp)
            .padding(top =1.dp,bottom = 1.dp)
            .clickable { itemClick(newsItem.url) },
            //.padding(top = 7.dp,end = 10.dp),
        elevation = 1.dp,
        shape = RoundedCornerShape(0.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            val imageSrc = newsItem.imgsrc.replace("http", "https")
            Text(
                text = newsItem.title,
                fontWeight = W700,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(start = 5.dp, end = 5.dp)

            )
            Image(
                painter = rememberImagePainter(data = imageSrc,
                    builder = { transformations(RoundedCornersTransformation(20f)) }),
                contentDescription = "News Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(5.dp)
            )
        }
    }
}