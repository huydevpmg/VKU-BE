import android.net.Uri
import com.dacs.vku.api.AuthResponse
import com.dacs.vku.api.AuthenticationAPI
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class AuthenticationRepository(private val authenticationAPI: AuthenticationAPI) {

    suspend fun signInWithGoogle(account: GoogleSignInAccount, param: (Any) -> Unit): AuthResponse? {
        val idToken = account.idToken ?: return null
        val call = authenticationAPI.verifyIdToken(idToken)
        val response = call.execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    fun getProfileImageUri(account: GoogleSignInAccount): Uri? {
        return account.photoUrl
    }
}
