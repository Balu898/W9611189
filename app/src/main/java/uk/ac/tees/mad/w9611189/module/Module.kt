package uk.ac.tees.mad.w9611189.module

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.w9611189.dataLayer.AuthRepository
import uk.ac.tees.mad.w9611189.dataLayer.AuthRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepositoryImpl(firebaseAuth: FirebaseAuth) : AuthRepository {
        return AuthRepositoryImpl(firebaseAuth = firebaseAuth )
    }
}