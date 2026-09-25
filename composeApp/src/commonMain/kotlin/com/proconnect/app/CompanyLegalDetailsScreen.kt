package com.proconnect.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class CompanyLegalDetails(
    val legalIdentificationNumber: String,
    val legalForm: String,
    val creationYear: Int,
    val businessSector: String,
)

data class CompanyLegalDetailsDraft(
    val legalIdentificationNumber: String,
    val legalForm: String,
    val creationYear: String,
    val businessSector: String,
)

@Composable
fun CompanyLegalDetailsScreen(
    onBack: () -> Unit,
    onNext: (CompanyLegalDetails) -> Unit = {},
    onSaveAndExit: (CompanyLegalDetailsDraft) -> Unit = {},
) {
    var legalIdentificationNumber by rememberSaveable { mutableStateOf("") }
    var legalForm by rememberSaveable { mutableStateOf("") }
    var creationYear by rememberSaveable { mutableStateOf("") }
    var businessSector by rememberSaveable { mutableStateOf("") }
    var legalVisited by remember { mutableStateOf(false) }
    var formVisited by remember { mutableStateOf(false) }
    var yearVisited by remember { mutableStateOf(false) }
    var sectorVisited by remember { mutableStateOf(false) }
    var showLegalFormPicker by remember { mutableStateOf(false) }
    var showSectorPicker by remember { mutableStateOf(false) }
    val yearFocus = remember { FocusRequester() }
    val yearNumber = creationYear.toIntOrNull()
    val yearValid = creationYear.length == 4 && yearNumber != null && yearNumber in 1800..2100
    val canContinue = legalIdentificationNumber.isNotBlank() && legalForm.isNotBlank() &&
        yearValid && businessSector.isNotBlank()
    val draft = CompanyLegalDetailsDraft(
        legalIdentificationNumber.trim(), legalForm, creationYear, businessSector,
    )
    val currentDetails = if (canContinue) CompanyLegalDetails(
        legalIdentificationNumber.trim(), legalForm, yearNumber!!, businessSector,
    ) else null

    if (showLegalFormPicker) CompanyOptionPicker(
        title = "Forme juridique",
        options = listOf("SARL", "SA", "SAS", "Entreprise individuelle"),
        onDismiss = { showLegalFormPicker = false },
        onSelect = { legalForm = it; showLegalFormPicker = false },
    )
    if (showSectorPicker) CompanyOptionPicker(
        title = "Secteur d'activité",
        options = listOf("Technologie", "Finance", "Commerce", "Industrie", "Construction", "Santé", "Éducation", "Transport"),
        onDismiss = { showSectorPicker = false },
        onSelect = { businessSector = it; showSectorPicker = false },
    )

    BoxWithConstraints(
        Modifier.fillMaxSize().background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing).imePadding(),
    ) {
        val compact = maxHeight < 720.dp
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(if (compact) 8.dp else 16.dp))
            CompanyRegistrationHeader(onBack, "Retour aux informations de base")
            Spacer(Modifier.height(if (compact) 10.dp else 16.dp))
            CompanyRegistrationStepper(currentStep = 2)
            Spacer(Modifier.height(if (compact) 16.dp else 20.dp))
            Column(Modifier.widthIn(max = 390.dp).fillMaxWidth()) {
                BasicText(
                    "Détails légaux",
                    style = TextStyle(color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold),
                )
                Spacer(Modifier.height(6.dp))
                BasicText(
                    "Ces informations nous permettent de vérifier\nl’existence légale de votre entreprise.",
                    style = TextStyle(color = SloganInk, fontSize = 15.sp, lineHeight = 21.sp),
                )
                Spacer(Modifier.height(if (compact) 18.dp else 22.dp))
                CompanyFormField(
                    label = "Numéro d’identification légale (RCCM, etc.) *",
                    placeholder = "Ex. RCCM/CI-ABJ-2023-B-12345",
                    value = legalIdentificationNumber,
                    onValueChange = { legalIdentificationNumber = it },
                    glyph = CompanyGlyphType.Building,
                    error = if (legalVisited && legalIdentificationNumber.isBlank()) "Numéro d’identification requis" else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { yearFocus.requestFocus() }),
                    onBlur = { legalVisited = true },
                )
                Spacer(Modifier.height(12.dp))
                CompanySelectorField(
                    label = "Forme juridique *", placeholder = "Sélectionnez la forme juridique",
                    value = legalForm, glyph = CompanyGlyphType.Building,
                    error = if (formVisited && legalForm.isBlank()) "Sélectionnez une forme juridique" else null,
                    onClick = { formVisited = true; showLegalFormPicker = true },
                )
                Spacer(Modifier.height(12.dp))
                CompanyFormField(
                    label = "Année de création *", placeholder = "Ex. 2020",
                    value = creationYear,
                    onValueChange = { creationYear = it.filter(Char::isDigit).take(4) },
                    glyph = CompanyGlyphType.Calendar,
                    error = when {
                        !yearVisited -> null
                        creationYear.isBlank() -> "Année de création requise"
                        !yearValid -> "Année de création invalide"
                        else -> null
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { yearVisited = true }),
                    focusRequester = yearFocus, onBlur = { yearVisited = true },
                )
                Spacer(Modifier.height(12.dp))
                CompanySelectorField(
                    label = "Secteur d'activité *", placeholder = "Sélectionnez votre secteur",
                    value = businessSector, glyph = CompanyGlyphType.Briefcase,
                    error = if (sectorVisited && businessSector.isBlank()) "Sélectionnez un secteur d’activité" else null,
                    onClick = { sectorVisited = true; showSectorPicker = true },
                )
                Spacer(Modifier.height(if (compact) 26.dp else 44.dp))
                LoginActionButton("Suivant", enabled = canContinue) {
                    currentDetails?.let(onNext)
                }
                Spacer(Modifier.height(16.dp))
                BasicText(
                    "Enregistrer et quitter",
                    modifier = Modifier.fillMaxWidth().clickable { onSaveAndExit(draft) },
                    style = TextStyle(
                        color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline, textAlign = TextAlign.Center,
                    ),
                )
                Spacer(Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun CompanyOptionPicker(
    title: String,
    options: List<String>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.widthIn(max = 340.dp).fillMaxWidth()
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(18.dp),
        ) {
            BasicText(title, style = TextStyle(color = Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(12.dp))
            Column(Modifier.heightIn(max = 320.dp).verticalScroll(rememberScrollState())) {
                options.forEach { option ->
                    BasicText(
                        option,
                        modifier = Modifier.fillMaxWidth().clickable { onSelect(option) }
                            .padding(vertical = 13.dp, horizontal = 4.dp),
                        style = TextStyle(color = Ink, fontSize = 15.sp),
                    )
                }
            }
        }
    }
}
