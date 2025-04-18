package lin.example.stock.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lin.example.stock.R
import lin.example.stock.domain.model.StockUiModel
import lin.example.stock.presentation.ui.theme.LocalCustomColors
import lin.example.stock.presentation.ui.theme.StockTheme

/**
 * 股票資訊卡片
 *
 * @param item      [StockUiModel] 單筆股票資料
 * @param onClick   點擊卡片的回呼
 */
@Composable
fun StockCard(
    item: StockUiModel,
    onClick: () -> Unit
) {
    // 決定顏色
    val closeColor =
        if (item.closePrice > item.monthlyAvgPrice) Color.Red else LocalCustomColors.current.fall
    val diffValue = item.change ?: 0.0
    val diffColor = if (diffValue >= 0) Color.Red else LocalCustomColors.current.fall

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 標題
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.code,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // 第一排：開盤價、收盤價
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                MetricItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.open_price),
                    value = "%.2f".format(item.openPrice)
                )
                MetricItem(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("closePrice_${if (closeColor == Color.Red) "up" else "down"}"),
                    label = stringResource(R.string.close_price),
                    value = "%.2f".format(item.closePrice),
                    valueColor = closeColor
                )
            }

            // 第二排：最高價、最低價
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                MetricItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.high_price),
                    value = "%.2f".format(item.highPrice)
                )
                MetricItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.low_price),
                    value = "%.2f".format(item.lowPrice)
                )
            }

            // 第三排：漲跌價差、月平均價
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                MetricItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.price_diff),
                    value = "%.2f".format(diffValue),
                    valueColor = diffColor
                )
                MetricItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.monthly_avg_price),
                    value = "%.2f".format(item.monthlyAvgPrice)
                )
            }

            // 第四排：成交筆數、成交股數、成交金額
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricItem(
                    label = stringResource(R.string.transaction_count),
                    value = item.transaction?.toString() ?: "-"
                )
                MetricItem(
                    label = stringResource(R.string.trade_volume),
                    value = item.tradeVolume.toString()
                )
                MetricItem(
                    label = stringResource(R.string.trade_value),
                    value = item.tradeValue.toInt().toString()
                )
            }
        }
    }
}

/**
 * 單一指標項目：標籤與數值水平排列
 *
 * @param modifier    版面配置
 * @param label       欄位名稱
 * @param value       欄位值
 * @param valueColor  值顏色，預設不變
 */
@Composable
private fun MetricItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value,
            color = valueColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StockCardPreview() {
    StockTheme {
        StockCard(
            item = StockUiModel(
                code = "0001",
                name = "股票名稱",
                openPrice = 100.0,
                highPrice = 110.0,
                lowPrice = 90.0,
                closePrice = 105.0,
                monthlyAvgPrice = 102.0,
                tradeVolume = 1000,
                tradeValue = 50000.0,
                peRatio = 15.5,
                dividendYield = 3.2,
                pbRatio = 1.8,
                change = 3.0,
                transaction = 120
            ),
            onClick = {}
        )
    }
}
