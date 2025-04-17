package lin.example.stock.data.remote.api

import lin.example.stock.data.remote.dto.TaifexDailyAvgResponseDto
import lin.example.stock.data.remote.dto.TaifexDailyTradingResponseDto
import lin.example.stock.data.remote.dto.TaifexResponseDto
import retrofit2.http.GET

/**
 * 台灣證券交易所 API 的服務介面。
 */
interface TaifexApiService {
    /**
     * 取得上市個股本益比、殖利率及股價淨值比
     *
     * @return List<TaifexResponseDto> 回傳上市個股財務指標 DTO 清單。
     */
    @GET("exchangeReport/BWIBBU_ALL")
    suspend fun fetchStockFinancialIndicators(): List<TaifexResponseDto>

    /**
     * 取得上市個股日收盤價及月平均價
     *
     * @return
     */
    @GET("exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun fetchDailyClosingAndMonthlyAverage(): List<TaifexDailyAvgResponseDto>

    /**
     * 取得上市個股日成交資訊
     *
     * @return
     */
    @GET("exchangeReport/STOCK_DAY_ALL")
    suspend fun fetchDailyStockTradingInfo(): List<TaifexDailyTradingResponseDto>
}