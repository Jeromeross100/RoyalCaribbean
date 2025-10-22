package com.rc.feature.offers
import app.cash.turbine.test
import com.rc.feature.offers.data.FakeOffersRepository
import com.rc.feature.offers.ui.details.OfferDetailsViewModel
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
class OfferDetailsViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setUp() { Dispatchers.setMain(dispatcher) }
    @After fun tearDown() { Dispatchers.resetMain() }
    @Test fun loadsDetails_success() = runTest(dispatcher) {
        val vm = OfferDetailsViewModel(FakeOffersRepository())
        vm.state.test {
            vm.load("1")
            assertTrue(awaitItem() is UIState.Loading)
            dispatcher.scheduler.advanceUntilIdle()
            val success = awaitItem()
            assertTrue(success is UIState.Success && success.data.id == "1")
            cancelAndConsumeRemainingEvents()
        }
    }
}
