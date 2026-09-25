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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AccountType { Candidate, Company }

@Composable
fun ProConnectCreateAccountScreen(
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onCompanySelected: () -> Unit = {},
    onCreateAccount: (AccountType, String, String, String) -> Unit = { _, _, _, _ -> },
) {
    var accountType by remember { mutableStateOf(AccountType.Candidate) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmation by remember { mutableStateOf("") }
    var acceptedTerms by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmationVisible by remember { mutableStateOf(false) }
    var nameVisited by remember { mutableStateOf(false) }
    var emailVisited by remember { mutableStateOf(false) }
    var passwordVisited by remember { mutableStateOf(false) }
    var confirmationVisited by remember { mutableStateOf(false) }
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmationFocus = remember { FocusRequester() }
    val emailValid = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(email.trim())
    val fieldsValid = name.isNotBlank() && emailValid && password.isNotBlank() &&
        confirmation.isNotBlank() && confirmation == password
    val canCreate = fieldsValid && acceptedTerms
    val nameError = if (nameVisited && name.isBlank()) "Nom complet requis" else null
    val emailError = when {
        !emailVisited -> null
        email.isBlank() -> "Adresse e-mail requise"
        !emailValid -> "E-mail invalide"
        else -> null
    }
    val passwordError = if (passwordVisited && password.isBlank()) "Mot de passe requis" else null
    val confirmationError = when {
        !confirmationVisited -> null
        confirmation.isBlank() -> "Confirmation requise"
        confirmation != password -> "Les mots de passe ne correspondent pas"
        else -> null
    }

    BoxWithConstraints(
        Modifier.fillMaxSize().background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing).imePadding(),
    ) {
        val compact = maxHeight < 850.dp
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(if (compact) 24.dp else 36.dp))
            Box(Modifier.widthIn(max = 390.dp).fillMaxWidth().height(48.dp)) {
                Box(
                    Modifier.size(44.dp).align(Alignment.CenterStart)
                        .clickable(onClick = onBack)
                        .semantics { contentDescription = "Retour à la connexion" },
                    contentAlignment = Alignment.CenterStart,
                ) { LoginGlyph(LoginGlyphType.Back, Modifier.size(23.dp)) }
            }
            Spacer(Modifier.height(if (compact) 16.dp else 24.dp))
            Column(Modifier.widthIn(max = 390.dp).fillMaxWidth()) {
                BasicText(
                    "Créer un compte",
                    style = TextStyle(color = Ink, fontSize = 26.sp, fontWeight = FontWeight.Bold),
                )
                Spacer(Modifier.height(8.dp))
                BasicText(
                    "Rejoignez ProConnect et accédez à des milliers\nd’opportunités.",
                    style = TextStyle(color = SloganInk, fontSize = 16.sp, lineHeight = 23.sp),
                )
                Spacer(Modifier.height(if (compact) 24.dp else 32.dp))
                AccountTypeSelector(accountType) {
                    accountType = it
                    if (it == AccountType.Company) onCompanySelected()
                }
                Spacer(Modifier.height(if (compact) 20.dp else 26.dp))
                LoginTextField(
                    value = name, onValueChange = { name = it }, placeholder = "Nom complet",
                    glyph = LoginGlyphType.Person, error = nameError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
                    onBlur = { nameVisited = true },
                )
                Spacer(Modifier.height(if (compact) 12.dp else 14.dp))
                LoginTextField(
                    value = email, onValueChange = { email = it }, placeholder = "Adresse e-mail",
                    glyph = LoginGlyphType.Mail, error = emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                    focusRequester = emailFocus, onBlur = { emailVisited = true },
                )
                Spacer(Modifier.height(if (compact) 12.dp else 14.dp))
                LoginTextField(
                    value = password, onValueChange = { password = it }, placeholder = "Mot de passe",
                    glyph = LoginGlyphType.Lock, error = passwordError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { confirmationFocus.requestFocus() }),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    focusRequester = passwordFocus, onBlur = { passwordVisited = true },
                    trailing = {
                        PasswordVisibilityButton(passwordVisible) { passwordVisible = !passwordVisible }
                    },
                )
                Spacer(Modifier.height(if (compact) 12.dp else 14.dp))
                LoginTextField(
                    value = confirmation, onValueChange = { confirmation = it },
                    placeholder = "Confirmer le mot de passe",
                    glyph = LoginGlyphType.Lock, error = confirmationError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        confirmationVisited = true
                        if (canCreate) onCreateAccount(accountType, name.trim(), email.trim(), password)
                    }),
                    visualTransformation = if (confirmationVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    focusRequester = confirmationFocus, onBlur = { confirmationVisited = true },
                    trailing = {
                        PasswordVisibilityButton(confirmationVisible) { confirmationVisible = !confirmationVisible }
                    },
                )
                Spacer(Modifier.height(if (compact) 20.dp else 28.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        Modifier.size(22.dp).border(1.5.dp, Ink, RoundedCornerShape(4.dp))
                            .clickable { acceptedTerms = !acceptedTerms }
                            .semantics { contentDescription = "Accepter les conditions d’utilisation et la politique de confidentialité" },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (acceptedTerms) Canvas(Modifier.size(14.dp)) {
                            drawLine(Ink, Offset(size.width * .12f, size.height * .52f),
                                Offset(size.width * .4f, size.height * .8f), 2.dp.toPx())
                            drawLine(Ink, Offset(size.width * .4f, size.height * .8f),
                                Offset(size.width * .9f, size.height * .16f), 2.dp.toPx())
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    val terms = buildAnnotatedString {
                        append("J’accepte les ")
                        pushStyle(SpanStyle(color = Ink, textDecoration = TextDecoration.Underline))
                        append("Conditions d’utilisation")
                        pop()
                        append(" et la ")
                        pushStyle(SpanStyle(color = Ink, textDecoration = TextDecoration.Underline))
                        append("Politique de confidentialité")
                        pop()
                        append(".")
                    }
                    BasicText(
                        terms,
                        modifier = Modifier.weight(1f).clickable { acceptedTerms = !acceptedTerms },
                        style = TextStyle(color = SloganInk, fontSize = 14.sp, lineHeight = 23.sp),
                    )
                }
                if (fieldsValid && !acceptedTerms && confirmationVisited) {
                    Spacer(Modifier.height(5.dp))
                    BasicText("Veuillez accepter les conditions", style = TextStyle(color = LoginError, fontSize = 12.sp))
                }
                Spacer(Modifier.height(if (compact) 22.dp else 28.dp))
                LoginActionButton("Créer mon compte", enabled = canCreate) {
                    onCreateAccount(accountType, name.trim(), email.trim(), password)
                }
                Spacer(Modifier.height(if (compact) 30.dp else 48.dp))
                BasicText(
                    "Vous avez déjà un compte ?",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(color = SloganInk, fontSize = 14.sp, textAlign = TextAlign.Center),
                )
                Spacer(Modifier.height(8.dp))
                BasicText(
                    "Se connecter",
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onSignIn),
                    style = TextStyle(
                        color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline, textAlign = TextAlign.Center,
                    ),
                )
                Spacer(Modifier.height(if (compact) 16.dp else 24.dp))
            }
        }
    }
}

@Composable
private fun AccountTypeSelector(selected: AccountType, onSelect: (AccountType) -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(40.dp)
            .background(Color(0xFFF2F5F8), RoundedCornerShape(13.dp)),
    ) {
        AccountSegment("Je suis un candidat", selected == AccountType.Candidate,
            Modifier.weight(1f)) { onSelect(AccountType.Candidate) }
        AccountSegment("Je suis une entreprise", selected == AccountType.Company,
            Modifier.weight(1f)) { onSelect(AccountType.Company) }
    }
}

@Composable
private fun AccountSegment(label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.fillMaxSize()
            .background(if (selected) Ink else Color.Transparent, RoundedCornerShape(13.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            label,
            style = TextStyle(
                color = if (selected) Color.White else Ink,
                fontSize = 13.sp, fontWeight = FontWeight.Medium,
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun PasswordVisibilityButton(visible: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.size(42.dp).clickable(onClick = onClick)
            .semantics { contentDescription = if (visible) "Masquer le mot de passe" else "Afficher le mot de passe" },
        contentAlignment = Alignment.Center,
    ) { LoginGlyph(LoginGlyphType.Eye, Modifier.size(21.dp), LoginMuted) }
}
