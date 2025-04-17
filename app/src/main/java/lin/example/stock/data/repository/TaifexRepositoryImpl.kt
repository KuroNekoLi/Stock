package lin.example.stock.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lin.example.stock.data.remote.api.TaifexApiService
import lin.example.stock.data.remote.dto.TaifexDailyAvgResponseDto
import lin.example.stock.data.remote.dto.TaifexDailyTradingResponseDto
import lin.example.stock.data.remote.dto.TaifexResponseDto
import lin.example.stock.domain.model.DailyAvgData
import lin.example.stock.domain.model.DailyTradingData
import lin.example.stock.domain.model.TaifexData
import lin.example.stock.domain.repository.TaifexRepository

/**
 * TaifexRepositoryImpl
 *
 * 實作 [TaifexRepository]，透過 [TaifexApiService] 呼叫證交所 OpenAPI，
 * 並將回傳的 DTO 轉換為 Domain Model。
 *
 * @param apiService    Retrofit 定義的 API 服務
 * @param ioDispatcher  用於 IO 操作的 CoroutineDispatcher（預設為 [Dispatchers.IO]）
 */
class TaifexRepositoryImpl(
    private val apiService: TaifexApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaifexRepository {
    override suspend fun fetchStockFinancialIndicators(): List<TaifexData> =
        withContext(ioDispatcher) {
            apiService.fetchStockFinancialIndicators()
                .map { dto: TaifexResponseDto ->
                    TaifexData(
                        code = dto.code,
                        name = dto.name,
                        peRatio = dto.peRatio.toDoubleOrNull(),
                        dividendYield = dto.dividendYield.toDoubleOrNull(),
                        pbRatio = dto.pbRatio.toDoubleOrNull()
                    )
                }
        }

    override suspend fun fetchDailyClosingAndMonthlyAverage(): List<DailyAvgData> =
        withContext(ioDispatcher) {
            apiService.fetchDailyClosingAndMonthlyAverage()
                .map { dto: TaifexDailyAvgResponseDto ->
                    DailyAvgData(
                        code = dto.code,
                        name = dto.name,
                        closingPrice = dto.closingPrice.toDoubleOrNull(),
                        monthlyAvgPrice = dto.monthlyAvgPrice.toDoubleOrNull()
                    )
                }
        }

    override suspend fun fetchDailyStockTradingInfo(): List<DailyTradingData> =
        withContext(ioDispatcher) {
            apiService.fetchDailyStockTradingInfo()
                .map { dto: TaifexDailyTradingResponseDto ->
                    DailyTradingData(
                        code = dto.code,
                        name = dto.name,
                        tradeVolume = dto.tradeVolume.toDoubleOrNull(),
                        tradeValue = dto.tradeValue.toDoubleOrNull(),
                        openPrice = dto.openPrice.toDoubleOrNull(),
                        highPrice = dto.highPrice.toDoubleOrNull(),
                        lowPrice = dto.lowPrice.toDoubleOrNull(),
                        closingPrice = dto.closingPrice.toDoubleOrNull(),
                        change = dto.change.toDoubleOrNull(),
                        transaction = dto.transaction.toIntOrNull()
                    )
                }
        }
}





