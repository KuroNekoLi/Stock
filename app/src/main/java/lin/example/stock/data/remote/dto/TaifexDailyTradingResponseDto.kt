package lin.example.stock.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 上市個股日成交資訊 DTO
 *
 * @property code 證券代號
 * @property name 證券名稱
 * @property tradeVolume 成交股數 (字串)
 * @property tradeValue 成交金額（字串）
 * @property openPrice 開盤價（字串）
 * @property highPrice 最高價（字串）
 * @property lowPrice 最低價（字串）
 * @property closingPrice 收盤價（字串）
 * @property change 漲跌價差（字串）
 * @property transaction 成交筆數（字串）
 */
data class TaifexDailyTradingResponseDto(
    @SerializedName("Code")         val code: String,
    @SerializedName("Name")         val name: String,
    @SerializedName("TradeVolume")  val tradeVolume: String,
    @SerializedName("TradeValue")   val tradeValue: String,
    @SerializedName("OpeningPrice")    val openPrice: String,
    @SerializedName("HighestPrice")    val highPrice: String,
    @SerializedName("LowestPrice")     val lowPrice: String,
    @SerializedName("ClosingPrice") val closingPrice: String,
    @SerializedName("Change") val change: String,
    @SerializedName("Transaction") val transaction: String
)