package com.rigobertods.rdscore.ui.util

import com.rigobertods.rdscore.R

/**
 * Mapa de códigos de backend a recursos de string de Android.
 * Permite la traducción en el cliente basada en códigos estándar.
 */
object MessageMapper {
    private val codeMap = mapOf(
        // Errores de Auth
        "AUTH_MISSING_FIELDS" to R.string.error_missing_fields,
        "AUTH_USERNAME_TAKEN" to R.string.error_username_taken,
        "AUTH_USERNAME_EXISTS" to R.string.error_username_taken,
        "AUTH_EMAIL_REGISTERED" to R.string.error_email_registered,
        "AUTH_EMAIL_EXISTS" to R.string.error_email_registered,
        "AUTH_MISSING_CREDENTIALS" to R.string.error_missing_credentials,
        "AUTH_INVALID_CREDENTIALS" to R.string.error_invalid_credentials,
        "AUTH_EMAIL_REQUIRED" to R.string.error_email_required,
        "AUTH_USER_NOT_FOUND" to R.string.error_user_not_found,
        "AUTH_NO_PERMISSION" to R.string.error_no_permission,
        "AUTH_PERMISSION_DENIED" to R.string.error_no_permission,
        "AUTH_MISSING_DATA" to R.string.error_missing_data,
        "AUTH_CODE_INVALID" to R.string.error_code_invalid_expired,
        "AUTH_TOKEN_INVALID" to R.string.error_token_invalid,
        "AUTH_TOKEN_EXPIRED" to R.string.error_token_invalid_expired,
        "AUTH_CURRENT_PASSWORD_WRONG" to R.string.error_current_password_wrong,
        "AUTH_EMAIL_SEND_FAILED" to R.string.error_email_send_failed,
        
        // Data Errors
        "DATA_INVALID_FORMAT" to R.string.error_data_invalid_format,
        "DATA_NOT_FOUND" to R.string.error_data_not_found,
        "DATA_FETCH_ERROR" to R.string.error_data_fetch,
        
        // Server Errors
        "SERVER_INTERNAL_ERROR" to R.string.error_server_internal,

        // Éxitos de Auth
        "AUTH_ACCOUNT_CREATED" to R.string.success_account_created,
        "AUTH_REGISTER_SUCCESS" to R.string.success_account_created,
        "AUTH_LOGIN_SUCCESS" to R.string.success_login,
        "AUTH_LOGOUT_SUCCESS" to R.string.success_logout,
        "AUTH_EMAIL_SENT" to R.string.msg_email_sent,
        "AUTH_PASSWORD_RESET_EMAIL_SENT" to R.string.msg_email_sent,
        "AUTH_EMAIL_SENT_IF_REGISTERED" to R.string.msg_email_sent_if_registered,
        "AUTH_ACCOUNT_DELETED" to R.string.msg_account_deleted,
        "AUTH_PASSWORD_UPDATED" to R.string.msg_password_updated,
        "AUTH_PASSWORD_RESET_SUCCESS" to R.string.msg_password_updated,
        "AUTH_PASSWORD_CHANGED" to R.string.msg_password_updated,
        
        // Predicciones
        "PREDICTION_NOT_AVAILABLE" to R.string.prediction_not_available
    )

    fun getResourceId(code: String): Int? {
        return codeMap[code]
    }
}
