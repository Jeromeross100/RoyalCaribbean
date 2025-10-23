package com.rc.feature.offers.schedule

// app/src/main/java/com/rc/app/schedule/ScheduleScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
        topBar = { CenterAlignedTopAppBar(title = { Text("Schedule") }) }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.padding(padding)
        ) {
            events.forEach { (dayHeader, itemsForDay) ->
                item {
                    Text(dayHeader, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(6.dp))
                }
                items(itemsForDay) { e ->
                    Column {
                        Text("${e.time}  •  ${e.title}", style = MaterialTheme.typography.titleMedium)
                        Text(e.location, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
