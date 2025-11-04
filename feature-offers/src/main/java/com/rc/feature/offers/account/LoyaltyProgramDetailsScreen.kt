// feature-offers/src/main/java/com/rc/feature/offers/account/LoyaltyProgramDetailsScreen.kt

package com.rc.feature.offers.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rc.feature.offers.theme.RoyalPalette



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoyaltyProgramDetailsScreen(
    status: LoyaltyStatus = SampleLoyaltyStatus, // Assuming SampleLoyaltyStatus is available
    allTiers: List<LoyaltyTier> = Tiers, // Assuming Tiers is available
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Crown & Anchor Society") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { CurrentTierCard(status) }
            item { NextTierProgress(status, allTiers) }

            item { SectionHeader("Your ${status.currentTier.name} Benefits") }
            item { BenefitList(status.currentTier.benefits) }

            item { SectionHeader("All Tiers Overview") }
            items(allTiers) { tier -> TierSummaryCard(tier, isCurrentTier = tier == status.currentTier) }
        }
    }
}

// --- UI Components (The logic is sound, only qualifiers fixed) ---

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            color = RoyalPalette.Navy,
            fontWeight = FontWeight.ExtraBold
        ),
        modifier = Modifier.padding(top = 8.dp)
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun CurrentTierCard(status: LoyaltyStatus) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = status.currentTier.color.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back, ${status.guestName}!",
                style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = status.currentTier.name.uppercase(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Black
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "${status.totalPoints} Cruise Points Earned",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = RoyalPalette.Navy,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
private fun NextTierProgress(status: LoyaltyStatus, allTiers: List<LoyaltyTier>) {
    val nextTier = allTiers.firstOrNull { it.pointsRequired > status.totalPoints }
    val nextTierName = nextTier?.name ?: "Pinnacle Club"

    val currentTierPoints = status.currentTier.pointsRequired
    val nextTierPoints = nextTier?.pointsRequired ?: (status.totalPoints + 1)

    val progressRange = nextTierPoints - currentTierPoints
    val pointsGainedInTier = status.totalPoints - currentTierPoints
    val progress = if (progressRange > 0) pointsGainedInTier.toFloat() / progressRange.toFloat() else 1f

    Column(Modifier.fillMaxWidth()) {
        Text(
            "Progress to $nextTierName Tier",
            style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = RoyalPalette.Gold,
            trackColor = RoyalPalette.Blue.copy(alpha = 0.2f)
        )

        Spacer(Modifier.height(4.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${status.totalPoints} Points",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = "${status.pointsToNextTier} needed for $nextTierName",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }
    }
}

@Composable
private fun BenefitList(benefits: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        benefits.forEach { benefit ->
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = RoyalPalette.Blue,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(top = 2.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = benefit,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TierSummaryCard(tier: LoyaltyTier, isCurrentTier: Boolean) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentTier) RoyalPalette.Blue.copy(alpha = 0.1f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentTier) 4.dp else 1.dp),
        // FIX: Removed redundant qualifier from BorderStroke
        border = if (isCurrentTier) BorderStroke(2.dp, RoyalPalette.Blue) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(tier.color)
                    .align(Alignment.CenterVertically)
            )
            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        tier.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            // FIX: Removed redundant qualifier from color
                            color = RoyalPalette.Navy
                        )
                    )
                    if (isCurrentTier) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "(CURRENT)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                // FIX: Removed redundant qualifier from color
                                color = RoyalPalette.Blue,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                Text(
                    "Requires ${tier.pointsRequired} Points",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            OutlinedButton(
                onClick = { /* navigate to deep tier details */ },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("View")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoyaltyProgramDetailsScreenPreview() {
    LoyaltyProgramDetailsScreen()
}