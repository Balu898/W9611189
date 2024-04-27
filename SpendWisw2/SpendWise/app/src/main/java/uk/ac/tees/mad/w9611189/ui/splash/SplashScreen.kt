package uk.ac.tees.mad.w9611189.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.w9611189.R
import uk.ac.tees.mad.w9611189.ui.util.Routes

@Composable
fun SplashScreen(navController: NavController) {

    val logoVisibility = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(500)
        logoVisibility.value = true
        delay(2000)
        navController.navigate(Routes.LoginScreen.route) {
            popUpTo(Routes.SplashScreen.route) {
                inclusive = true
            }
        }
    }

    val fadeInAlpha by animateFloatAsState(
        targetValue = if (logoVisibility.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.light_black)),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = logoVisibility.value,
            enter = fadeInAlphaAnimationSpec(),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Splash Screen Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(fadeInAlpha)
            )
        }
    }
}

private fun fadeInAlphaAnimationSpec(): EnterTransition {
    return fadeIn(animationSpec = tween(durationMillis = 1000)) +
            slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(durationMillis = 1000)
            )
}