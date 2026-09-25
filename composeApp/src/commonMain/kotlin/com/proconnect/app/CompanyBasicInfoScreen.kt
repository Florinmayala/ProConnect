package com.proconnect.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class CompanyLocation(val country: String, val city: String)

data class CompanyBasicInfo(
    val companyName: String,
    val professionalEmail: String,
    val phoneNumber: String,
    val country: String,
    val city: String,
)

@Composable
fun CompanyBasicInfoScreen(
    onBack: () -> Unit,
    onSelectLocation: (((CompanyLocation) -> Unit) -> Unit)? = null,
    onNext: (CompanyBasicInfo) -> Unit = {},
    onSaveAndExit: (CompanyBasicInfo) -> Unit = {},
) {
    var companyName by rememberSaveable { mutableStateOf("") }
    var professionalEmail by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var nameVisited by remember { mutableStateOf(false) }
    var emailVisited by remember { mutableStateOf(false) }
    var phoneVisited by remember { mutableStateOf(false) }
    var locationVisited by remember { mutableStateOf(false) }
    var showLocationPicker by remember { mutableStateOf(false) }
    val emailFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    val emailValid = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(professionalEmail.trim())
    val phoneDigits = phoneNumber.count { it.isDigit() }
    val phoneValid = phoneDigits in 7..15 && Regex("^\\+?[0-9 ()-]{7,24}$").matches(phoneNumber.trim())
    val locationValid = country.isNotBlank() && city.isNotBlank()
    val canContinue = companyName.isNotBlank() && emailValid && phoneValid && locationValid
    val currentInfo = CompanyBasicInfo(companyName.trim(), professionalEmail.trim(), phoneNumber.trim(), country, city)

    if (showLocationPicker) CompanyLocationPicker(
        initialCountry = country,
        initialCity = city,
        onDismiss = { showLocationPicker = false },
        onSelect = { selected ->
            country = selected.country
            city = selected.city
            showLocationPicker = false
        },
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
            CompanyRegistrationHeader(onBack, "Retour à la création de compte")
            Spacer(Modifier.height(if (compact) 10.dp else 16.dp))
            CompanyRegistrationStepper(currentStep = 1)
            Spacer(Modifier.height(if (compact) 16.dp else 20.dp))
            Column(Modifier.widthIn(max = 390.dp).fillMaxWidth()) {
                BasicText(
                    "Informations de base",
                    style = TextStyle(color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold),
                )
                Spacer(Modifier.height(6.dp))
                BasicText(
                    "Commençons par les informations principales\nde votre entreprise.",
                    style = TextStyle(color = SloganInk, fontSize = 15.sp, lineHeight = 21.sp),
                )
                Spacer(Modifier.height(if (compact) 18.dp else 22.dp))
                CompanyFormField(
                    label = "Nom de l'entreprise *", placeholder = "Ex. ProConnect SARL",
                    value = companyName, onValueChange = { companyName = it },
                    glyph = CompanyGlyphType.Building,
                    error = if (nameVisited && companyName.isBlank()) "Nom de l'entreprise requis" else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
                    onBlur = { nameVisited = true },
                )
                Spacer(Modifier.height(12.dp))
                CompanyFormField(
                    label = "Adresse e-mail professionnelle *", placeholder = "contact@votreentreprise.com",
                    value = professionalEmail, onValueChange = { professionalEmail = it },
                    glyph = CompanyGlyphType.Mail,
                    error = when {
                        !emailVisited -> null
                        professionalEmail.isBlank() -> "Adresse e-mail requise"
                        !emailValid -> "E-mail invalide"
                        else -> null
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { phoneFocus.requestFocus() }),
                    focusRequester = emailFocus, onBlur = { emailVisited = true },
                )
                Spacer(Modifier.height(12.dp))
                CompanyFormField(
                    label = "Numéro de téléphone *", placeholder = "+225 07 00 00 00 00",
                    value = phoneNumber, onValueChange = { phoneNumber = it },
                    glyph = CompanyGlyphType.Phone,
                    error = when {
                        !phoneVisited -> null
                        phoneNumber.isBlank() -> "Numéro de téléphone requis"
                        !phoneValid -> "Numéro de téléphone invalide"
                        else -> null
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { phoneVisited = true }),
                    focusRequester = phoneFocus, onBlur = { phoneVisited = true },
                )
                Spacer(Modifier.height(12.dp))
                CompanySelectorField(
                    label = "Pays / Ville *", placeholder = "Sélectionnez votre pays et ville",
                    value = if (locationValid) "$country / $city" else "",
                    glyph = CompanyGlyphType.Pin,
                    error = if (locationVisited && !locationValid) "Sélectionnez un pays et une ville" else null,
                    onClick = {
                        locationVisited = true
                        if (onSelectLocation == null) showLocationPicker = true
                        else onSelectLocation { selected ->
                            country = selected.country
                            city = selected.city
                        }
                    },
                )
                Spacer(Modifier.height(if (compact) 26.dp else 44.dp))
                LoginActionButton("Suivant", enabled = canContinue) { onNext(currentInfo) }
                Spacer(Modifier.height(16.dp))
                BasicText(
                    "Enregistrer et quitter",
                    modifier = Modifier.fillMaxWidth().clickable { onSaveAndExit(currentInfo) },
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
private fun CompanyLocationPicker(
    initialCountry: String,
    initialCity: String,
    onDismiss: () -> Unit,
    onSelect: (CompanyLocation) -> Unit,
) {
    var selectedCountry by remember(initialCountry) { mutableStateOf(initialCountry) }
    var selectedCity by remember(initialCity) { mutableStateOf(initialCity) }
    Dialog(onDismissRequest = onDismiss) {
        Column(
            Modifier.widthIn(max = 340.dp).fillMaxWidth()
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(18.dp),
        ) {
            BasicText("Pays / Ville", style = TextStyle(color = Ink, fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(16.dp))
            CompanyFormField(
                label = "Pays *", placeholder = "Saisissez le pays",
                value = selectedCountry, onValueChange = { selectedCountry = it },
                glyph = CompanyGlyphType.Pin, error = null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions.Default, onBlur = {},
            )
            Spacer(Modifier.height(12.dp))
            CompanyFormField(
                label = "Ville *", placeholder = "Saisissez la ville",
                value = selectedCity, onValueChange = { selectedCity = it },
                glyph = CompanyGlyphType.Pin, error = null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions.Default, onBlur = {},
            )
            Spacer(Modifier.height(20.dp))
            LoginActionButton(
                "Valider",
                enabled = selectedCountry.isNotBlank() && selectedCity.isNotBlank(),
            ) { onSelect(CompanyLocation(selectedCountry.trim(), selectedCity.trim())) }
        }
    }
}

@Composable
internal fun CompanyRegistrationHeader(onBack: () -> Unit, backDescription: String) {
    Box(Modifier.widthIn(max = 390.dp).fillMaxWidth().height(66.dp)) {
        Row(
            Modifier.align(Alignment.TopStart).height(44.dp)
                .clickable(onClick = onBack)
                .semantics { contentDescription = backDescription },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LoginGlyph(LoginGlyphType.Back, Modifier.size(19.dp))
            Spacer(Modifier.width(6.dp))
            BasicText("Retour", style = TextStyle(color = Ink, fontSize = 13.sp))
        }
        Column(
            Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProConnectLogo(Modifier.size(width = 44.dp, height = 36.dp))
            Spacer(Modifier.height(2.dp))
            BasicText(
                "ProConnect",
                style = TextStyle(color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
internal fun CompanyRegistrationStepper(currentStep: Int, totalSteps: Int = 5) {
    val labels = listOf("Informations\nde base", "Détails\nlégaux", "Vérification", "Configuration", "Terminé")
    val shown = totalSteps.coerceIn(1, labels.size)
    Column(Modifier.widthIn(max = 390.dp).fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            repeat(shown) { index ->
                Box(
                    Modifier.size(20.dp)
                        .background(if (index + 1 <= currentStep) Ink else Color(0xFFF1F4F7), RoundedCornerShape(50))
                        .border(1.dp, if (index + 1 <= currentStep) Ink else LoginBorder, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (index + 1 < currentStep || (index == 0 && currentStep == 1)) {
                        Canvas(Modifier.size(12.dp)) {
                            drawLine(Color.White, Offset(size.width * .12f, size.height * .52f),
                                Offset(size.width * .4f, size.height * .78f), 1.7.dp.toPx())
                            drawLine(Color.White, Offset(size.width * .4f, size.height * .78f),
                                Offset(size.width * .9f, size.height * .18f), 1.7.dp.toPx())
                        }
                    } else BasicText(
                        "${index + 1}",
                        style = TextStyle(
                            color = if (index + 1 == currentStep) Color.White else SloganInk,
                            fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        ),
                    )
                }
                if (index < shown - 1) {
                    Box(Modifier.weight(1f).height(1.dp).background(LoginBorder))
                }
            }
        }
        Spacer(Modifier.height(7.dp))
        Row(Modifier.fillMaxWidth()) {
            repeat(shown) { index ->
                BasicText(
                    labels[index],
                    modifier = Modifier.weight(1f),
                    style = TextStyle(
                        color = if (index + 1 == currentStep) Ink else LoginMuted,
                        fontSize = 10.sp, lineHeight = 13.sp, textAlign = TextAlign.Center,
                    ),
                )
            }
        }
    }
}

@Composable
internal fun CompanyFormField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    glyph: CompanyGlyphType,
    error: String?,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onBlur: () -> Unit,
    focusRequester: FocusRequester = FocusRequester(),
) {
    var focused by remember { mutableStateOf(false) }
    var hadFocus by remember { mutableStateOf(false) }
    Column {
        Row(
            Modifier.fillMaxWidth().height(58.dp)
                .border(
                    if (focused || error != null) 1.5.dp else 1.dp,
                    if (error != null) LoginError else if (focused) SloganInk else LoginBorder,
                    RoundedCornerShape(14.dp),
                ).padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompanyGlyph(glyph, Modifier.size(20.dp))
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                BasicText(
                    label,
                    style = TextStyle(color = SloganInk, fontSize = 11.sp, fontWeight = FontWeight.Bold),
                    maxLines = 1,
                )
                Spacer(Modifier.height(2.dp))
                BasicTextField(
                    value = value, onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).onFocusChanged {
                        focused = it.isFocused
                        if (it.isFocused) hadFocus = true else if (hadFocus) onBlur()
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = Ink, fontSize = 13.sp),
                    cursorBrush = SolidColor(Ink),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    decorationBox = { inner ->
                        Box {
                            if (value.isEmpty()) BasicText(
                                placeholder,
                                style = TextStyle(color = LoginMuted, fontSize = 13.sp),
                                maxLines = 1,
                            )
                            inner()
                        }
                    },
                )
            }
        }
        if (error != null) {
            Spacer(Modifier.height(5.dp))
            BasicText(error, style = TextStyle(color = LoginError, fontSize = 12.sp))
        }
    }
}

@Composable
internal fun CompanySelectorField(
    label: String,
    placeholder: String,
    value: String,
    glyph: CompanyGlyphType,
    error: String?,
    onClick: () -> Unit,
) {
    Column {
        Row(
            Modifier.fillMaxWidth().height(58.dp)
                .border(if (error != null) 1.5.dp else 1.dp,
                    if (error != null) LoginError else LoginBorder, RoundedCornerShape(14.dp))
                .clickable(onClick = onClick).padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompanyGlyph(glyph, Modifier.size(20.dp))
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                BasicText(label, style = TextStyle(color = SloganInk, fontSize = 11.sp, fontWeight = FontWeight.Bold))
                Spacer(Modifier.height(2.dp))
                BasicText(
                    if (value.isEmpty()) placeholder else value,
                    style = TextStyle(color = if (value.isEmpty()) LoginMuted else Ink, fontSize = 13.sp),
                    maxLines = 1,
                )
            }
            CompanyGlyph(CompanyGlyphType.Chevron, Modifier.size(16.dp))
        }
        if (error != null) {
            Spacer(Modifier.height(5.dp))
            BasicText(error, style = TextStyle(color = LoginError, fontSize = 12.sp))
        }
    }
}

internal enum class CompanyGlyphType { Building, Mail, Phone, Pin, Calendar, Briefcase, Chevron }

@Composable
internal fun CompanyGlyph(type: CompanyGlyphType, modifier: Modifier) {
    if (type == CompanyGlyphType.Mail) {
        LoginGlyph(LoginGlyphType.Mail, modifier)
        return
    }
    Canvas(modifier) {
        val sx = size.width / 24f
        val sy = size.height / 24f
        val stroke = Stroke(1.8f * sx, cap = StrokeCap.Round)
        fun line(x1: Float, y1: Float, x2: Float, y2: Float) =
            drawLine(Ink, Offset(x1 * sx, y1 * sy), Offset(x2 * sx, y2 * sy), 1.8f * sx, StrokeCap.Round)
        when (type) {
            CompanyGlyphType.Building -> {
                drawRoundRect(Ink, Offset(5f * sx, 4f * sy), Size(14f * sx, 18f * sy),
                    androidx.compose.ui.geometry.CornerRadius(1.5f * sx), style = stroke)
                line(9f, 8f, 11f, 8f); line(9f, 12f, 11f, 12f); line(9f, 16f, 11f, 16f)
                line(14f, 8f, 16f, 8f); line(14f, 12f, 16f, 12f); line(14f, 16f, 16f, 16f)
                line(11f, 22f, 11f, 19f); line(11f, 19f, 14f, 19f); line(14f, 19f, 14f, 22f)
            }
            CompanyGlyphType.Phone -> {
                val path = Path().apply {
                    moveTo(5f * sx, 3f * sy)
                    cubicTo(3f * sx, 3f * sy, 2f * sx, 5f * sy, 3f * sx, 9f * sy)
                    cubicTo(5f * sx, 16f * sy, 10f * sx, 21f * sy, 17f * sx, 22f * sy)
                    cubicTo(21f * sx, 23f * sy, 23f * sx, 20f * sy, 21f * sx, 18f * sy)
                    lineTo(17f * sx, 15f * sy); lineTo(14f * sx, 18f * sy)
                    cubicTo(10f * sx, 16f * sy, 8f * sx, 13f * sy, 6f * sx, 10f * sy)
                    lineTo(9f * sx, 7f * sy); close()
                }
                drawPath(path, Ink, style = stroke)
            }
            CompanyGlyphType.Pin -> {
                val path = Path().apply {
                    moveTo(12f * sx, 22f * sy)
                    cubicTo(9f * sx, 18f * sy, 4f * sx, 13f * sy, 4f * sx, 9f * sy)
                    cubicTo(4f * sx, 4f * sy, 7.5f * sx, 2f * sy, 12f * sx, 2f * sy)
                    cubicTo(16.5f * sx, 2f * sy, 20f * sx, 4f * sy, 20f * sx, 9f * sy)
                    cubicTo(20f * sx, 13f * sy, 15f * sx, 18f * sy, 12f * sx, 22f * sy)
                    close()
                }
                drawPath(path, Ink, style = stroke)
                drawCircle(Ink, 2.4f * sx, Offset(12f * sx, 9f * sy), style = stroke)
            }
            CompanyGlyphType.Calendar -> {
                drawRoundRect(Ink, Offset(3f * sx, 5f * sy), Size(18f * sx, 17f * sy),
                    androidx.compose.ui.geometry.CornerRadius(2f * sx), style = stroke)
                line(3f, 10f, 21f, 10f)
                line(8f, 2f, 8f, 7f); line(16f, 2f, 16f, 7f)
            }
            CompanyGlyphType.Briefcase -> {
                drawRoundRect(Ink, Offset(3f * sx, 8f * sy), Size(18f * sx, 13f * sy),
                    androidx.compose.ui.geometry.CornerRadius(2f * sx), style = stroke)
                line(9f, 8f, 9f, 4f); line(9f, 4f, 15f, 4f); line(15f, 4f, 15f, 8f)
                line(3f, 14f, 21f, 14f); line(11f, 13f, 11f, 16f); line(13f, 13f, 13f, 16f)
            }
            CompanyGlyphType.Chevron -> {
                line(5f, 9f, 12f, 16f); line(12f, 16f, 19f, 9f)
            }
            CompanyGlyphType.Mail -> Unit
        }
    }
}
