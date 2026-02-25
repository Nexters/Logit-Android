package com.useai.feature.account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.useai.core.data.repository.AccountRepository
import com.useai.core.navigation.LocalScreenProvider
import com.useai.core.navigation.ScreenProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.security.SecureRandom

@Parcelize
data object LoginScreen : Screen {
    data class State(
        val eventSink: (Event) -> Unit = {},
    ) : CircuitUiState

    sealed interface Event : CircuitUiEvent {
        object GoogleLoginClicked : Event
        object TermsClicked : Event
        object PrivacyClicked : Event
    }
}

class LoginPresenter @AssistedInject constructor(
    @Assisted private val navigator: Navigator,
    private val accountRepository: AccountRepository,
) : Presenter<LoginScreen.State> {
    @Composable
    override fun present(): LoginScreen.State {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val credentialManager = CredentialManager.create(context)
        val screenProvider = LocalScreenProvider.current

        return LoginScreen.State { event ->
            when (event) {
                LoginScreen.Event.GoogleLoginClicked -> {
                    Log.d(TAG, "GoogleLoginClicked")
                    scope.launch {
                        requestGoogleLogin(
                            credentialManager = credentialManager,
                            context = context,
                            scope = scope,
                            navigator = navigator,
                            screenProvider = screenProvider,
                        )
                    }
                }

                LoginScreen.Event.TermsClicked -> {
                    Log.d(TAG, "TermsClicked")
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_URL)))
                }

                LoginScreen.Event.PrivacyClicked -> {
                    Log.d(TAG, "PrivacyClicked")
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))
                }
            }
        }
    }

    private suspend fun requestGoogleLogin(
        credentialManager: CredentialManager,
        context: Context,
        scope: CoroutineScope,
        navigator: Navigator,
        screenProvider: ScreenProvider,
    ) {
        val nonce = generateNonce()
        val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = CLIENT_ID
        ).setNonce(nonce)
            .build()
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )

            handleSignInWithGoogleOption(
                result = result,
                onSuccess = { googleIdTokenCredential ->
                    scope.launch {
                        accountRepository.requestLogin(
                            idToken = googleIdTokenCredential.idToken,
                        ).onSuccess {
                            scope.launch {
                                accountRepository.setAccessToken(it.accessToken)
                                navigator.resetRoot(screenProvider.rootScreen())
                            }
                        }.onFailure {
                            Log.e(TAG, "Google login failed: $it")
                        }
                    }
                },
                onFailure = {
                    scope.launch {
                        signUp(
                            credentialManager = credentialManager,
                            context = context,
                            onSuccess = { googleIdTokenCredential ->
                                scope.launch {
                                    accountRepository.requestLogin(
                                        idToken = googleIdTokenCredential.idToken,
                                    ).onSuccess {
                                        accountRepository.setAccessToken(it.accessToken)
                                        navigator.resetRoot(screenProvider.rootScreen())
                                    }
                                }
                            },
                        )
                    }
                }
            )
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException", e)
            signUp(
                credentialManager = credentialManager,
                context = context,
                onSuccess = { googleIdTokenCredential ->
                    scope.launch {
                        accountRepository.requestLogin(
                            idToken = googleIdTokenCredential.idToken,
                        ).onSuccess {
                            accountRepository.setAccessToken(it.accessToken)
                            navigator.resetRoot(screenProvider.rootScreen())
                        }
                    }
                },
            )
        }
    }

    private fun generateNonce(length: Int = 16): String {
        val random = SecureRandom()
        val bytes = ByteArray(length)
        random.nextBytes(bytes)
        return bytes.toHexString()
    }

    // 바이??배열??16진수 문자?�로 변?�하???�장 ?�수
    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    private fun handleSignInWithGoogleOption(
        result: GetCredentialResponse,
        onSuccess: (GoogleIdTokenCredential) -> Unit,
        onFailure: () -> Unit,
    ) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        onSuccess(googleIdTokenCredential)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                        onFailure()
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                    onFailure()
                }
            }

            else -> {
                Log.e(TAG, "Unexpected type of credential")
                onFailure()
            }
        }
    }

    private suspend fun signUp(
        credentialManager: CredentialManager,
        context: Context,
        onSuccess: (GoogleIdTokenCredential) -> Unit,
    ) {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(CLIENT_ID)
            .build()
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            handleSignInWithGoogleOption(
                result = result,
                onSuccess = { googleIdTokenCredential ->
                    onSuccess(googleIdTokenCredential)
                },
                onFailure = {
                    Log.e(TAG, "Sign up failed")
                }
            )
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException", e)
        }
    }

    @AssistedFactory
    @CircuitInject(LoginScreen::class, ActivityRetainedComponent::class)
    fun interface Factory {
        fun create(
            navigator: Navigator,
        ): LoginPresenter
    }

    companion object {
        private val TAG = LoginPresenter::class.simpleName
        private const val CLIENT_ID = BuildConfig.GOOGLE_OAUTH_CLIENT_ID
        private const val TERMS_URL = "https://docs.logit.ai.kr/policys/tos"
        private const val PRIVACY_POLICY_URL = "https://docs.logit.ai.kr/policys/privacy-policy"
    }
}
