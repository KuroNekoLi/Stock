package lin.example.stock.domain.model

/**
 * 上市個股日成交資訊模型
 *
 * @property code 股票代號
 * @property name 股票名稱
 * @property tradeVolume 成交股數(Int?)
 * @property tradeValue 成交金額（Double?）
 * @property openPrice 開盤價（Double?）
 * @property highPrice 最高價（Double?）
 * @property lowPrice 最低價（Double?）
 * @property closingPrice 收盤價（Double?）
 * @property change       漲跌價差
 * @property transaction  成交筆數
 */
data class DailyTradingData(
    val code: String,
    val name: String,
    val tradeVolume: Int?,
    val tradeValue: Double?,
    val openPrice: Double?,
    val highPrice: Double?,
    val lowPrice: Double?,
    val closingPrice: Double?,
    val change: Double?,
    val transaction: Int?
)