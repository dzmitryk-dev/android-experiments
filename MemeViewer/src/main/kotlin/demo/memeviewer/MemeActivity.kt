package demo.memeviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import demo.memeviewer.model.MemeData
import demo.memeviewer.presentation.MemeViewModel
import demo.memeviewer.ui.theme.MemeViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemeActivity : ComponentActivity() {

    private val viewModel: MemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemeViewerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MemeScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
internal fun MemeScreen(viewModel: MemeViewModel) {
    val memes = viewModel.memes.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = memes.itemCount,
            key = { index ->
                when (val meme = memes.peek(index)) {
                    is MemeData.Image -> meme.imageUrl
                    is MemeData.Video -> meme.videoUrl
                    is MemeData.NSFW -> "nsfw"
                    null -> "placeholder_$index"
                }
            }
        ) { index ->
            memes[index]?.let { meme ->
                MemeItem(meme = meme)
            }
        }

        when (val state = memes.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is LoadState.Error -> {
                item {
                    Text(
                        text = "Error loading more items: ${state.error.message}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            else -> Unit
        }
    }

    when (val state = memes.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error loading items: ${state.error.message}")
            }
        }

        else -> Unit
    }
}

@Composable
fun MemeItem(meme: MemeData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        when (meme) {
            is MemeData.Image -> ImageMeme(meme)
            is MemeData.Video, is MemeData.NSFW -> {
                // TODO: Implement UI for Video and NSFW memes
            }
        }
    }
}

@Composable
fun ImageMeme(meme: MemeData.Image, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(meme.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            contentDescription = meme.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        Column(Modifier.padding(8.dp)) {
            Text(
                text = meme.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "by ${meme.creator}",
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

