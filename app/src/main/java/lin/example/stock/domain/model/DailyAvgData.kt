package lin.example.stock.domain.model

/**
 * 上市個股日收盤價及月平均價模型
 *
 * @property code 股票代號
 * @property name 股票名稱
 * @property closingPrice 日收盤價（Double?）
 * @property monthlyAvgPrice 月平均價（Double?）
 */
data class DailyAvgData(
    val code: String,
    val name: String,
    val closingPrice: Double?,
    val monthlyAvgPrice: Double?
)