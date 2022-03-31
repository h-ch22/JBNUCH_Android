package kr.ac.jbnu.ch.userManagement.models

enum class UserManagementResultModel {
    success, networkError, userNotFound, userTokenExpired, tooManyRequests, invalidAPIKey, appNotAuthorized, keychainError, internalError, operationNotAllowed, wrongPassword, invalidEmail, userDisabled, unknownError, EmailAlreadyInUse, weakPassword, passwordNotEquals, emptyField, legacyUser, IDCardValidationFailed, registeredUser
}