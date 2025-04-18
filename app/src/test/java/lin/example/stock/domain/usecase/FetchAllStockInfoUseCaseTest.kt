package lin.example.stock.domain.usecase

import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import lin.example.stock.Resource
import lin.example.stock.data.di.networkModule
import lin.example.stock.domain.model.DailyAvgData
import lin.example.stock.domain.model.DailyTradingData
import lin.example.stock.domain.model.TaifexData
import lin.example.stock.domain.repository.TaifexRepository
import org.junit.Assert.*
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock

class FetchAllStockInfoUseCaseTest : KoinTest {

    @get:Rule
    val koinRule = KoinTestRule.create {
        allowOverride(true)
        modules(networkModule)
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    private val repository: TaifexRepository by inject()
    private val useCase: FetchAllStockInfoUseCase by lazy { FetchAllStockInfoUseCase(repository) }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun fetchAllStockInfo_shouldReturnSuccess_whenAllDataPresent() = runTest {
        declareMock<TaifexRepository> {
            coEvery { fetchStockFinancialIndicators() } returns listOf(
                TaifexData("2330", "TSMC", 15.5, 2.1, 3.0)
            )
            coEvery { fetchDailyClosingAndMonthlyAverage() } returns listOf(
                DailyAvgData("2330", "TSMC", 600.0, 590.0)
            )
            coEvery { fetchDailyStockTradingInfo() } returns listOf(
                DailyTradingData("2330", "TSMC", 10000, 6000000.0, 610.0, 620.0, 595.0, 605.0, 5.0, 3000)
            )
        }

        val result = useCase()

        assertTrue(result is Resource.Success)
        val data = (result as Resource.Success).data
        assertEquals(1, data.size)
        val stock = data.first()
        assertEquals("2330", stock.code)
        assertEquals(610.0, stock.openPrice, 0.001)
        assertEquals(590.0, stock.monthlyAvgPrice, 0.001)
        assertNotNull(stock.change)
        assertEquals(5.0, stock.change!!, 0.001)
    }

    @Test
    fun fetchAllStockInfo_shouldSkipItems_whenPartialDataMissing() = runTest {
        declareMock<TaifexRepository> {
            coEvery { fetchStockFinancialIndicators() } returns listOf(
                TaifexData("1234", "OnlyFin", 10.0, 1.0, 1.5)
            )
            coEvery { fetchDailyClosingAndMonthlyAverage() } returns emptyList()
            coEvery { fetchDailyStockTradingInfo() } returns emptyList()
        }

        val result = useCase()

        assertTrue(result is Resource.Success)
        assertTrue((result as Resource.Success).data.isEmpty())
    }

    @Test
    fun fetchAllStockInfo_shouldFillDefaultValues_whenNullInSource() = runTest {
        declareMock<TaifexRepository> {
            coEvery { fetchStockFinancialIndicators() } returns listOf(
                TaifexData("9999", "DefaultTest", null, null, null)
            )
            coEvery { fetchDailyClosingAndMonthlyAverage() } returns listOf(
                DailyAvgData("9999", "DefaultTest", null, null)
            )
            coEvery { fetchDailyStockTradingInfo() } returns listOf(
                DailyTradingData("9999", "DefaultTest", null, null, null, null, null, null, null, null)
            )
        }

        val result = useCase()

        assertTrue(result is Resource.Success)
        val stock = (result as Resource.Success).data.first()
        assertEquals(0.0, stock.openPrice, 0.001)
        assertEquals(0.0, stock.monthlyAvgPrice, 0.001)
        assertEquals(0, stock.tradeVolume)
        assertNull(stock.peRatio)
    }

    @Test
    fun fetchAllStockInfo_shouldReturnError_whenExceptionThrown() = runTest {
        declareMock<TaifexRepository> {
            coEvery { fetchStockFinancialIndicators() } throws RuntimeException("network failed")
            coEvery { fetchDailyClosingAndMonthlyAverage() } returns emptyList()
            coEvery { fetchDailyStockTradingInfo() } returns emptyList()
        }

        val result = useCase()

        assertTrue(result is Resource.Error)
        assertEquals("network failed", (result as Resource.Error).message)
    }
}
