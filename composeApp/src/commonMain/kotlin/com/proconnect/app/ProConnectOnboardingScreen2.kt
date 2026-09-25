package com.proconnect.app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Outline = Color(0xFFDFE4EA)
private val SoftSurface = Color(0xFFF4F6F8)

@Composable
fun ProConnectOnboardingScreen2() {
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
                text = "Trouvez les opportunités qui vous correspondent.",
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
                text = "Emplois, stages, missions et appels d’offres réunis au même endroit.",
                modifier = Modifier.widthIn(max = 330.dp).fillMaxWidth(),
                style = TextStyle(
                    color = SloganInk,
                    fontSize = if (compact) 15.sp else 16.sp,
                    lineHeight = if (compact) 22.sp else 24.sp,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(Modifier.height(if (compact) 22.dp else 34.dp))
            ProConnectSearchIllustration(
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
            OnboardingIndicator(currentPage = 1, onDarkBackground = false)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProConnectSearchIllustration(compact: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier.semantics {
            contentDescription = "Illustration de recherche d’opportunités : catégories Emploi, Stage, Mission et Appel d’offres, puis offres de deux entreprises."
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (compact) 46.dp else 52.dp)
                .border(1.dp, Outline, RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Canvas(Modifier.size(18.dp)) {
                val stroke = 1.8.dp.toPx()
                drawCircle(
                    color = Ink,
                    radius = size.minDimension * 0.31f,
                    center = Offset(size.width * 0.42f, size.height * 0.42f),
                    style = Stroke(stroke),
                )
                drawLine(
                    color = Ink,
                    start = Offset(size.width * 0.64f, size.height * 0.64f),
                    end = Offset(size.width * 0.94f, size.height * 0.94f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
            }
            BasicText(
                "Rechercher une opportunité",
                style = TextStyle(color = SloganInk, fontSize = if (compact) 13.sp else 14.sp),
            )
        }

        Spacer(Modifier.height(if (compact) 12.dp else 16.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CategoryChip("Emploi", selected = true)
            CategoryChip("Stage")
            CategoryChip("Mission")
            CategoryChip("Appel d’offres")
        }

        Spacer(Modifier.height(if (compact) 16.dp else 24.dp))
        OpportunityIllustrationCard(
            title = "Développeur Mobile",
            company = "Groupe ABC",
            location = "Kinshasa",
            type = "Emploi",
            certified = true,
            compact = compact,
        )
        if (!compact) {
            Spacer(Modifier.height(12.dp))
            OpportunityIllustrationCard(
                title = "Assistant Marketing",
                company = "Groupe XYZ",
                type = "Stage",
                compact = false,
                secondary = true,
            )
        }
    }
}

@Composable
private fun CategoryChip(label: String, selected: Boolean = false) {
    Box(
        Modifier
            .background(if (selected) Ink else Color.White, RoundedCornerShape(50))
            .border(1.dp, if (selected) Ink else Outline, RoundedCornerShape(50))
            .padding(horizontal = 11.dp, vertical = 7.dp),
    ) {
        BasicText(
            label,
            style = TextStyle(
                color = if (selected) Color.White else Ink,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
private fun OpportunityIllustrationCard(
    title: String,
    company: String,
    type: String,
    compact: Boolean,
    location: String? = null,
    certified: Boolean = false,
    secondary: Boolean = false,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Outline), RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(if (compact) 14.dp else 16.dp),
    ) {
        BasicText(
            title,
            style = TextStyle(color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(Modifier.height(7.dp))
        if (secondary) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                BasicText(company, style = TextStyle(color = SloganInk, fontSize = 13.sp))
                OpportunityTypeTag(type)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                BasicText(company, style = TextStyle(color = SloganInk, fontSize = 13.sp))
                if (certified) CertifiedCompanyMark()
            }
            if (location != null) {
                Spacer(Modifier.height(4.dp))
                BasicText(location, style = TextStyle(color = SloganInk, fontSize = 13.sp))
            }
            Spacer(Modifier.height(if (compact) 10.dp else 14.dp))
            OpportunityTypeTag(type)
        }
    }
}

@Composable
private fun OpportunityTypeTag(type: String) {
    Box(
        Modifier
            .background(SoftSurface, RoundedCornerShape(6.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp),
    ) {
        BasicText(type, style = TextStyle(color = Ink, fontSize = 11.sp, fontWeight = FontWeight.Medium))
    }
}

@Composable
private fun CertifiedCompanyMark() {
    Canvas(Modifier.size(14.dp).semantics { contentDescription = "Entreprise certifiée" }) {
        drawCircle(Ink)
        drawLine(
            Color.White,
            Offset(size.width * 0.24f, size.height * 0.52f),
            Offset(size.width * 0.43f, size.height * 0.7f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawLine(
            Color.White,
            Offset(size.width * 0.43f, size.height * 0.7f),
            Offset(size.width * 0.76f, size.height * 0.32f),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}
