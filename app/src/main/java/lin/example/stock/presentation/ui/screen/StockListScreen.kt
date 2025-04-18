package lin.example.stock.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lin.example.stock.Resource
import lin.example.stock.domain.model.StockUiModel
import lin.example.stock.presentation.ui.model.StockListScreenState
import lin.example.stock.presentation.ui.model.StockSortOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListScreen(
    screenState: StockListScreenState = StockListScreenState(),
    onSortSelect: (StockSortOption) -> Unit = {}
) {
    val uiState = screenState.resource
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    )
    val scope = rememberCoroutineScope()

    // 1. 暫存「選中的股票」，用來控制 AlertDialog
    var selectedStock by remember { mutableStateOf<StockUiModel?>(null) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        topBar = {
            TopAppBar(
                title = { Text("上市個股日資訊") },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch { scaffoldState.bottomSheetState.expand() }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "排序")
                    }
                }
            )
        },
        sheetContent = {
            SortBottomSheet(
                current = screenState.sortOption,
                onSelect = {
                    onSortSelect(it)
                    scope.launch { scaffoldState.bottomSheetState.hide() }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is Resource.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                LazyColumn(
                    Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.data) { item ->
                        StockCard(item) {
                            // 2. 卡片點擊：記錄選中，觸發 Alert
                            selectedStock = item
                        }
                    }
                }
            }

            is Resource.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(uiState.message)
                }
            }
        }

        // 3. AlertDialog：顯示本益比／殖利率／股價淨值比
        selectedStock?.let { stock ->
            AlertDialog(
                onDismissRequest = { selectedStock = null },
                title = { Text(text = "${stock.code} ${stock.name}") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("本益比 (P/E): ${stock.peRatio?.let { "%.2f".format(it) } ?: "-"}")
                        Text("殖利率 (%): ${stock.dividendYield?.let { "%.2f".format(it) } ?: "-"}")
                        Text("股價淨值比 (P/B): ${stock.pbRatio?.let { "%.2f".format(it) } ?: "-"}")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedStock = null }) {
                        Text("關閉")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockListScreenPreview() {
    // 模擬 ViewModel 狀態
    val sampleList = List(5) { index ->
        StockUiModel(
            code = "000${index + 1}",
            name = "股票名稱${index + 1}",
            openPrice = 100.0 + index,
            highPrice = 110.0 + index,
            lowPrice = 90.0 + index,
            closePrice = 105.0 + index,
            monthlyAvgPrice = 102.0,
            tradeVolume = 1000 * (index + 1),
            tradeValue = 50000.0 * (index + 1),
            peRatio = 15.5,
            dividendYield = 3.2,
            pbRatio = 1.8,
            change = 3.0,
            transaction = 120 + index
        )
    }
    // 假裝這是 ViewModel 內的狀態
    StockListScreen(
        screenState = StockListScreenState(Resource.Success(sampleList)),
        onSortSelect = {}
    )
}