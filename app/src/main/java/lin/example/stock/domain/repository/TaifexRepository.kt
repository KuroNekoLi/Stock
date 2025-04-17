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

    /**
     * 取得上市個股日收盤價及月平均價清單
     *
     * @return List<DailyAvgData> 日收盤價及月平均價模型清單
     * @throws HttpException 當 HTTP 回應非 2xx
     * @throws IOException   當網路連線失敗
     */
    suspend fun fetchDailyClosingAndMonthlyAverage(): List<DailyAvgData>

    /**
     * 取得上市個股日成交資訊清單
     *
     * @return List<DailyTradingData> 日成交資訊模型清單
     * @throws HttpException 當 HTTP 回應非 2xx
     * @throws IOException   當網路連線失敗
     */
    suspend fun fetchDailyStockTradingInfo(): List<DailyTradingData>
}