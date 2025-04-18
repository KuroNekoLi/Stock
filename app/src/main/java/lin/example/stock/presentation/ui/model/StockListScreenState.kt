package lin.example.stock.presentation.ui.model

import lin.example.stock.Resource
import lin.example.stock.domain.model.StockUiModel

/**
 * 畫面專用狀態資料，將 UI 需要的所有欄位聚合在一起：
 *  - [resource]：資料載入成功/失敗/進行中
 *  - [sortOption]：目前的排序方式
 */
data class StockListScreenState(
    val resource: Resource<List<StockUiModel>> = Resource.Loading,
    val sortOption: StockSortOption = StockSortOption.CODE_DESC
)