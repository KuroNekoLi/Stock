package lin.example.stock.domain.model

/**
 * UI 層所需的單一股票完整資訊模型，
 * @property code 股票代號
 * @property name 股票名稱
 * @property openPrice 開盤價
 * @property highPrice 最高價
 * @property lowPrice 最低價
 * @property closePrice 收盤價
 * @property monthlyAvgPrice 月平均價
 * @property tradeVolume 成交量
 * @property tradeValue 成交金額
 * @property peRatio 本益比（Double）
 * @property dividendYield 殖利率（Double）
 * @property pbRatio 股價淨值比（Double）
 * @property change 漲跌價差（Double）
 * @property transaction 成交筆數（Int）
 */
data class StockUiModel(
    val code: String,
    val name: String,
    val openPrice: Double,
    val highPrice: Double,
    val lowPrice: Double,
    val closePrice: Double,
    val monthlyAvgPrice: Double,
    val tradeVolume: Double,
    val tradeValue: Double,
    val peRatio: Double?,
    val dividendYield: Double?,
    val pbRatio: Double?,
    val change: Double?,
    val transaction: Int?
)