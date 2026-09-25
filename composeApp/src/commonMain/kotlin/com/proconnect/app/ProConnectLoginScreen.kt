package com.proconnect.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal val LoginBorder = Color(0xFFDCE4EE)
internal val LoginMuted = Color(0xFF718199)
internal val LoginError = Color(0xFF8A3440)

@Composable
fun ProConnectLoginScreen(
    onBack: () -> Unit,
    onSignIn: (String, String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {},
    onAppleSignIn: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
) {
    var identity by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var identityVisited by remember { mutableStateOf(false) }
    var passwordVisited by remember { mutableStateOf(false) }
    val passwordFocus = remember { FocusRequester() }
    val identityValid = identity.isNotBlank() && identity.trim().none { it.isWhitespace() } &&
        (!identity.contains('@') || Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$").matches(identity.trim()))
    val canSignIn = identityValid && password.isNotBlank()
    val identityError = when {
        !identityVisited -> null
        identity.isBlank() -> "Adresse requise"
        !identityValid -> "E-mail invalide"
        else -> null
    }
    val passwordError = if (passwordVisited && password.isBlank()) "Mot de passe requis" else null

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
            Spacer(Modifier.height(if (compact) 24.dp else 40.dp))
            Box(Modifier.widthIn(max = 390.dp).fillMaxWidth().height(48.dp)) {
                Box(
                    Modifier.size(44.dp).align(Alignment.CenterStart)
                        .clickable(onClick = onBack)
                        .semantics { contentDescription = "Retour à l’onboarding" },
                    contentAlignment = Alignment.CenterStart,
                ) { LoginGlyph(LoginGlyphType.Back, Modifier.size(23.dp)) }
            }
            ProConnectLogo(Modifier.size(width = if (compact) 61.dp else 70.dp, height = if (compact) 50.dp else 58.dp))
            Spacer(Modifier.height(6.dp))
            BasicText(
                "ProConnect",
                style = TextStyle(
                    color = Ink, fontSize = 34.sp, fontWeight = FontWeight.Bold,
                    letterSpacing = (-1.1).sp,
                ),
            )
            Spacer(Modifier.height(if (compact) 25.dp else 34.dp))
            Column(Modifier.widthIn(max = 390.dp).fillMaxWidth()) {
                BasicText(
                    "Connexion",
                    style = TextStyle(color = Ink, fontSize = 26.sp, fontWeight = FontWeight.Bold),
                )
                Spacer(Modifier.height(8.dp))
                BasicText(
                    "Bienvenue ! Connectez-vous à votre compte\npour accéder à toutes les opportunités.",
                    style = TextStyle(color = SloganInk, fontSize = 16.sp, lineHeight = 23.sp),
                )
                Spacer(Modifier.height(if (compact) 20.dp else 32.dp))
                LoginTextField(
                    value = identity,
                    onValueChange = { identity = it },
                    placeholder = "Adresse e-mail ou identifiant",
                    glyph = LoginGlyphType.Mail,
                    error = identityError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                    onBlur = { identityVisited = true },
                )
                Spacer(Modifier.height(if (compact) 12.dp else 14.dp))
                LoginTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Mot de passe",
                    glyph = LoginGlyphType.Lock,
                    error = passwordError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        passwordVisited = true
                        if (canSignIn) onSignIn(identity.trim(), password)
                    }),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    focusRequester = passwordFocus,
                    onBlur = { passwordVisited = true },
                    trailing = {
                        Box(
                            Modifier.size(42.dp).clickable { passwordVisible = !passwordVisible }
                                .semantics {
                                    contentDescription = if (passwordVisible) "Masquer le mot de passe" else "Afficher le mot de passe"
                                },
                            contentAlignment = Alignment.Center,
                        ) { LoginGlyph(LoginGlyphType.Eye, Modifier.size(21.dp), LoginMuted) }
                    },
                )
                Spacer(Modifier.height(if (compact) 8.dp else 10.dp))
                BasicText(
                    "Mot de passe oublié ?",
                    modifier = Modifier.align(Alignment.End).clickable(onClick = onForgotPassword),
                    style = TextStyle(color = SloganInk, fontSize = 14.sp, fontWeight = FontWeight.Medium),
                )
                Spacer(Modifier.height(if (compact) 20.dp else 28.dp))
                LoginActionButton("Se connecter", enabled = canSignIn) {
                    onSignIn(identity.trim(), password)
                }
                Spacer(Modifier.height(if (compact) 22.dp else 28.dp))
                LoginDivider()
                Spacer(Modifier.height(if (compact) 18.dp else 22.dp))
                SocialLoginButton("Continuer avec Google", LoginGlyphType.Google, onGoogleSignIn)
                Spacer(Modifier.height(if (compact) 12.dp else 14.dp))
                SocialLoginButton("Continuer avec Apple", LoginGlyphType.Apple, onAppleSignIn)
                Spacer(Modifier.height(if (compact) 18.dp else 40.dp))
                BasicText(
                    "Vous n'avez pas de compte ?",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(color = SloganInk, fontSize = 14.sp, textAlign = TextAlign.Center),
                )
                Spacer(Modifier.height(if (compact) 6.dp else 8.dp))
                BasicText(
                    "Créer un compte",
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onCreateAccount),
                    style = TextStyle(
                        color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline, textAlign = TextAlign.Center,
                    ),
                )
                Spacer(Modifier.height(if (compact) 16.dp else 28.dp))
            }
        }
    }
}

@Composable
internal fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    glyph: LoginGlyphType,
    error: String?,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    onBlur: () -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusRequester: FocusRequester = FocusRequester(),
    enabled: Boolean = true,
    trailing: @Composable (() -> Unit)? = null,
) {
    var focused by remember { mutableStateOf(false) }
    var hadFocus by remember { mutableStateOf(false) }
    val border = when {
        error != null -> LoginError
        focused -> SloganInk
        else -> LoginBorder
    }
    Column {
        Row(
            Modifier.fillMaxWidth().height(54.dp)
                .border(if (focused || error != null) 1.5.dp else 1.dp, border, RoundedCornerShape(14.dp))
                .padding(start = 14.dp, end = if (trailing == null) 14.dp else 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LoginGlyph(glyph, Modifier.size(20.dp), if (enabled) Ink else LoginMuted)
            Spacer(Modifier.width(13.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f).focusRequester(focusRequester)
                    .onFocusChanged {
                        focused = it.isFocused
                        if (it.isFocused) hadFocus = true
                        else if (hadFocus) onBlur()
                    },
                enabled = enabled,
                singleLine = true,
                textStyle = TextStyle(color = if (enabled) Ink else LoginMuted, fontSize = 15.sp),
                cursorBrush = SolidColor(Ink),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                decorationBox = { innerTextField ->
                    Box {
                        if (value.isEmpty()) BasicText(
                            placeholder,
                            style = TextStyle(color = LoginMuted, fontSize = 15.sp),
                        )
                        innerTextField()
                    }
                },
            )
            trailing?.invoke()
        }
        if (error != null) {
            Spacer(Modifier.height(5.dp))
            BasicText(error, style = TextStyle(color = LoginError, fontSize = 12.sp))
        }
    }
}

@Composable
internal fun LoginActionButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val background = when {
        !enabled -> Ink.copy(alpha = 0.72f)
        pressed -> Color(0xFF354554)
        else -> Ink
    }
    Box(
        Modifier.fillMaxWidth().height(48.dp).background(background, RoundedCornerShape(24.dp))
            .clickable(interactionSource = interaction, indication = null, enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(text, style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium))
    }
}

@Composable
private fun LoginDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.weight(1f).height(1.dp).background(LoginBorder))
        BasicText("ou", modifier = Modifier.padding(horizontal = 16.dp), style = TextStyle(color = SloganInk, fontSize = 14.sp))
        Box(Modifier.weight(1f).height(1.dp).background(LoginBorder))
    }
}

@Composable
private fun SocialLoginButton(label: String, glyph: LoginGlyphType, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    Box(
        Modifier.fillMaxWidth().height(54.dp)
            .background(if (pressed) Color(0xFFF4F6F8) else Color.White, RoundedCornerShape(27.dp))
            .border(1.dp, LoginBorder, RoundedCornerShape(27.dp))
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        LoginGlyph(glyph, Modifier.align(Alignment.CenterStart).padding(start = 46.dp).size(22.dp))
        BasicText(
            label,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Medium),
        )
    }
}

internal enum class LoginGlyphType { Back, Person, Mail, Lock, Eye, Google, Apple }

@Composable
internal fun LoginGlyph(type: LoginGlyphType, modifier: Modifier, tint: Color = Ink) {
    Canvas(modifier) {
        val sx = size.width / 24f
        val sy = size.height / 24f
        val stroke = Stroke(1.8f * sx, cap = StrokeCap.Round)
        fun line(x1: Float, y1: Float, x2: Float, y2: Float, color: Color = tint) {
            drawLine(color, Offset(x1 * sx, y1 * sy), Offset(x2 * sx, y2 * sy), strokeWidth = 1.8f * sx, cap = StrokeCap.Round)
        }
        when (type) {
            LoginGlyphType.Back -> {
                line(20f, 12f, 4f, 12f)
                line(4f, 12f, 11f, 5f)
                line(4f, 12f, 11f, 19f)
            }
            LoginGlyphType.Person -> {
                drawCircle(tint, radius = 4f * sx, center = Offset(12f * sx, 6f * sy), style = stroke)
                drawArc(tint, 190f, 160f, false, Offset(2f * sx, 12f * sy), Size(20f * sx, 16f * sy), style = stroke)
            }
            LoginGlyphType.Mail -> {
                drawRoundRect(tint, Offset(2f * sx, 4f * sy), Size(20f * sx, 16f * sy),
                    androidx.compose.ui.geometry.CornerRadius(2f * sx), style = stroke)
                line(2.5f, 5f, 12f, 13f)
                line(21.5f, 5f, 12f, 13f)
            }
            LoginGlyphType.Lock -> {
                drawRoundRect(tint, Offset(4f * sx, 10f * sy), Size(16f * sx, 12f * sy),
                    androidx.compose.ui.geometry.CornerRadius(2f * sx), style = stroke)
                drawArc(tint, 180f, 180f, false, Offset(7f * sx, 2f * sy), Size(10f * sx, 16f * sy), style = stroke)
                drawCircle(tint, radius = 1.3f * sx, center = Offset(12f * sx, 16f * sy))
            }
            LoginGlyphType.Eye -> {
                val eye = Path().apply {
                    moveTo(2f * sx, 12f * sy)
                    cubicTo(7f * sx, 4f * sy, 17f * sx, 4f * sy, 22f * sx, 12f * sy)
                    cubicTo(17f * sx, 20f * sy, 7f * sx, 20f * sy, 2f * sx, 12f * sy)
                    close()
                }
                drawPath(eye, tint, style = stroke)
                drawCircle(tint, radius = 2.6f * sx, center = Offset(12f * sx, 12f * sy), style = stroke)
            }
            LoginGlyphType.Google -> {
                val ring = Stroke(4f * sx, cap = StrokeCap.Butt)
                val topLeft = Offset(3.5f * sx, 3.5f * sy)
                val arcSize = Size(17f * sx, 17f * sy)
                drawArc(Color(0xFF4285F4), -42f, 85f, false, topLeft, arcSize, style = ring)
                drawArc(Color(0xFF34A853), 43f, 80f, false, topLeft, arcSize, style = ring)
                drawArc(Color(0xFFFBBC05), 123f, 72f, false, topLeft, arcSize, style = ring)
                drawArc(Color(0xFFEA4335), 195f, 110f, false, topLeft, arcSize, style = ring)
                drawLine(Color(0xFF4285F4), Offset(12f * sx, 12f * sy), Offset(21f * sx, 12f * sy), 4f * sx)
            }
            LoginGlyphType.Apple -> {
                val body = Path().apply {
                    moveTo(12f * sx, 7.5f * sy)
                    cubicTo(9f * sx, 5f * sy, 5f * sx, 7f * sy, 4f * sx, 11f * sy)
                    cubicTo(2.5f * sx, 16f * sy, 6f * sx, 22f * sy, 9f * sx, 22f * sy)
                    cubicTo(10.7f * sx, 22f * sy, 11f * sx, 21f * sy, 12f * sx, 21f * sy)
                    cubicTo(13f * sx, 21f * sy, 13.7f * sx, 22f * sy, 15.5f * sx, 22f * sy)
                    cubicTo(18f * sx, 22f * sy, 20f * sx, 18f * sy, 21f * sx, 15f * sy)
                    cubicTo(18f * sx, 13.5f * sy, 18f * sx, 10f * sy, 20f * sx, 8.5f * sy)
                    cubicTo(17f * sx, 6f * sy, 14f * sx, 6f * sy, 12f * sx, 7.5f * sy)
                    close()
                }
                val leaf = Path().apply {
                    moveTo(12f * sx, 6f * sy)
                    cubicTo(12.5f * sx, 2.5f * sy, 15f * sx, 1f * sy, 18f * sx, 1f * sy)
                    cubicTo(18f * sx, 4f * sy, 15.5f * sx, 6f * sy, 12f * sx, 6f * sy)
                    close()
                }
                drawPath(body, Color.Black)
                drawPath(leaf, Color.Black)
            }
        }
    }
}
