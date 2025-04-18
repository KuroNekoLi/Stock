package lin.example.stock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import lin.example.stock.presentation.ui.screen.StockListScreen
import lin.example.stock.presentation.ui.theme.StockTheme
import lin.example.stock.presentation.ui.viewmodel.TaifexViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: TaifexViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.loadStocks()
        setContent {
            val screenState by viewModel.state.collectAsState()
            StockTheme {
                StockListScreen(
                    screenState = screenState,
                    onSortSelect = { viewModel.onSortOptionSelected(it) }
                )
            }
        }
    }
}