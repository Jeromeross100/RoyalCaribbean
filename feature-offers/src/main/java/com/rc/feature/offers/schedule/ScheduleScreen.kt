package com.rc.feature.offers.schedule

// app/src/main/java/com/rc/app/schedule/ScheduleScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ROYAL CARIBBEAN PALETTE DEFINITION
private object RoyalPalette {
    val Navy = Color(0xFF061556) // Dark Blue / Oxford Blue (Headers)
    val Blue = Color(0xFF0073BB) // Primary Blue (Accents)
    val Gold = Color(0xFFFEBD11) // Accent Gold (Timeline dots)
    val Background = Color(0xFFF8FAFB) // Light Off-White
}

data class DayEvent(val time: String, val title: String, val location: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    val events = listOf(
        "Day 1 – Miami (Embark)" to listOf(
            DayEvent("12:00 PM", "Boarding Opens", "Terminal A"),
            DayEvent("04:00 PM", "Sail Away Party", "Pool Deck")
        ),
        "Day 2 – Perfect Day at CocoCay" to listOf(
            DayEvent("09:00 AM", "Arrive", "CocoCay"),
            DayEvent("10:00 AM", "Thrill Waterpark", "CocoCay"),
            DayEvent("05:00 PM", "All Aboard", "Pier")
        ),
        "Day 3 – Nassau" to listOf(
            DayEvent("08:00 AM", "Arrive", "Prince George Wharf"),
            DayEvent("10:00 AM", "Snorkel Excursion", "Nassau"),
            DayEvent("05:30 PM", "All Aboard", "Pier")
        ),
        "Day 4 – Miami (Debark)" to listOf(
            DayEvent("07:00 AM", "Arrival", "Terminal A"),
            DayEvent("07:30 AM", "Breakfast", "Main Dining"),
            DayEvent("09:00 AM", "Debarkation", "Terminal A")
        )
    )

    Scaffold(
        // 🚢 UPDATED: Branded Top Bar
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Voyage Schedule") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(padding)
        ) {
            events.forEach { (dayHeader, itemsForDay) ->
                // --- Day Header ---
                item {
                    Text(
                        dayHeader,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = RoyalPalette.Navy, // Use Navy for strong header
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                }

                // --- Day Events (Timeline) ---
                items(itemsForDay) { e ->
                    // ⚓ Use a Row to create the timeline effect
                    Row(Modifier.fillMaxWidth()) {
                        // 1. Timeline Accent (Gold Dot and Blue Line)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Gold Dot for the event time
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(RoyalPalette.Gold, CircleShape)
                            )
                            // Blue vertical line connecting events (optional, adjust height as needed)
                            if (e != itemsForDay.last()) {
                                Spacer(Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(IntrinsicSize.Min) // Set to min size of content
                                        .background(RoyalPalette.Blue)
                                        .weight(1f, fill = true)
                                )
                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        // 2. Event Details
                        Column(modifier = Modifier.weight(1f)) {
                            // Time and Title (highlighted)
                            Text(
                                "${e.time}  •  ${e.title}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = RoyalPalette.Navy,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            // Location (subdued)
                            Text(
                                e.location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}