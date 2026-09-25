package com.proconnect.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val IllustrationOutline = Color(0xFFDFE4EA)
private val IllustrationSurface = Color(0xFFF4F6F8)

@Composable
fun ProConnectOnboardingScreen3() {
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        val compact = maxHeight < 700.dp || maxWidth < 340.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = maxHeight * (if (compact) 0.045f else 0.08f), bottom = 112.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProConnectLogo(
                Modifier.size(
                    width = if (compact) 80.dp else 88.dp,
                    height = if (compact) 69.dp else 76.dp,
                ),
            )
            Spacer(Modifier.height(if (compact) 12.dp else 18.dp))
            BasicText(
                text = "Suivez les entreprises qui vous intéressent.",
                modifier = Modifier.widthIn(max = 350.dp).fillMaxWidth(),
                style = TextStyle(
                    color = Ink,
                    fontSize = if (compact) 25.sp else 28.sp,
                    lineHeight = if (compact) 31.sp else 35.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.6).sp,
                ),
            )
            Spacer(Modifier.height(if (compact) 12.dp else 18.dp))
            BasicText(
                text = "Recevez des alertes lorsqu’elles publient de nouvelles opportunités.",
                modifier = Modifier.widthIn(max = 330.dp).fillMaxWidth(),
                style = TextStyle(
                    color = SloganInk,
                    fontSize = if (compact) 15.sp else 16.sp,
                    lineHeight = if (compact) 22.sp else 24.sp,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(Modifier.height(if (compact) 24.dp else 34.dp))
            FollowAndAlertIllustration(
                compact = compact,
                modifier = Modifier.widthIn(max = 350.dp).fillMaxWidth(),
            )
        }

        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 12.dp, bottom = 56.dp),
            contentAlignment = Alignment.Center,
        ) {
            OnboardingIndicator(currentPage = 2, onDarkBackground = false)
        }
    }
}

@Composable
private fun FollowAndAlertIllustration(compact: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier.semantics {
            contentDescription = "Groupe ABC, entreprise certifiée, est suivie. Une nouvelle opportunité Développeur Mobile déclenche une alerte."
        },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .border(1.dp, IllustrationOutline, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(if (compact) 15.dp else 18.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                BasicText(
                    "Groupe ABC",
                    style = TextStyle(color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold),
                )
                BasicText(
                    "✓",
                    modifier = Modifier.semantics { contentDescription = "Entreprise certifiée" },
                    style = TextStyle(color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold),
                )
            }
            Spacer(Modifier.height(5.dp))
            BasicText(
                "Entreprise certifiée",
                style = TextStyle(color = SloganInk, fontSize = 13.sp),
            )
        }

        IllustrationConnector(compact)
        Box(
            Modifier
                .background(Ink, RoundedCornerShape(12.dp))
                .semantics { contentDescription = "Entreprise suivie" }
                .padding(horizontal = 24.dp, vertical = if (compact) 10.dp else 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            BasicText(
                "✓ Suivie",
                style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium),
            )
        }

        IllustrationConnector(compact)
        Column(
            Modifier
                .fillMaxWidth()
                .border(1.dp, IllustrationOutline, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(if (compact) 15.dp else 18.dp),
        ) {
            BasicText(
                "Nouvelle opportunité",
                style = TextStyle(color = SloganInk, fontSize = 13.sp),
            )
            Spacer(Modifier.height(7.dp))
            BasicText(
                "Développeur Mobile",
                style = TextStyle(color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold),
            )
        }

        IllustrationConnector(compact)
        Row(
            Modifier
                .fillMaxWidth()
                .background(IllustrationSurface, RoundedCornerShape(14.dp))
                .padding(horizontal = 18.dp, vertical = if (compact) 13.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AlertBell(Modifier.size(20.dp))
            BasicText(
                "Nouvelle alerte",
                style = TextStyle(color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Medium),
            )
        }
    }
}

@Composable
private fun IllustrationConnector(compact: Boolean) {
    Spacer(
        Modifier
            .padding(vertical = if (compact) 7.dp else 10.dp)
            .width(1.dp)
            .height(if (compact) 10.dp else 14.dp)
            .background(IllustrationOutline),
    )
}

@Composable
private fun AlertBell(modifier: Modifier = Modifier) {
    Canvas(modifier.semantics { contentDescription = "Alerte" }) {
        val bell = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.72f)
            cubicTo(
                size.width * 0.34f, size.height * 0.61f,
                size.width * 0.29f, size.height * 0.28f,
                size.width * 0.5f, size.height * 0.28f,
            )
            cubicTo(
                size.width * 0.71f, size.height * 0.28f,
                size.width * 0.66f, size.height * 0.61f,
                size.width * 0.8f, size.height * 0.72f,
            )
            close()
        }
        drawPath(
            bell,
            color = Ink,
            style = Stroke(1.7.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
        drawCircle(Ink, radius = 1.6.dp.toPx(), center = Offset(size.width * 0.5f, size.height * 0.83f))
    }
}
