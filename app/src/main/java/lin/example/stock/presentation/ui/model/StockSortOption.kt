package lin.example.stock.presentation.ui.model

/**
 * 排序選項
 *
 * @property title 顯示在 UI 上的文字
 */
enum class StockSortOption(val title: String) {
    CODE_DESC("代碼 ▼"),
    CODE_ASC("代碼 ▲"),
    CLOSE_DESC("收盤 ▼"),
    CLOSE_ASC("收盤 ▲"),
    DIFF_DESC("漲跌 ▼"),
    DIFF_ASC("漲跌 ▲")
}
