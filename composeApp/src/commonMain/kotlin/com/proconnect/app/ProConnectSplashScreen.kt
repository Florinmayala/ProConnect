package com.proconnect.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import com.proconnect.app.resources.Res
import com.proconnect.app.resources.proconnect_building_bw

private val Ink = Color(0xFF111B25)
private val SloganInk = Color(0xFF344354)

@Composable
fun ProConnectSplashScreen() {
    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White)) {
        val compact = maxHeight < 650.dp

        Image(
            painter = painterResource(Res.drawable.proconnect_building_bw),
            contentDescription = "Façade vitrée d’un bâtiment moderne en noir et blanc",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(maxHeight * 0.51f),
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter,
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = maxHeight * (if (compact) 0.12f else 0.17f)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProConnectLogo(Modifier.size(width = 88.dp, height = 76.dp))
            Spacer(Modifier.height(8.dp))
            BasicText(
                text = "ProConnect",
                maxLines = 1,
                style = TextStyle(
                    color = Ink,
                    fontSize = if (compact) 37.sp else 42.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1.5).sp,
                ),
            )
            Spacer(Modifier.height(if (compact) 18.dp else 24.dp))
            BasicText(
                text = "Les opportunités d’aujourd’hui,\nles carrières de demain.",
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 20.dp),
                style = TextStyle(
                    color = SloganInk,
                    fontSize = if (compact) 16.sp else 18.sp,
                    lineHeight = if (compact) 24.sp else 27.sp,
                    textAlign = TextAlign.Center,
                ),
            )
        }

        OnboardingIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 68.dp),
        )
    }
}

@Composable
private fun ProConnectLogo(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val sx = size.width / 92f
        val sy = size.height / 76f
        fun path(vararg points: Pair<Float, Float>): Path = Path().apply {
            moveTo(points[0].first * sx, points[0].second * sy)
            points.drop(1).forEach { lineTo(it.first * sx, it.second * sy) }
        }
        val leftLink = path(
            43f to 10f, 34f to 5f, 27f to 5f, 6f to 26f,
            6f to 43f, 26f to 63f, 36f to 63f, 58f to 41f,
        )
        val rightLink = path(
            49f to 58f, 58f to 65f, 66f to 65f, 87f to 43f,
            87f to 26f, 67f to 6f, 57f to 6f, 35f to 28f,
        )
        val strokeWidth = 10f * sx
        val stroke = Stroke(strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawPath(leftLink, Ink, style = stroke)
        drawPath(rightLink, Color.White, style = Stroke(strokeWidth + 7f * sx, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawPath(rightLink, Ink, style = stroke)
    }
}

@Composable
private fun OnboardingIndicator(modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(3) { index ->
            Canvas(Modifier.size(8.dp)) {
                drawCircle(if (index == 0) Color.White else Color(0xFF8B949C), radius = size.minDimension / 2)
                if (index != 0) {
                    drawCircle(Color(0xFF17212A), radius = size.minDimension / 4)
                }
            }
        }
    }
}
