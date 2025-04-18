package lin.example.stock.presentation.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import lin.example.stock.domain.model.StockUiModel
import org.junit.Rule
import org.junit.Test

class StockCardTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldDisplayFields_andUseCorrectColors_andHandleClick() {
        var clicked = false
        val model = StockUiModel(
            code = "2330",
            name = "TSMC",
            openPrice = 100.0,
            highPrice = 110.0,
            lowPrice = 90.0,
            closePrice = 105.0,
            monthlyAvgPrice = 102.0,
            tradeVolume = 1000,
            tradeValue = 50000.0,
            peRatio = 15.0,
            dividendYield = 2.5,
            pbRatio = 1.5,
            change = 3.0,
            transaction = 50
        )
        composeRule.setContent {
            StockCard(item = model) { clicked = true }
        }

        // 代號與名稱
        composeRule.onNodeWithText("2330").assertExists()
        composeRule.onNodeWithText("TSMC").assertExists()

        // 價格格式化 收盤價 > 月均時，顏色為紅色
        composeRule.onNodeWithTag("closePrice_up", useUnmergedTree = true).assertExists()

        // 價格格式化
        composeRule.onNodeWithText("100.00").assertExists()  // 開盤
        composeRule.onNodeWithText("105.00").assertExists()  // 收盤

        // 點擊觸發 onClick
        composeRule.onNodeWithText("2330").performClick()
        assert(clicked)
    }
}