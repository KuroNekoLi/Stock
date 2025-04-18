import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import lin.example.MainDispatcherRule
import lin.example.stock.Resource
import lin.example.stock.domain.model.StockUiModel
import lin.example.stock.domain.usecase.FetchAllStockInfoUseCase
import lin.example.stock.presentation.ui.model.StockSortOption
import lin.example.stock.presentation.ui.viewmodel.TaifexViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaifexViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val singleStock = listOf(
        StockUiModel(
            code = "2330",
            name = "TSMC",
            openPrice = 600.0,
            highPrice = 610.0,
            lowPrice = 590.0,
            closePrice = 605.0,
            monthlyAvgPrice = 590.0,
            tradeVolume = 10_000,
            tradeValue = 6_000_000.0,
            peRatio = 15.5,
            dividendYield = 3.0,
            pbRatio = 1.8,
            change = 5.0,
            transaction = 3_000
        )
    )

    private val twoStocks = listOf(
        StockUiModel(
            code = "2303",
            name = "UMC",
            openPrice = 55.0,
            highPrice = 57.0,
            lowPrice = 54.0,
            closePrice = 56.0,
            monthlyAvgPrice = 53.0,
            tradeVolume = 8_000,
            tradeValue = 448_000.0,
            peRatio = 10.0,
            dividendYield = 2.5,
            pbRatio = 1.2,
            change = 2.0,
            transaction = 2_000
        ),
        StockUiModel(
            code = "2330",
            name = "TSMC",
            openPrice = 600.0,
            highPrice = 610.0,
            lowPrice = 590.0,
            closePrice = 605.0,
            monthlyAvgPrice = 590.0,
            tradeVolume = 10_000,
            tradeValue = 6_000_000.0,
            peRatio = 15.5,
            dividendYield = 3.0,
            pbRatio = 1.8,
            change = 5.0,
            transaction = 3_000
        )
    )


    private lateinit var useCase: FetchAllStockInfoUseCase
    private lateinit var viewModel: TaifexViewModel

    @Before
    fun setUp() {
        useCase = mockk()
        coEvery { useCase() } returns Resource.Success(emptyList())
    }

    @Test
    fun `viewModel emits loading then success on init`() = runTest {
        coEvery { useCase() } returns Resource.Success(singleStock)
        viewModel = TaifexViewModel(useCase)

        viewModel.state.test {
            advanceUntilIdle()
            val loadingState = awaitItem()
            val successState = awaitItem()

            assertTrue(loadingState.resource is Resource.Loading)
            assertTrue(successState.resource is Resource.Success)
            assertEquals(singleStock, (successState.resource as Resource.Success).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadStocks emits loading then success`() = runTest {
        coEvery { useCase() } returns Resource.Success(singleStock)
        viewModel = TaifexViewModel(useCase)

        viewModel.state.test {
            viewModel.loadStocks()
            advanceUntilIdle()
            val loading = awaitItem()
            val success = awaitItem()
            assertTrue(loading.resource is Resource.Loading)
            assertTrue(success.resource is Resource.Success)
            assertEquals(singleStock, (success.resource as Resource.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadStocks emits loading then error on failure`() = runTest {
        val errorMsg = "Network Error"
        coEvery { useCase() } returns Resource.Error(errorMsg)
        viewModel = TaifexViewModel(useCase)

        viewModel.state.test {
            viewModel.loadStocks()
            advanceUntilIdle()
            assertTrue(awaitItem().resource is Resource.Loading)
            val errorState = awaitItem()
            assertTrue(errorState.resource is Resource.Error)
            assertEquals(errorMsg, (errorState.resource as Resource.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSortOptionSelected updates sort option with changed list`() = runTest(mainDispatcherRule.dispatcher) {
        coEvery { useCase() } returns Resource.Success(twoStocks)
        viewModel = TaifexViewModel(useCase)

        viewModel.state.test {
            viewModel.loadStocks()
            advanceUntilIdle()
            awaitItem()
            awaitItem()

            //觸發排序
            viewModel.onSortOptionSelected(StockSortOption.CODE_DESC)
            advanceUntilIdle()
            awaitItem() // 跳過排序時可能重送的 Loading

            //拿到真正的 Success
            val sortedState = awaitItem()
            assertTrue(sortedState.resource is Resource.Success)
            val sortedData = (sortedState.resource as Resource.Success).data
            assertEquals("2330", sortedData.first().code)

            cancelAndIgnoreRemainingEvents()
        }
    }
}