package uk.ac.tees.mad.w9611189.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.ac.tees.mad.w9611189.dataLayer.Resource
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9611189.dataLayer.AuthRepositoryImpl
import uk.ac.tees.mad.w9611189.route.RegisterState
import javax.inject.Inject

@HiltViewModel
class UserSignupViewModel @Inject constructor(
    application: Application,
    private val repository: AuthRepositoryImpl
) : AndroidViewModel(application) {

    private var _registerState = MutableStateFlow(value = RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun registerUser(email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email = email, password = password).collectLatest { result ->
            when (result) {
                is Resource.Loading -> {
                    _registerState.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    _registerState.update { it.copy(isSuccess = "Register Successful!") }
                }

                is Resource.Error -> {
                    _registerState.update { it.copy(isError = result.message) }
                }
            }
        }
    }
}