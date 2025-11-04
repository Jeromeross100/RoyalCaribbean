// feature-offers/src/main/java/com/rc/feature/offers/account/WalletScreen.kt

package com.rc.feature.offers.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rc.feature.offers.theme.RoyalPalette


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    balance: String = "$150.00",
    loyaltyTier: String = "Emerald",
    onNavigateToLoyaltyDetails: () -> Unit // Navigation action
) {
    // Uses data classes imported from WalletModels.kt
    val transactions = listOf(
        OnboardTransaction("10/30 4:30 PM", "Drink Package Purchase", "$180.00"),
        OnboardTransaction("10/31 10:00 AM", "Shore Excursion Deposit", "$50.00")
    )
    val packages = listOf(
        Package("Deluxe Drink", "Active", RoyalPalette.Blue),
        Package("VOOM Wi-Fi", "Active", RoyalPalette.Blue),
        Package("Specialty Dining", "3 of 5 Used", RoyalPalette.Gold)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Wallet & Account") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BalanceAndLoyaltyCard(
                    balance = balance,
                    loyaltyTier = loyaltyTier,
                    onViewLoyaltyDetails = onNavigateToLoyaltyDetails
                )
            }
            item { Text("My Active Packages", style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold)) }
            items(packages) { pkg -> PackageCard(pkg) }
            item { Text("Transaction History", style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold)) }
            items(transactions) { tx -> TransactionRow(tx) }
        }
    }
}

@Composable
private fun BalanceAndLoyaltyCard(
    balance: String,
    loyaltyTier: String,
    onViewLoyaltyDetails: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.dp, RoyalPalette.Blue),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Onboard Account Balance", style = MaterialTheme.typography.titleMedium)
            Text(
                balance,
                style = MaterialTheme.typography.headlineLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            HorizontalDivider(Modifier.padding(vertical = 8.dp))

            // FIX: Loyalty section is clickable and uses RoyalPalette
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onViewLoyaltyDetails)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Crown & Anchor Tier: $loyaltyTier",
                    style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Gold, fontWeight = FontWeight.SemiBold)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View Details",
                    tint = RoyalPalette.Gold,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun PackageCard(pkg: Package) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    pkg.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.SemiBold)
                )
                Text(
                    pkg.status,
                    style = MaterialTheme.typography.bodySmall.copy(color = if (pkg.status == "Active") pkg.color else Color.Gray)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Package Details",
                tint = RoyalPalette.Navy
            )
        }
    }
}

@Composable
private fun TransactionRow(tx: OnboardTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                tx.description,
                style = MaterialTheme.typography.bodyLarge.copy(color = RoyalPalette.Navy)
            )
            Text(
                tx.date,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Text(
            tx.amount,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
    HorizontalDivider(color = RoyalPalette.Blue)
}

@Preview(showBackground = true)
@Composable
fun WalletScreenPreview() {
    WalletScreen(onNavigateToLoyaltyDetails = {})
}