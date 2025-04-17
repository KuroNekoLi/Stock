package lin.example.stock.domain.model

/**
 * 上市個股財務指標模型，供 Presentation 層使用。
 *
 * @property code 股票代號。
 * @property name 股票名稱。
 * @property peRatio 本益比（Double）。
 * @property dividendYield 殖利率（Double）。
 * @property pbRatio 股價淨值比（Double）。
 */
data class TaifexData(
    val code: String,
    val name: String,
    val peRatio: Double?,
    val dividendYield: Double?,
    val pbRatio: Double?
)