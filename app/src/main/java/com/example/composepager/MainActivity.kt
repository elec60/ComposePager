package com.example.composepager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composepager.ui.theme.ComposePagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = mutableListOf<MyData>().apply {
            repeat(30) {
                this.add(MyData("Title ${it + 1}", "Sample body ${it + 1}"))
            }
        }
        setContent {
            ComposePagerTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Zoom-out page transformer", modifier = Modifier.padding(16.dp))
                    ZoomOutPager(items)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Depth page transformer", modifier = Modifier.padding(16.dp))
                    DepthPager(items)
                }

            }
        }
    }
}

@Composable
private fun ZoomOutPager(items: MutableList<MyData>) {
    Pager(
        modifier = Modifier
            .background(Color.LightGray)
            .height(200.dp),
        pagesCount = items.size
    ) { page, position ->
        var pageHeight by remember {
            mutableStateOf(0f)
        }
        var pageWidth by remember {
            mutableStateOf(0f)
        }

        ItemContent(
            modifier = Modifier.fillParentMaxWidth(),
            myData = items[page],
            graphicsLayerScope = zoomOutTransformer(position, pageWidth, pageHeight)
        ) { width, height ->
            pageHeight = height
            pageWidth = width
        }
    }
}

@Composable
private fun DepthPager(items: MutableList<MyData>) {
    Pager(
        modifier = Modifier
            .background(Color.LightGray)
            .height(200.dp),
        pagesCount = items.size
    ) { page, position ->
        var pageHeight by remember {
            mutableStateOf(0f)
        }
        var pageWidth by remember {
            mutableStateOf(0f)
        }

        ItemContent(
            modifier = Modifier.fillParentMaxWidth(),
            myData = items[page],
            graphicsLayerScope = depthTransformer(position, pageWidth)
        ) { width, height ->
            pageHeight = height
            pageWidth = width
        }
    }
}
@Composable
fun ItemContent(
    modifier: Modifier,
    myData: MyData,
    graphicsLayerScope: GraphicsLayerScope,
    sizeCallback: (width: Float, height: Float) -> Unit,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .graphicsLayer {
                setFrom(graphicsLayerScope)
            }
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
            .onGloballyPositioned {
                sizeCallback(it.size.width.toFloat(), it.size.height.toFloat())
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = myData.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = myData.text, fontSize = 12.sp)
        }
    }
}

