# ProConnect

Application Kotlin Multiplatform avec une interface partagée en Compose Multiplatform pour Android et iOS.

Cette première étape contient uniquement l'écran d'accueil : logo, slogan, photographie architecturale et indicateurs de pagination. Les autres écrans et l'authentification ne sont pas encore implémentés.

## Lancer sur Android

1. Ouvrir ce dossier dans Android Studio.
2. Installer le SDK Android 35 et les Platform Tools depuis **Tools > SDK Manager**.
3. Connecter un téléphone avec le débogage USB activé, sélectionner l'appareil et lancer la configuration `composeApp`.

Le code de l'écran se trouve dans `composeApp/src/commonMain` et est partagé avec l'hôte iOS situé dans `iosApp`.
