# ProConnect

Application Kotlin Multiplatform avec une interface partagée en Compose Multiplatform pour Android et iOS.

Le projet contient les trois écrans d'onboarding, l'écran de connexion, la création de compte et les deux premières étapes du parcours entreprise. Un glissement horizontal permet de parcourir l'onboarding et d'ouvrir la connexion. Le lien « Créer un compte » ouvre le formulaire d'inscription ; le choix « Je suis une entreprise » ouvre « Informations de base ». Le bouton « Suivant » ouvre « Détails légaux » lorsque les quatre informations de base sont valides. Chaque flèche de retour revient à l'écran précédent ; « Se connecter » depuis la création de compte revient à la connexion.

Les champs, validations, sélecteurs et liens de navigation ci-dessus sont reliés. Le sélecteur Pays / Ville permet une saisie dans une fenêtre dédiée en attendant une source de données géographiques. L'authentification, la création finale du compte, la sauvegarde et l'étape 3 « Vérification » ne sont pas encore implémentées : leurs actions sont préparées par des callbacks, sans service ni stockage. L'application n'a pas encore été exécutée sur un appareil Android dans cet environnement, car le SDK Android 35 et les Platform Tools manquent.

## Lancer sur Android

1. Ouvrir ce dossier dans Android Studio.
2. Installer le SDK Android 35 et les Platform Tools depuis **Tools > SDK Manager**.
3. Connecter un téléphone avec le débogage USB activé, sélectionner l'appareil et lancer la configuration `composeApp`.

Le code des écrans se trouve dans `composeApp/src/commonMain` et est partagé avec l'hôte iOS situé dans `iosApp`.
