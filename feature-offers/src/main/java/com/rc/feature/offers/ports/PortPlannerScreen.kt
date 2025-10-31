@file:OptIn(ExperimentalMaterial3Api::class)

package com.rc.feature.offers.ports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // FIX 1: Import for List support in LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
data class Excursion(val name: String, val price: String, val duration: String, val isBooked: Boolean)
data class PortTimelineItem(val time: String, val description: String, val isCritical: Boolean)

// --- Palette (Re-defined for file independence) ---
private object RoyalPalette {
    val Navy = Color(0xFF061556)
    val Blue = Color(0xFF0073BB)
    val Gold = Color(0xFFFEBD11)
    val Background = Color(0xFFF8FAFB)
}

@Composable
fun PortPlannerScreen(portName: String = "Perfect Day at CocoCay") {
    val excursions = listOf(
        Excursion("Thrill Waterpark Pass", "$129.99", "Full Day", true),
        Excursion("Zip Line Adventure", "$99.00", "2 Hours", false),
        Excursion("Snorkel Gear Rental", "$25.00", "4 Hours", false)
    )
    val timeline = listOf(
        PortTimelineItem("9:00 AM", "Ship Docks at Pier", false),
        PortTimelineItem("9:30 AM", "Waterpark Entry (Booked)", true),
        PortTimelineItem("4:00 PM", "Shopping and Lunch", false),
        PortTimelineItem("5:00 PM", "ALL ABOARD", true)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Port Planner") },
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { PortHero(portName) }
            item { PortDayTimeline(timeline) }
            item { Text("Available Excursions", style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold)) }
            items(excursions) { excursion -> ExcursionCard(excursion) } // FIX 1: Correctly using the imported items extension function
        }
    }
}

@Composable
private fun PortHero(portName: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(portName, style = MaterialTheme.typography.headlineSmall.copy(color = RoyalPalette.Navy))
            Spacer(Modifier.height(8.dp))
            Text("Know Before You Go: Passports required. Local time zone is EST. Weather: 85°F, Sunny.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun PortDayTimeline(timeline: List<PortTimelineItem>) {
    Column(Modifier.fillMaxWidth()) {
        Text("My Port Day Summary", style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(12.dp))

        timeline.forEachIndexed { index, item ->
            Row(Modifier.fillMaxWidth()) {
                // Timeline Dot & Line
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(if (item.isCritical) MaterialTheme.colorScheme.error else RoyalPalette.Gold, CircleShape)
                    )
                    if (index < timeline.lastIndex) {
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier.width(2.dp).height(50.dp).background(RoyalPalette.Blue)
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))

                // Details
                Column {
                    Text(
                        item.time,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = if (item.isCritical) MaterialTheme.colorScheme.error else RoyalPalette.Navy)
                    )
                    Text(item.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun ExcursionCard(excursion: Excursion) {
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
                Text(excursion.name, style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy))
                Spacer(Modifier.height(4.dp))
                Text("${excursion.duration} • ${excursion.price}", style = MaterialTheme.typography.bodyMedium)
            }
            Button(
                onClick = { /* booking action */ },
                enabled = !excursion.isBooked,
                colors = ButtonDefaults.buttonColors(containerColor = if (excursion.isBooked) Color.Gray else RoyalPalette.Blue)
            ) {
                Text(if (excursion.isBooked) "Booked" else "Book Now")
            }
        }
    }
}

// FIX 2: Add a Preview function to make the screen runnable
@Preview(showBackground = true)
@Composable
fun PortPlannerScreenPreview() {
    PortPlannerScreen()
}