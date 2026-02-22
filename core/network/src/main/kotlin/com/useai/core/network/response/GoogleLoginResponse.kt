import com.useai.core.model.account.Login
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("is_new_user") val isNewUser: Boolean,
)

fun GoogleLoginResponse.toLogin(): Login = Login(
    accessToken = accessToken,
    refreshToken = refreshToken,
    isNewUser = isNewUser
)
