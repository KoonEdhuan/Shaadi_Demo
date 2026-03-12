package com.example.shaadidemo

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.shaadidemo.data.entity.ProfilesEntity
import com.example.shaadidemo.viewmodel.MatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesScreen(viewModel: MatchViewModel) {

    val isLoading by viewModel.isLoading.collectAsState(false)
    val profiles by viewModel.profiles.collectAsState()

    val pullToRefreshState = rememberPullToRefreshState()
    val pagerState = rememberPagerState(pageCount = { profiles.size })

    LaunchedEffect(pagerState.currentPage) {
        if (profiles.isNotEmpty() && pagerState.currentPage == profiles.size - 2) {
            viewModel.refreshMatches()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar =  {
            TopAppBar(
                title = { Text(
                    text = "Profiles",
                    style = MaterialTheme.typography.titleLarge
                ) },
            )
        }
    ) { innerPadding ->

        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = isLoading,
            onRefresh = {
                viewModel.refreshMatches()
            },
            indicator =  {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isLoading,
                    modifier = Modifier.align(Alignment.TopCenter),
                    color = Color(0xFFFF5A60)
                )
            }
        ) {
            if (profiles.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No profiles found.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.refreshMatches() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5A60))
                    ) {
                        Text("Retry")
                    }
                }
            } else {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    pageSpacing = 16.dp
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ProfileCard(
                            profile = profiles[page],
                            onAccept = { uuid ->
                                viewModel.acceptUser(uuid)
                            },
                            onDecline = { uuid ->
                                viewModel.declineUser(uuid)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileCard(
    profile: ProfilesEntity,
    onAccept: (uuid: String) -> Unit,
    onDecline: (uuid: String) -> Unit
) {

    var status by remember(profile.uuid) { mutableStateOf(profile.matchStatus) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            GlideImage(
                model = profile.picture.large,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Top-left Match Score Badge
            AnimatedMatchScore(profile.matchScore)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                            startY = 0f
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "${profile.name.first} ${profile.name.last}, ${profile.dob.age}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${profile.location.city}, ${profile.location.country} • ${profile.education}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Logic
                    when (status) {
                        "PENDING" -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        status = "DECLINED"
                                        onDecline(profile.uuid)
                                    },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    border = BorderStroke(2.dp, Color.White),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                                ) {
                                    Text("Decline", fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = {
                                        status = "ACCEPTED"
                                        onAccept(profile.uuid)
                                    },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.7f)
                                    )
                                ) {
                                    Text("Accept", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        "ACCEPTED" -> {
                            StatusResultView("Profile Accepted", Color(0xFF4CAF50), Icons.Default.CheckCircle)
                        }
                        "DECLINED" -> {
                            StatusResultView("Profile Declined", Color.Red, null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusResultView(text: String, color: Color, icon: ImageVector?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(icon, null, tint = color)
            Spacer(Modifier.width(8.dp))
        }
        Text(text, color = color, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
fun AnimatedMatchScore(targetScore: Int) {
    var animationTriggered by remember { mutableStateOf(false) }
    val animatedScore by animateIntAsState(
        targetValue = if (animationTriggered) targetScore else 0,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "scoreAnimation"
    )

    LaunchedEffect(Unit) {
        animationTriggered = true
    }

    Surface(
        color = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(bottomEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$animatedScore% Match",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
