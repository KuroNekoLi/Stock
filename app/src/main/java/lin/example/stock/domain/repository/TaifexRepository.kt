package lin.example.stock.domain.repository

import lin.example.stock.domain.model.DailyAvgData
import lin.example.stock.domain.model.DailyTradingData
import lin.example.stock.domain.model.TaifexData

/**
 * 台股證券交易所財務指標資料取得介面。
 */
interface TaifexRepository {
    /**
     * 取得上市個股本益比、殖利率及股價淨值比清單。
     *
     * @return List<TaifexData> 上市個股財務指標模型清單。
     */
    suspend fun fetchStockFinancialIndicators(): List<TaifexData>
    suspend fun fetchDailyClosingAndMonthlyAverage(): List<DailyAvgData>
    suspend fun fetchDailyStockTradingInfo(): List<DailyTradingData>
}