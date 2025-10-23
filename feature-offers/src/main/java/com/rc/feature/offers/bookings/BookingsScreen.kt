package com.rc.feature.offers.bookings

// feature-offers/src/main/java/com/rc/feature/offers/bookings/BookingsScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rc.feature.offers.data.graphql.BookingDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    vm: BookingsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("My Bookings") }) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is com.rc.feature.offers.util.UIState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is com.rc.feature.offers.util.UIState.Error ->
                    Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Unable to load bookings")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { vm.load() }) { Text("Retry") }
                    }

                is com.rc.feature.offers.util.UIState.Success ->
                    BookingsList(
                        bookings = s.data,
                        onCancel = { id -> vm.cancel(id) { /* could show snackbar */ } }
                    )
            }
        }
    }
}

@Composable
private fun BookingsList(
    bookings: List<BookingDto>,
    onCancel: (String) -> Unit
) {
    if (bookings.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No bookings yet.")
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(bookings, key = { it.id }) { b ->
            Surface(tonalElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Confirmation: ${b.confirmationId}", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text("Guest: ${b.guestName}")
                    Text("Email: ${b.email}")
                    Text("Offer ID: ${b.offerId}")
                    Text("Created: ${b.createdAt}")
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = { onCancel(b.id) }) { Text("Cancel") }
                    }
                }
            }
        }
    }
}
