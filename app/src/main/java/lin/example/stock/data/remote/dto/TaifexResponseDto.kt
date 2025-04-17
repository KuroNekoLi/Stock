package lin.example.stock.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 對應 API 回傳的上市個股財務指標資料。
 *
 * @property code 股票代號。
 * @property name 股票名稱。
 * @property peRatio 本益比（原始字串）。
 * @property dividendYield 殖利率（% 原始字串）。
 * @property pbRatio 股價淨值比（原始字串）。
 */
data class TaifexResponseDto(
    @SerializedName("Code")          val code: String,
    @SerializedName("Name")          val name: String,
    @SerializedName("PEratio")       val peRatio: String,
    @SerializedName("DividendYield") val dividendYield: String,
    @SerializedName("PBratio")       val pbRatio: String
)