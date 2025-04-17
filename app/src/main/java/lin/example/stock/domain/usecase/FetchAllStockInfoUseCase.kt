package lin.example.stock.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lin.example.stock.Resource
import lin.example.stock.domain.model.StockUiModel
import lin.example.stock.domain.repository.TaifexRepository

/**
 * 台股證券交易所財務指標資料取得 UseCase。
 *
 * @property repository 台股證券交易所財務指標資料取得介面。
 */

/**
 * 一次取得並整合所有三種資料源，回傳 UI 可用的 [StockUiModel]。
 */
class FetchAllStockInfoUseCase(
    private val repository: TaifexRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(): Resource<List<StockUiModel>> =
        withContext(ioDispatcher) {
            try {
                // 1. 串接
                val fin = repository.fetchStockFinancialIndicators()
                val avg = repository.fetchDailyClosingAndMonthlyAverage().associateBy { it.code }
                val trade = repository.fetchDailyStockTradingInfo().associateBy { it.code }

                // 2. 合併
                val list = fin.mapNotNull { f ->
                    avg[f.code]?.let { a ->
                        trade[f.code]?.let { t ->
                            StockUiModel(
                                code = f.code,
                                name = f.name,
                                openPrice = t.openPrice ?: 0.0,
                                highPrice = t.highPrice ?: 0.0,
                                lowPrice = t.lowPrice ?: 0.0,
                                closePrice = t.closingPrice ?: 0.0,
                                monthlyAvgPrice = a.monthlyAvgPrice ?: 0.0,
                                tradeVolume = t.tradeVolume ?: 0.0,
                                tradeValue = t.tradeValue ?: 0.0,
                                peRatio = f.peRatio,
                                dividendYield = f.dividendYield,
                                pbRatio = f.pbRatio,
                                change = t.change,
                                transaction = t.transaction
                            )
                        }
                    }
                }

                Resource.Success(list)
            } catch (e: Exception) {
                // 3. 統一捕捉所有例外
                Resource.Error(e.message ?: "未知錯誤", e)
            }
        }
}