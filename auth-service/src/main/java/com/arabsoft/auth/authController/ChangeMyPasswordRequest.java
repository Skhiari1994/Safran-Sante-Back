package com.arabsoft.auth.authController;

/** Changement du mot de passe par l'utilisateur connecté (écran « Gérer mon compte »). */
public record ChangeMyPasswordRequest(String currentPassword, String newPassword, String confirmationPassword) {
}
