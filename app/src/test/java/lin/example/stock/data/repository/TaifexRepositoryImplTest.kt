package lin.example.stock.data.repository

import io.mockk.coEvery
import io.mockk.mockkClass
import kotlinx.coroutines.test.runTest
import lin.example.stock.data.di.networkModule
import lin.example.stock.data.remote.api.TaifexApiService
import lin.example.stock.data.remote.dto.TaifexDailyAvgResponseDto
import lin.example.stock.data.remote.dto.TaifexDailyTradingResponseDto
import lin.example.stock.data.remote.dto.TaifexResponseDto
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

class TaifexRepositoryImplTest : KoinTest {

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

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun fetchStockFinancialIndicators_shouldReturnCorrectData() = runTest {
        val sampleDto = listOf(
            TaifexResponseDto("A", "StockA", "10.5", "2.3", "1.2"),
            TaifexResponseDto("B", "StockB", "invalid", "0.0", "3.4")
        )
        declareMock<TaifexApiService> {
            coEvery { fetchStockFinancialIndicators() } returns sampleDto
        }

        val result = repository.fetchStockFinancialIndicators()

        assertEquals(2, result.size)
        result[0].let {
            assertEquals("A", it.code)
            assertEquals(10.5, it.peRatio)
        }
        result[1].let {
            assertEquals("B", it.code)
            assertNull(it.peRatio)
        }
    }

    @Test
    fun fetchDailyClosingAndMonthlyAverage_shouldHandleValidAndInvalidValues() = runTest {
        val sampleDto = listOf(
            TaifexDailyAvgResponseDto(
                code = "1234",
                name = "Test Corp",
                closingPrice = "52.3",
                monthlyAvgPrice = "50.0"
            ),
            TaifexDailyAvgResponseDto(
                code = "5678",
                name = "Fail Corp",
                closingPrice = "invalid",
                monthlyAvgPrice = "NaN"
            )
        )
        declareMock<TaifexApiService> {
            coEvery { fetchDailyClosingAndMonthlyAverage() } returns sampleDto
        }

        val result = repository.fetchDailyClosingAndMonthlyAverage()

        assertEquals(2, result.size)
        result[0].let {
            assertEquals("1234", it.code)
            assertEquals("Test Corp", it.name)
            assertEquals(52.3, it.closingPrice)
            assertEquals(50.0, it.monthlyAvgPrice)
        }
        result[1].let {
            assertEquals("5678", it.code)
            assertEquals("Fail Corp", it.name)
            assertNull(it.closingPrice)
            assertTrue(it.monthlyAvgPrice!!.isNaN())
        }
    }

    @Test
    fun fetchDailyStockTradingInfo_shouldHandleCompleteAndErrorCases() = runTest {
        val sampleDto = listOf(
            TaifexDailyTradingResponseDto(
                code = "8888",
                name = "Trade Co",
                tradeVolume = "100000",
                tradeValue = "6500000.0",
                openPrice = "65.0",
                highPrice = "66.5",
                lowPrice = "64.2",
                closingPrice = "65.8",
                change = "-0.5",
                transaction = "452"
            ),
            TaifexDailyTradingResponseDto(
                code = "9999",
                name = "Error Co",
                tradeVolume = "NaN",
                tradeValue = "error",
                openPrice = "N/A",
                highPrice = "NaN",
                lowPrice = "NaN",
                closingPrice = "NaN",
                change = "NaN",
                transaction = "invalid"
            )
        )
        declareMock<TaifexApiService> {
            coEvery { fetchDailyStockTradingInfo() } returns sampleDto
        }

        val result = repository.fetchDailyStockTradingInfo()

        assertEquals(2, result.size)
        result[0].let {
            assertEquals("8888", it.code)
            assertEquals("Trade Co", it.name)
            assertEquals(100000, it.tradeVolume)
            assertEquals(6500000.0, it.tradeValue)
            assertEquals(65.0, it.openPrice)
            assertEquals(66.5, it.highPrice)
            assertEquals(64.2, it.lowPrice)
            assertEquals(65.8, it.closingPrice)
            assertEquals(-0.5, it.change)
            assertEquals(452, it.transaction)
        }
        result[1].let {
            assertEquals("9999", it.code)
            assertEquals("Error Co", it.name)
            assertNull(it.tradeVolume)
            assertNull(it.tradeValue)
            assertNull(it.openPrice)
            assertTrue(it.highPrice!!.isNaN())
            assertTrue(it.lowPrice!!.isNaN())
            assertTrue(it.closingPrice!!.isNaN())
            assertTrue(it.change!!.isNaN())
            assertNull(it.transaction)
        }
    }
}
