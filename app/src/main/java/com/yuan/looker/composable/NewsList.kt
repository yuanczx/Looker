package com.yuan.looker.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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

fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount-1

@ExperimentalCoilApi
@Composable
fun NewsList(
    newscast: List<Content>,
    listState: LazyListState = rememberLazyListState(),
    lastEvent: ((Boolean) -> Unit)? =null,
    itemClick:(url:String)->Unit
) {
    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        items(newscast) { item ->
            NewsItem(newsItem = item,itemClick)
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp),horizontalArrangement = Arrangement.Center) {
                CircularProgressIndicator(modifier = Modifier.size(25.dp),strokeWidth = 3.dp)
                Text(text = "Loading",
                    Modifier
                        .padding(start = 10.dp)
                        .fillMaxHeight(),textAlign = TextAlign.Center,fontWeight = W700)
            }

        }

    }
    lastEvent?.let {
        it(listState.isScrolledToTheEnd())
    }
}

@ExperimentalCoilApi
@Composable
fun NewsItem(newsItem: Content,itemClick:(url:String)->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(start = 3.dp, bottom = 3.dp, end = 3.dp, top = 5.dp)
            .clickable { itemClick(newsItem.url) }
    ) {
        val imageSrc = newsItem.imgsrc.replace("http", "https")

        Text(
            text = newsItem.title,
            fontWeight = W700,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onSurface,
            maxLines = 3,
            modifier = Modifier
                .padding(end = 5.dp)
                .fillMaxWidth(0.6f)
        )
        Image(
            painter = rememberImagePainter(data = imageSrc,
                builder = { transformations(RoundedCornersTransformation(20f)) }),
            contentDescription = "News Image",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}