package com.proconnect.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun ProConnectOnboardingFlow() {
    val pagerState = rememberPagerState(pageCount = { 7 })
    val scope = rememberCoroutineScope()
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = pagerState.currentPage < 3,
    ) { page ->
        when (page) {
            0 -> ProConnectSplashScreen()
            1 -> ProConnectOnboardingScreen2()
            2 -> ProConnectOnboardingScreen3()
            3 -> ProConnectLoginScreen(
                onBack = { scope.launch { pagerState.animateScrollToPage(2) } },
                onCreateAccount = { scope.launch { pagerState.animateScrollToPage(4) } },
            )
            4 -> ProConnectCreateAccountScreen(
                onBack = { scope.launch { pagerState.animateScrollToPage(3) } },
                onSignIn = { scope.launch { pagerState.animateScrollToPage(3) } },
                onCompanySelected = { scope.launch { pagerState.animateScrollToPage(5) } },
            )
            5 -> CompanyBasicInfoScreen(
                onBack = { scope.launch { pagerState.animateScrollToPage(4) } },
                onNext = { scope.launch { pagerState.animateScrollToPage(6) } },
            )
            else -> CompanyLegalDetailsScreen(
                onBack = { scope.launch { pagerState.animateScrollToPage(5) } },
            )
        }
    }
}
