package uk.ac.tees.mad.w9611189.dataLayer

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email : String, password : String) : Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String) : Flow<Resource<AuthResult>>

}