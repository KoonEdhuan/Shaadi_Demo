package com.example.shaadidemo

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesScreen() {
    val mockUsers = remember {
        listOf(
            MockUser(1, "Aditi Sharma", 26, "Mumbai", "India",  "M.Tech", 92, "https://randomuser.me/api/portraits/women/1.jpg"),
            MockUser(2, "Rahul Verma", 29, "Nagpur", "India",  "MBA", 85, "https://randomuser.me/api/portraits/men/2.jpg"),
            MockUser(3, "Sneha Patil", 25, "Pune", "India",  "B.E", 78, "https://randomuser.me/api/portraits/women/3.jpg"),
            MockUser(4, "Amit Deshmukh", 30, "Mumbai", "India",  "PhD", 95, "https://randomuser.me/api/portraits/men/4.jpg"),
            MockUser(5, "Priya Nair", 27, "Bangalore", "India",  "MBBS", 88, "https://randomuser.me/api/portraits/women/5.jpg")
        )
    }

    val pagerState = rememberPagerState(pageCount = { mockUsers.size })

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

        Box {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color.White),
                pageSpacing = 16.dp
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ProfileCard(
                        user = mockUsers[page],
                        onAccept = { /* Handle accept action */ },
                        onDecline = { /* Handle decline action */ }
                    )
                }
            }

            ScrollDownHint()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileCard(
    user: MockUser,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {

    var status by remember { mutableStateOf("PENDING") }

    // Use a Box to layer text and buttons over the full-height image
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. Full-screen Background Image
            GlideImage(
                model = user.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 2. Top-left Match Score Badge
            AnimatedMatchScore(user.score)

            // 3. Bottom Detail Overlay (Gradient + Info + Actions)
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
                    ScrollDownHint()

                    Text(
                        text = "${user.name}, ${user.age}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${user.city}, ${user.country} • ${user.education}",
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
                                        onDecline()
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
                                        onAccept()
                                    },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
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

@Preview
@Composable
fun Review() {
    ProfilesScreen()
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
        shape = RoundedCornerShape(bottomEnd =24.dp)
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

@Composable
fun ScrollDownHint(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "scroll")
    val yOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yOffset"
    )

    Column(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .offset(y = yOffset.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Swipe for next",
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.labelSmall
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.8f)
        )
    }
}



data class MockUser(
    val id: Int,
    val name: String,
    val age: Int,
    val city: String,
    val country: String,
    val education: String,
    val score: Int,
    val imageUrl: String
)