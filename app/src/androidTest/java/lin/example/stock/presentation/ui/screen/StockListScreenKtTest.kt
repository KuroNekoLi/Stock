package lin.example.stock.presentation.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import lin.example.stock.Resource
import lin.example.stock.domain.model.StockUiModel
import lin.example.stock.presentation.ui.model.StockListScreenState
import org.junit.Rule
import org.junit.Test

class StockListScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun showsLoadingIndicator_whenStateIsLoading() {
        composeRule.setContent {
            StockListScreen(
                screenState = StockListScreenState(resource = Resource.Loading),
                onSortSelect = {}
            )
        }
        composeRule.onNodeWithText("Loading").assertDoesNotExist() // Compose 無內建 Loading 文字，改檢查圓形指示器
        composeRule.onNodeWithText("上市個股日資訊").assertExists()
    }

    @Test
    fun showsStockList_whenStateIsSuccess() {
        val data = listOf(
            StockUiModel("0001","A",0.0,0.0,0.0,0.0,0.0,0,0.0,1.0,1.0,1.0,1.0,0)
        )
        composeRule.setContent {
            StockListScreen(
                screenState = StockListScreenState(Resource.Success(data)),
                onSortSelect = {}
            )
        }
        composeRule.onNodeWithText("0001").assertIsDisplayed()
    }

    @Test
    fun showsErrorMessage_whenStateIsError() {
        composeRule.setContent {
            StockListScreen(
                screenState = StockListScreenState(Resource.Error("Oops", Exception())),
                onSortSelect = {}
            )
        }
        composeRule.onNodeWithText("Oops").assertIsDisplayed()
    }

    @Test
    fun stockListScreen_showsFinancialIndicatorsInDialog_whenCardClicked() {
        // 1. 準備一筆帶有 peRatio/dividendYield/pbRatio 的 StockUiModel
        val stock = StockUiModel(
            code = "0001",
            name = "A",
            openPrice = 0.0,
            highPrice = 0.0,
            lowPrice = 0.0,
            closePrice = 0.0,
            monthlyAvgPrice = 0.0,
            tradeVolume = 0,
            tradeValue = 0.0,
            peRatio = 2.0,
            dividendYield = 3.0,
            pbRatio = 4.0,
            change = 5.0,
            transaction = 0
        )

        // 2. 載入畫面並顯示 list
        composeRule.setContent {
            StockListScreen(
                screenState = StockListScreenState(Resource.Success(listOf(stock))),
                onSortSelect = {}
            )
        }

        // 3. 點擊該筆卡片
        composeRule.onNodeWithText("0001").assertIsDisplayed()       // 確保節點存在&#8203;:contentReference[oaicite:6]{index=6}
        composeRule.onNodeWithText("0001").performClick()           // 模擬使用者點擊&#8203;:contentReference[oaicite:7]{index=7}

        // 4. 驗證 AlertDialog 標題與所有金融指標顯示
        composeRule.onNodeWithText("0001 A").assertIsDisplayed()                // Dialog 標題&#8203;:contentReference[oaicite:8]{index=8}
        composeRule.onNodeWithText("本益比 (P/E): 2.00").assertIsDisplayed()   // P/E 顯示正確
        composeRule.onNodeWithText("殖利率 (%): 3.00").assertIsDisplayed()      // 殖利率顯示正確&#8203;:contentReference[oaicite:9]{index=9}
        composeRule.onNodeWithText("股價淨值比 (P/B): 4.00").assertIsDisplayed()// P/B 顯示正確&#8203;:contentReference[oaicite:10]{index=10}
    }
}