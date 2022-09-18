package com.example.animationsexamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animationsexamples.ui.theme.AnimationsExamplesTheme
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationsExamplesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainFun()
                }
            }
        }
    }
}

@Composable
fun MainFun() {
    val scrollState: ScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        CrossfadeDemo()
        Spacer(modifier = Modifier.height(30.dp))
        AnimateContentSizeDemo()
        Spacer(modifier = Modifier.height(30.dp))
        AnimatedVisibilityDemo()
        Spacer(modifier = Modifier.height(30.dp))
        UpdateTransitionDemo()
        Spacer(modifier = Modifier.height(30.dp))
        AnimateAsStateDemo()
        Spacer(modifier = Modifier.height(30.dp))
    }
}

enum class DemoScene {
    Text, Icon
}

@Preview
@Composable
fun CrossfadeDemo() {
    var scene: DemoScene by remember { mutableStateOf(DemoScene.Text) }

    Column {
        Button(onClick = {
            scene = when(scene) {
                DemoScene.Text -> DemoScene.Icon
                DemoScene.Icon -> DemoScene.Text
            }
        }) {
            Text(text = "TOGGLE")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(targetState = scene) { scene ->
            when (scene) {
                DemoScene.Text -> Text(
                    text = "Phone",
                    fontSize = 32.sp
                )
                DemoScene.Icon -> Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone"
                )
            }
        }
    }
}

@Preview
@Composable
fun AnimateContentSizeDemo() {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded = !expanded }) {
            Text(text = if(expanded) "Shrink" else "Expand")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .animateContentSize()
        ) {
            Text(
                text = stringResource(id = R.string.long_text),
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(16.dp),
                maxLines = if(expanded) Int.MAX_VALUE else 2
            )
        }
    }
}

@Preview
@Composable
fun AnimatedVisibilityDemo() {
    var visible by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { visible = !visible }) {
            Text(text = if(visible) "HIDE" else "SHOW")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible) {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(Blue)
            )
        }
    }
}

enum class BoxState {
    Small, Large
}

@Preview
@Composable
fun UpdateTransitionDemo() {
    var boxState by remember { mutableStateOf(BoxState.Small) }
    val transition = updateTransition(targetState = boxState, label = "")

    val color: Color by transition.animateColor(label = "") { state ->
        when(state) {
            BoxState.Small -> Blue
            BoxState.Large -> Yellow
        }
    }

    val size: Dp by transition.animateDp(
        label = ""
    ) { state ->
        when(state) {
            BoxState.Small -> 64.dp
            BoxState.Large -> 128.dp
        }
    }

    Column {
        Button(onClick = {
            boxState = when (boxState) {
                BoxState.Small -> BoxState.Large
                BoxState.Large -> BoxState.Small
            }
        }
        ) {
            Text(text = "Change Color")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier
            .size(size)
            .background(color))
    }
}

@Preview()
@Composable
fun AnimateAsStateDemo() {
    var blue by remember { mutableStateOf(true) }
    val color: Color by animateColorAsState(if (blue) Blue else Yellow)
    
    Column {
        Button(onClick = { blue = !blue }) {
            Text(text = "Change Color")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier
            .size(128.dp)
            .background(color))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnimationsExamplesTheme {
        MainFun()
    }
}