package uk.ac.tees.mad.w9611189.route


data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = null
)
