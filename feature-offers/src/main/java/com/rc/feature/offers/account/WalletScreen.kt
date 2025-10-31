@file:OptIn(ExperimentalMaterial3Api::class)

package com.rc.feature.offers.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview // FIX 2: Import for Preview
import androidx.compose.ui.unit.dp

// --- Data Structures ---
data class OnboardTransaction(val date: String, val description: String, val amount: String)
data class Package(val name: String, val status: String, val color: Color)

// --- Palette (Re-defined for file independence) ---
private object RoyalPalette {
    val Navy = Color(0xFF061556)
    val Blue = Color(0xFF0073BB)
    val Gold = Color(0xFFFEBD11)
    val Background = Color(0xFFF8FAFB)
}

@Composable
fun WalletScreen(
    balance: String = "$150.00",
    loyaltyTier: String = "Emerald"
) {
    val transactions = listOf(
        OnboardTransaction("10/30 4:30 PM", "Drink Package Purchase", "$180.00"),
        OnboardTransaction("10/30 8:00 PM", "Specialty Coffee - Cafe", "$4.50"),
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { BalanceAndLoyaltyCard(balance, loyaltyTier) }
            item { Text("My Active Packages", style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold)) }
            items(packages) { pkg -> PackageCard(pkg) }
            item { Text("Transaction History", style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold)) }
            items(transactions) { tx -> TransactionRow(tx) }
        }
    }
}

@Composable
private fun BalanceAndLoyaltyCard(balance: String, loyaltyTier: String) {
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
            HorizontalDivider(Modifier.padding(vertical = 8.dp)) // FIX 1: Replaced Divider with HorizontalDivider
            Text("Crown & Anchor Tier: $loyaltyTier", style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Gold, fontWeight = FontWeight.SemiBold)) // FIX 3: Removed redundant {}
        }
    }
}

@Composable
private fun PackageCard(pkg: Package) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(pkg.name, style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy))
                Text(pkg.status, style = MaterialTheme.typography.bodyMedium.copy(color = pkg.color))
            }
            Button(
                onClick = { /* Upgrade/Manage Action */ },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue)
            ) {
                Text("Manage")
            }
        }
    }
}

@Composable
private fun TransactionRow(tx: OnboardTransaction) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(tx.description, style = MaterialTheme.typography.titleSmall.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Medium))
            Text(tx.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Text(
            tx.amount,
            style = MaterialTheme.typography.titleSmall.copy(color = RoyalPalette.Blue, fontWeight = FontWeight.SemiBold)
        )
    }
    HorizontalDivider() // FIX 1: Replaced Divider with HorizontalDivider
}

// FIX 2: Add a Preview function to make the screen runnable
@Preview(showBackground = true)
@Composable
fun WalletScreenPreview() {
    WalletScreen()

}