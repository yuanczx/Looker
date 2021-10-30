package com.yuan.looker.ui.composable

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
import androidx.compose.ui.text.font.FontWeight.Companion.W300
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.yuan.looker.model.NetEaseNewsItem
import com.yuan.looker.ui.theme.listBack

fun LazyListState.isScrolledToTheEnd() =layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@ExperimentalCoilApi
@Composable
fun NewsList(
    newsList: List<NetEaseNewsItem>,
    listState: LazyListState = rememberLazyListState(),
    lastEvent: ((Boolean) -> Unit)? = null,
    itemClick: (url: String) -> Unit
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.listBack)
    ) {
        items(newsList) { item ->
            NewsItem(newsItem = item, itemClick)
        }
        item {
            Row( Modifier.fillMaxWidth().height(30.dp), horizontalArrangement = Arrangement.Center) {
                CircularProgressIndicator(modifier = Modifier.size(25.dp), strokeWidth = 3.dp)
                Text(text = "Loading", modifier = Modifier.padding(start = 10.dp).fillMaxHeight(), textAlign = TextAlign.Center, fontWeight = W700 ) }
        }

    }
    lastEvent?.let {it(listState.isScrolledToTheEnd())}
}

@ExperimentalCoilApi
@Composable
fun NewsItem(newsItem:NetEaseNewsItem,itemClick: (String) -> Unit) {
    Box(Modifier.fillMaxWidth().width(150.dp)){
    Card(backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(top = 4.dp, bottom = 4.dp)
            .clickable { itemClick(newsItem.docid) },
        elevation = 0.dp,
        shape = RoundedCornerShape(0.dp),
    ) {
        Row( modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
            val imageSrc = newsItem.imgsrc.replace("http", "https")
            Column(
                Modifier
                    .fillMaxWidth(0.6f)
                    .fillMaxHeight()
                    .padding(start = 5.dp, end = 5.dp,top = 5.dp,bottom = 6.dp),verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = newsItem.title,
                fontWeight = W700,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface,
                maxLines = 3,
            )
            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = newsItem.source,
                fontWeight = W300,
                fontSize = 13.sp,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
            )
            Text(
                text = newsItem.ptime,
                fontWeight = W300,
                fontSize = 13.sp,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
            )}}
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
}