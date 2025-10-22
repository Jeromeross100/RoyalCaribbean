package com.rc.feature.offers
import app.cash.turbine.test
import com.rc.feature.offers.data.FakeOffersRepository
import com.rc.feature.offers.ui.list.OffersListViewModel
import com.rc.feature.offers.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OffersListViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }
    @Test fun loadsOffers_success() = runTest(dispatcher) {
        val vm = OffersListViewModel(FakeOffersRepository())
        vm.state.test {
            assertTrue(awaitItem() is UIState.Loading)
            dispatcher.scheduler.advanceUntilIdle()
            val success = awaitItem()
            assertTrue(success is UIState.Success && success.data.isNotEmpty())
            cancelAndConsumeRemainingEvents()
        }
    }
}
