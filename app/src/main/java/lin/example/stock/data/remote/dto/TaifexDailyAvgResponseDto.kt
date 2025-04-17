package lin.example.stock.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 上市個股日收盤價及月平均價 DTO
 *
 * @property code 股票代號
 * @property name 股票名稱
 * @property closingPrice 日收盤價（字串）
 * @property monthlyAvgPrice 月平均價（字串）
 */
data class TaifexDailyAvgResponseDto(
    @SerializedName("Code")            val code: String,
    @SerializedName("Name")            val name: String,
    @SerializedName("ClosingPrice")    val closingPrice: String,
    @SerializedName("MonthlyAveragePrice") val monthlyAvgPrice: String
)