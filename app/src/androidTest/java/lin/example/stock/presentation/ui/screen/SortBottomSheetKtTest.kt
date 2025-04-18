package lin.example.stock.presentation.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import lin.example.stock.presentation.ui.model.StockSortOption
import org.junit.Rule
import org.junit.Test

class SortBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldDisplayTitleAndOptions_andInvokeCallbackOnClick() {
        var selected: StockSortOption? = null
        composeRule.setContent {
            SortBottomSheet(
                current = StockSortOption.CODE_ASC,
                onSelect = { selected = it }
            )
        }

        // 標題顯示
        composeRule.onNodeWithText("排序方式").assertExists()

        // 檢查所有選項文字存在
        StockSortOption.entries.forEach { option ->
            composeRule.onNodeWithText(option.title).assertExists()
        }

        // 點擊第二個選項
        val secondTitle = StockSortOption.entries[1].title
        composeRule.onAllNodesWithText(secondTitle).onFirst().performClick()

        // callback 應更新 selected
        assert(selected == StockSortOption.entries.toTypedArray()[1])
    }
}