package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.IncomeGreen
import com.example.data.model.Transaction
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite
import com.example.util.ChartDataHelper
import com.example.util.CurrencyHelper
import kotlin.math.roundToInt

/**
 * Data model for an individual interactive financial chart point
 */
data class FinancialDataPoint(
    val dateLabel: String,         // e.g. "26 Okt 2026"
    val shortDateLabel: String,    // e.g. "26 Okt"
    val value: Double,             // e.g. 458228.0
    val formattedValue: String = CurrencyHelper.formatRupiah(value),
    val percentChange: Double = 0.0,
    val deltaAmount: Double = 0.0
)

/**
 * Default color palette matching the financial reference video
 */
object FinancialChartTheme {
    val NeonGreen = Color(0xFF00E676) // Bright vibrant neon green line
    val NeonGreenGlow = Color(0xFF69F0AE)
    val DarkChartBg = Color(0xFF131A24) // Dark card surface
    val GridLineColor = Color(0xFF334155).copy(alpha = 0.35f)
    val TooltipBg = Color(0xFF1E2633)
}

/**
 * Generates dummy / sample series for each timeframe matching the video recording
 */
object FinancialLineChartDefaults {

    fun getDummyData(timeframe: String, baseBalance: Double = 2793428.0): List<FinancialDataPoint> {
        val base = if (baseBalance > 0) baseBalance else 2793428.0
        return when (timeframe) {
            "7D" -> listOf(
                FinancialDataPoint("1 Nov 2026", "1 Nov", base * 0.768, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("2 Nov 2026", "2 Nov", base * 0.787, percentChange = 2.5, deltaAmount = base * 0.019),
                FinancialDataPoint("3 Nov 2026", "3 Nov", base * 0.780, percentChange = 1.5, deltaAmount = base * 0.012),
                FinancialDataPoint("4 Nov 2026", "4 Nov", base * 0.841, percentChange = 9.5, deltaAmount = base * 0.073),
                FinancialDataPoint("5 Nov 2026", "5 Nov", base * 0.859, percentChange = 11.8, deltaAmount = base * 0.091),
                FinancialDataPoint("6 Nov 2026", "6 Nov", base * 0.923, percentChange = 20.2, deltaAmount = base * 0.155),
                FinancialDataPoint("7 Nov 2026", "7 Nov", base, percentChange = 21.3, deltaAmount = base * 0.232)
            )

            "1M" -> listOf(
                FinancialDataPoint("9 Okt 2026", "9 Okt", base * 0.283, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("13 Okt 2026", "13 Okt", base * 0.338, percentChange = 19.3, deltaAmount = base * 0.055),
                FinancialDataPoint("18 Okt 2026", "18 Okt", base * 0.293, percentChange = 3.6, deltaAmount = base * 0.010),
                FinancialDataPoint("22 Okt 2026", "22 Okt", base * 0.232, percentChange = -17.9, deltaAmount = -base * 0.051),
                FinancialDataPoint("26 Okt 2026", "26 Okt", base * 0.164, percentChange = -42.1, deltaAmount = -base * 0.119),
                FinancialDataPoint("30 Okt 2026", "30 Okt", base * 0.400, percentChange = 41.5, deltaAmount = base * 0.117),
                FinancialDataPoint("3 Nov 2026", "3 Nov", base * 0.780, percentChange = 175.4, deltaAmount = base * 0.497),
                FinancialDataPoint("7 Nov 2026", "7 Nov", base, percentChange = 125.8, deltaAmount = base * 0.717)
            )

            "3M" -> listOf(
                FinancialDataPoint("26 Agu 2026", "Agu 26", base * 0.701, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("4 Sep 2026", "4 Sep", base * 1.233, percentChange = 75.7, deltaAmount = base * 0.532),
                FinancialDataPoint("14 Sep 2026", "14 Sep", base * 0.626, percentChange = -10.7, deltaAmount = -base * 0.075),
                FinancialDataPoint("27 Sep 2026", "27 Sep", base * 0.257, percentChange = -63.2, deltaAmount = -base * 0.444),
                FinancialDataPoint("13 Okt 2026", "13 Okt", base * 0.338, percentChange = -51.8, deltaAmount = -base * 0.363),
                FinancialDataPoint("26 Okt 2026", "26 Okt", base * 0.164, percentChange = -76.6, deltaAmount = -base * 0.537),
                FinancialDataPoint("7 Nov 2026", "Nov 26", base, percentChange = 42.4, deltaAmount = base * 0.299)
            )

            "YTD" -> listOf(
                FinancialDataPoint("1 Jan 2026", "Jan 26", base * 0.617, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("15 Mar 2026", "Mar 26", base * 0.769, percentChange = 24.7, deltaAmount = base * 0.152),
                FinancialDataPoint("1 Mei 2026", "Mei 26", base * 0.676, percentChange = 9.6, deltaAmount = base * 0.059),
                FinancialDataPoint("4 Sep 2026", "Sep 26", base * 1.233, percentChange = 99.8, deltaAmount = base * 0.616),
                FinancialDataPoint("26 Okt 2026", "Okt 26", base * 0.164, percentChange = -73.4, deltaAmount = -base * 0.453),
                FinancialDataPoint("7 Nov 2026", "Nov 26", base, percentChange = 62.1, deltaAmount = base * 0.383)
            )

            else -> listOf( // "ALL"
                FinancialDataPoint("1 Jan 2025", "Jan 25", base * 0.304, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("1 Jun 2025", "Jun 25", base * 0.501, percentChange = 64.7, deltaAmount = base * 0.197),
                FinancialDataPoint("1 Jan 2026", "Jan 26", base * 0.617, percentChange = 102.8, deltaAmount = base * 0.313),
                FinancialDataPoint("4 Sep 2026", "Sep 26", base * 1.233, percentChange = 305.2, deltaAmount = base * 0.929),
                FinancialDataPoint("26 Okt 2026", "Okt 26", base * 0.164, percentChange = -46.1, deltaAmount = -base * 0.140),
                FinancialDataPoint("7 Nov 2026", "Nov 26", base, percentChange = 228.6, deltaAmount = base * 0.696)
            )
        }
    }

    /**
     * Generates realistic valuation history for an Asset across timeframes
     */
    fun getAssetData(timeframe: String, assetName: String, baseValuation: Double): List<FinancialDataPoint> {
        val base = if (baseValuation > 0) baseValuation else 10_000_000.0
        return when (timeframe) {
            "7D" -> listOf(
                FinancialDataPoint("1 Nov 2026", "1 Nov", base * 0.952, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("2 Nov 2026", "2 Nov", base * 0.965, percentChange = 1.4, deltaAmount = base * 0.013),
                FinancialDataPoint("3 Nov 2026", "3 Nov", base * 0.958, percentChange = 0.6, deltaAmount = base * 0.006),
                FinancialDataPoint("4 Nov 2026", "4 Nov", base * 0.978, percentChange = 2.7, deltaAmount = base * 0.026),
                FinancialDataPoint("5 Nov 2026", "5 Nov", base * 0.985, percentChange = 3.5, deltaAmount = base * 0.033),
                FinancialDataPoint("6 Nov 2026", "6 Nov", base * 0.992, percentChange = 4.2, deltaAmount = base * 0.040),
                FinancialDataPoint("7 Nov 2026", "7 Nov", base, percentChange = 5.0, deltaAmount = base * 0.048)
            )
            "1M" -> listOf(
                FinancialDataPoint("9 Okt 2026", "9 Okt", base * 0.820, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("13 Okt 2026", "13 Okt", base * 0.855, percentChange = 4.3, deltaAmount = base * 0.035),
                FinancialDataPoint("18 Okt 2026", "18 Okt", base * 0.840, percentChange = 2.4, deltaAmount = base * 0.020),
                FinancialDataPoint("22 Okt 2026", "22 Okt", base * 0.890, percentChange = 8.5, deltaAmount = base * 0.070),
                FinancialDataPoint("26 Okt 2026", "26 Okt", base * 0.915, percentChange = 11.6, deltaAmount = base * 0.095),
                FinancialDataPoint("30 Okt 2026", "30 Okt", base * 0.940, percentChange = 14.6, deltaAmount = base * 0.120),
                FinancialDataPoint("3 Nov 2026", "3 Nov", base * 0.975, percentChange = 18.9, deltaAmount = base * 0.155),
                FinancialDataPoint("7 Nov 2026", "7 Nov", base, percentChange = 22.0, deltaAmount = base * 0.180)
            )
            "3M" -> listOf(
                FinancialDataPoint("26 Agu 2026", "Agu 26", base * 0.720, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("4 Sep 2026", "4 Sep", base * 0.765, percentChange = 6.3, deltaAmount = base * 0.045),
                FinancialDataPoint("14 Sep 2026", "14 Sep", base * 0.810, percentChange = 12.5, deltaAmount = base * 0.090),
                FinancialDataPoint("27 Sep 2026", "27 Sep", base * 0.850, percentChange = 18.1, deltaAmount = base * 0.130),
                FinancialDataPoint("13 Okt 2026", "13 Okt", base * 0.895, percentChange = 24.3, deltaAmount = base * 0.175),
                FinancialDataPoint("26 Okt 2026", "26 Okt", base * 0.940, percentChange = 30.6, deltaAmount = base * 0.220),
                FinancialDataPoint("7 Nov 2026", "Nov 26", base, percentChange = 38.9, deltaAmount = base * 0.280)
            )
            "YTD" -> listOf(
                FinancialDataPoint("1 Jan 2026", "Jan 26", base * 0.600, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("15 Mar 2026", "Mar 26", base * 0.680, percentChange = 13.3, deltaAmount = base * 0.080),
                FinancialDataPoint("1 Mei 2026", "Mei 26", base * 0.750, percentChange = 25.0, deltaAmount = base * 0.150),
                FinancialDataPoint("4 Sep 2026", "Sep 26", base * 0.860, percentChange = 43.3, deltaAmount = base * 0.260),
                FinancialDataPoint("26 Okt 2026", "Okt 26", base * 0.940, percentChange = 56.7, deltaAmount = base * 0.340),
                FinancialDataPoint("7 Nov 2026", "Nov 26", base, percentChange = 66.7, deltaAmount = base * 0.400)
            )
            else -> listOf( // "ALL"
                FinancialDataPoint("1 Jan 2025", "Jan 25", base * 0.420, percentChange = 0.0, deltaAmount = 0.0),
                FinancialDataPoint("1 Jun 2025", "Jun 25", base * 0.550, percentChange = 31.0, deltaAmount = base * 0.130),
                FinancialDataPoint("1 Jan 2026", "Jan 26", base * 0.680, percentChange = 61.9, deltaAmount = base * 0.260),
                FinancialDataPoint("4 Sep 2026", "Sep 26", base * 0.850, percentChange = 102.4, deltaAmount = base * 0.430),
                FinancialDataPoint("26 Okt 2026", "Okt 26", base * 0.930, percentChange = 121.4, deltaAmount = base * 0.510),
                FinancialDataPoint("7 Nov 2026", "Nov 26", base, percentChange = 138.1, deltaAmount = base * 0.580)
            )
        }
    }

    /**
     * Generates realistic remaining balance trajectory for a Debt across timeframes
     */
    fun getDebtData(timeframe: String, debtName: String, remainingAmount: Double, totalAmount: Double): List<FinancialDataPoint> {
        val total = if (totalAmount > 0) totalAmount else (remainingAmount * 1.5).coerceAtLeast(5_000_000.0)
        val rem = remainingAmount.coerceIn(0.0, total)
        val paid = (total - rem).coerceAtLeast(0.0)

        return when (timeframe) {
            "7D" -> {
                val start = (rem + (paid * 0.08).coerceAtLeast(100_000.0)).coerceAtMost(total)
                val diff = rem - start
                val pct = if (start > 0) (diff / start) * 100.0 else 0.0
                listOf(
                    FinancialDataPoint("1 Nov 2026", "1 Nov", start, percentChange = 0.0, deltaAmount = 0.0),
                    FinancialDataPoint("2 Nov 2026", "2 Nov", start + diff * 0.15, percentChange = pct * 0.15, deltaAmount = diff * 0.15),
                    FinancialDataPoint("3 Nov 2026", "3 Nov", start + diff * 0.30, percentChange = pct * 0.30, deltaAmount = diff * 0.30),
                    FinancialDataPoint("4 Nov 2026", "4 Nov", start + diff * 0.45, percentChange = pct * 0.45, deltaAmount = diff * 0.45),
                    FinancialDataPoint("5 Nov 2026", "5 Nov", start + diff * 0.65, percentChange = pct * 0.65, deltaAmount = diff * 0.65),
                    FinancialDataPoint("6 Nov 2026", "6 Nov", start + diff * 0.85, percentChange = pct * 0.85, deltaAmount = diff * 0.85),
                    FinancialDataPoint("7 Nov 2026", "7 Nov", rem, percentChange = pct, deltaAmount = diff)
                )
            }
            "1M" -> {
                val start = (rem + (paid * 0.45).coerceAtLeast(300_000.0)).coerceAtMost(total)
                val diff = rem - start
                val pct = if (start > 0) (diff / start) * 100.0 else 0.0
                listOf(
                    FinancialDataPoint("9 Okt 2026", "9 Okt", start, percentChange = 0.0, deltaAmount = 0.0),
                    FinancialDataPoint("13 Okt 2026", "13 Okt", start + diff * 0.15, percentChange = pct * 0.15, deltaAmount = diff * 0.15),
                    FinancialDataPoint("18 Okt 2026", "18 Okt", start + diff * 0.30, percentChange = pct * 0.30, deltaAmount = diff * 0.30),
                    FinancialDataPoint("22 Okt 2026", "22 Okt", start + diff * 0.45, percentChange = pct * 0.45, deltaAmount = diff * 0.45),
                    FinancialDataPoint("26 Okt 2026", "26 Okt", start + diff * 0.60, percentChange = pct * 0.60, deltaAmount = diff * 0.60),
                    FinancialDataPoint("30 Okt 2026", "30 Okt", start + diff * 0.75, percentChange = pct * 0.75, deltaAmount = diff * 0.75),
                    FinancialDataPoint("3 Nov 2026", "3 Nov", start + diff * 0.90, percentChange = pct * 0.90, deltaAmount = diff * 0.90),
                    FinancialDataPoint("7 Nov 2026", "7 Nov", rem, percentChange = pct, deltaAmount = diff)
                )
            }
            "3M" -> {
                val start = (rem + (paid * 0.75).coerceAtLeast(600_000.0)).coerceAtMost(total)
                val diff = rem - start
                val pct = if (start > 0) (diff / start) * 100.0 else 0.0
                listOf(
                    FinancialDataPoint("26 Agu 2026", "Agu 26", start, percentChange = 0.0, deltaAmount = 0.0),
                    FinancialDataPoint("4 Sep 2026", "4 Sep", start + diff * 0.18, percentChange = pct * 0.18, deltaAmount = diff * 0.18),
                    FinancialDataPoint("14 Sep 2026", "14 Sep", start + diff * 0.35, percentChange = pct * 0.35, deltaAmount = diff * 0.35),
                    FinancialDataPoint("27 Sep 2026", "27 Sep", start + diff * 0.52, percentChange = pct * 0.52, deltaAmount = diff * 0.52),
                    FinancialDataPoint("13 Okt 2026", "13 Okt", start + diff * 0.68, percentChange = pct * 0.68, deltaAmount = diff * 0.68),
                    FinancialDataPoint("26 Okt 2026", "26 Okt", start + diff * 0.85, percentChange = pct * 0.85, deltaAmount = diff * 0.85),
                    FinancialDataPoint("7 Nov 2026", "Nov 26", rem, percentChange = pct, deltaAmount = diff)
                )
            }
            "YTD" -> {
                val start = total
                val diff = rem - start
                val pct = if (start > 0) (diff / start) * 100.0 else 0.0
                listOf(
                    FinancialDataPoint("1 Jan 2026", "Jan 26", start, percentChange = 0.0, deltaAmount = 0.0),
                    FinancialDataPoint("15 Mar 2026", "Mar 26", start + diff * 0.20, percentChange = pct * 0.20, deltaAmount = diff * 0.20),
                    FinancialDataPoint("1 Mei 2026", "Mei 26", start + diff * 0.40, percentChange = pct * 0.40, deltaAmount = diff * 0.40),
                    FinancialDataPoint("4 Sep 2026", "Sep 26", start + diff * 0.65, percentChange = pct * 0.65, deltaAmount = diff * 0.65),
                    FinancialDataPoint("26 Okt 2026", "Okt 26", start + diff * 0.85, percentChange = pct * 0.85, deltaAmount = diff * 0.85),
                    FinancialDataPoint("7 Nov 2026", "Nov 26", rem, percentChange = pct, deltaAmount = diff)
                )
            }
            else -> { // "ALL"
                val start = total
                val diff = rem - start
                val pct = if (start > 0) (diff / start) * 100.0 else 0.0
                listOf(
                    FinancialDataPoint("1 Jan 2025", "Jan 25", start, percentChange = 0.0, deltaAmount = 0.0),
                    FinancialDataPoint("1 Jun 2025", "Jun 25", start + diff * 0.25, percentChange = pct * 0.25, deltaAmount = diff * 0.25),
                    FinancialDataPoint("1 Jan 2026", "Jan 26", start + diff * 0.50, percentChange = pct * 0.50, deltaAmount = diff * 0.50),
                    FinancialDataPoint("4 Sep 2026", "Sep 26", start + diff * 0.75, percentChange = pct * 0.75, deltaAmount = diff * 0.75),
                    FinancialDataPoint("26 Okt 2026", "Okt 26", start + diff * 0.90, percentChange = pct * 0.90, deltaAmount = diff * 0.90),
                    FinancialDataPoint("7 Nov 2026", "Nov 26", rem, percentChange = pct, deltaAmount = diff)
                )
            }
        }
    }
}

/**
 * Interactive financial line chart canvas with touch scrubbing, vertical dashed cursor,
 * neon green gradient, and floating dynamic tooltip.
 */
@Composable
fun FinancialLineCanvas(
    points: List<FinancialDataPoint>,
    lineColor: Color = FinancialChartTheme.NeonGreen,
    modifier: Modifier = Modifier,
    onScrub: (FinancialDataPoint?) -> Unit = {},
    showHorizontalGrid: Boolean = true
) {
    if (points.isEmpty()) return

    val minVal = remember(points) { points.minOf { it.value } }
    val maxVal = remember(points) { points.maxOf { it.value } }
    val range = remember(minVal, maxVal) { maxVal - minVal }

    // Touch scrubbing states
    var isDragging by remember { mutableStateOf(false) }
    var touchX by remember { mutableFloatStateOf(0f) }
    var scrubIndex by remember { mutableIntStateOf(-1) }

    Box(
        modifier = modifier
            .pointerInput(points) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val canvasWidth = size.width.toFloat()
                    if (points.isNotEmpty() && canvasWidth > 0f) {
                        isDragging = true
                        val dragX = down.position.x
                        touchX = dragX
                        val index = if (points.size > 1) {
                            ((dragX / canvasWidth) * (points.size - 1)).roundToInt().coerceIn(0, points.size - 1)
                        } else {
                            0
                        }
                        scrubIndex = index
                        onScrub(points[index])
                    }

                    val pointerId = down.id
                    while (true) {
                        val event = awaitPointerEvent()
                        val dragChange = event.changes.firstOrNull { it.id == pointerId } ?: break
                        if (!dragChange.pressed) {
                            break
                        }
                        val canvasWidth = size.width.toFloat()
                        val dragX = dragChange.position.x
                        touchX = dragX
                        if (points.isNotEmpty() && canvasWidth > 0f) {
                            val index = if (points.size > 1) {
                                ((dragX / canvasWidth) * (points.size - 1)).roundToInt().coerceIn(0, points.size - 1)
                            } else {
                                0
                            }
                            if (index != scrubIndex) {
                                scrubIndex = index
                                onScrub(points[index])
                            }
                        }
                        dragChange.consume()
                    }

                    isDragging = false
                    scrubIndex = -1
                    onScrub(null)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Horizontal dashed grid reference lines
            if (showHorizontalGrid) {
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                val gridLines = 3
                for (i in 1..gridLines) {
                    val y = h * (i.toFloat() / (gridLines + 1))
                    drawLine(
                        color = FinancialChartTheme.GridLineColor,
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = dashEffect
                    )
                }
            }

            if (points.size < 2) {
                if (points.size == 1) {
                    val y = h * 0.5f
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 2.4.dp.toPx()
                    )
                    drawCircle(
                        color = lineColor,
                        radius = 4.dp.toPx(),
                        center = Offset(w * 0.5f, y)
                    )
                }
                return@Canvas
            }

            val stepX = w / (points.size - 1).coerceAtLeast(1)

            // Convert raw values to normalized Y coordinates (with 12% padding top & bottom)
            val coords = points.mapIndexed { idx, pt ->
                val x = idx * stepX
                val normalizedY = if (range < 0.001) 0.5f else ((pt.value - minVal) / range).toFloat()
                val y = h - (normalizedY * h * 0.76f + h * 0.12f)
                Offset(x, y)
            }

            // 2. Smooth Bezier Curve Path
            val linePath = Path()
            val fillPath = Path()
            fillPath.moveTo(coords.first().x, h)
            fillPath.lineTo(coords.first().x, coords.first().y)
            linePath.moveTo(coords.first().x, coords.first().y)

            for (i in 0 until coords.size - 1) {
                val p0 = coords[i]
                val p1 = coords[i + 1]
                val cx1 = p0.x + (p1.x - p0.x) / 2f
                val cy1 = p0.y
                val cx2 = p0.x + (p1.x - p0.x) / 2f
                val cy2 = p1.y

                linePath.cubicTo(cx1, cy1, cx2, cy2, p1.x, p1.y)
                fillPath.cubicTo(cx1, cy1, cx2, cy2, p1.x, p1.y)
            }

            fillPath.lineTo(coords.last().x, h)
            fillPath.close()

            // 3. Draw Gradient Area Fill under Line
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.28f),
                        lineColor.copy(alpha = 0.06f),
                        Color.Transparent
                    )
                )
            )

            // 4. Draw Neon Green Line
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(
                    width = 2.4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // 5. Active Touch Scrubbing Guideline & Marker Point
            if (isDragging && scrubIndex in coords.indices) {
                val activePt = coords[scrubIndex]
                val activeX = activePt.x
                val activeY = activePt.y

                // Vertical dashed tracking line
                val vDashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 5f), 0f)
                drawLine(
                    color = lineColor.copy(alpha = 0.75f),
                    start = Offset(activeX, 0f),
                    end = Offset(activeX, h),
                    strokeWidth = 1.2.dp.toPx(),
                    pathEffect = vDashEffect
                )

                // Glowing outer marker ring
                drawCircle(
                    color = lineColor.copy(alpha = 0.35f),
                    radius = 9.dp.toPx(),
                    center = Offset(activeX, activeY)
                )

                // Solid neon green dot
                drawCircle(
                    color = lineColor,
                    radius = 5.5.dp.toPx(),
                    center = Offset(activeX, activeY)
                )

                // Inner white sparkle
                drawCircle(
                    color = Color.White,
                    radius = 2.5.dp.toPx(),
                    center = Offset(activeX, activeY)
                )
            }
        }

        // 6. Floating Tooltip Card over the active point
        if (isDragging && scrubIndex in points.indices) {
            val point = points[scrubIndex]
            val stepXPercent = scrubIndex.toFloat() / (points.size - 1).coerceAtLeast(1)

            // Calculate tooltip horizontal alignment offset smoothly
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                val alignBias = (stepXPercent * 2f - 1f).coerceIn(-0.85f, 0.85f)

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = FinancialChartTheme.TooltipBg),
                    border = BorderStroke(1.dp, lineColor.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .align(BiasAlignment(alignBias, -0.75f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = point.dateLabel,
                            fontSize = 10.5.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val pctSign = if (point.percentChange >= 0) "+" else ""
                        val pctFormatted = String.format(java.util.Locale.US, "%.1f", point.percentChange).replace('.', ',')
                        Text(
                            text = "${point.formattedValue} ($pctSign$pctFormatted%)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }
    }
}

/**
 * Complete Financial Chart Card Component matching the reference video:
 * - Dynamic Timeframe Tabs: [ 7D ] [ 1M ] [ 3M ] [ YTD ] [ ALL ]
 * - Expand fullscreen action button [ ⛶ ]
 * - Dark Card styling with 16dp rounded corners
 * - Responsive scrubbing with live header update
 * - Detailed BottomSheet modal with Y-axis currency scale labels
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveFinancialChartCard(
    currentBalance: Double = 2793428.0,
    walletCount: Int = 4,
    planAmount: Double = 5443428.0,
    title: String? = null,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    customPointsMap: Map<String, List<FinancialDataPoint>>? = null,
    transactions: List<Transaction> = emptyList(),
    isDebtChart: Boolean = false,
    lineColor: Color? = null
) {
    var isBalanceHidden by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("1M") }
    var showExpandedSheet by remember { mutableStateOf(false) }

    val periods = listOf("7D", "1M", "3M", "YTD", "ALL")

    // Active dataset for selected period
    val activePoints = remember(selectedPeriod, currentBalance, customPointsMap, transactions, isDebtChart) {
        val mapped = customPointsMap?.get(selectedPeriod)
        if (!mapped.isNullOrEmpty()) {
            mapped
        } else if (transactions.isNotEmpty()) {
            ChartDataHelper.generateFinancialPoints(
                timeframe = selectedPeriod,
                transactions = transactions,
                currentBalance = currentBalance,
                isDebt = isDebtChart
            )
        } else if (isDebtChart) {
            ChartDataHelper.generateAllTimeframePoints(emptyList(), currentBalance, isDebt = true)[selectedPeriod] ?: emptyList()
        } else {
            ChartDataHelper.generateAllTimeframePoints(emptyList(), currentBalance)[selectedPeriod] ?: emptyList()
        }
    }

    // Scrubbed point state (null when user is not touching the chart)
    var scrubbedPoint by remember { mutableStateOf<FinancialDataPoint?>(null) }

    // Dynamically displayed metrics based on scrubbed point vs latest point
    val latestPoint = activePoints.lastOrNull()
    val displayPoint = scrubbedPoint ?: latestPoint

    val displayAmount = displayPoint?.value ?: currentBalance
    val displayDelta = displayPoint?.deltaAmount ?: (latestPoint?.deltaAmount ?: 0.0)
    val displayPercent = displayPoint?.percentChange ?: (latestPoint?.percentChange ?: 0.0)

    // Check if the graph is rising or falling
    val firstPoint = activePoints.firstOrNull()
    val isGraphRising = if (scrubbedPoint != null && firstPoint != null) {
        scrubbedPoint!!.value >= firstPoint.value
    } else if (activePoints.size >= 2) {
        activePoints.last().value >= activePoints.first().value
    } else {
        displayDelta >= 0.0
    }

    // Dynamic green when rising (up), red when falling (down) as requested:
    // "Sesuaikan warna grafik ketika grafik naik berwarna hijau ketika grafik turun berwarna merah"
    val effectiveChartColor = lineColor ?: if (isGraphRising) FinancialChartTheme.NeonGreen else ExpenseRed

    // For debt charts: negative delta (debt reduction) is healthy/positive
    val isGoodTrend = if (isDebtChart) displayDelta <= 0 else isGraphRising
    val trendColor = if (isGoodTrend) IncomeGreen else ExpenseRed
    val trendIcon = if (displayDelta >= 0) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown
    val deltaSign = if (displayDelta >= 0) "+" else ""

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FinancialChartTheme.DarkChartBg),
        border = BorderStroke(1.dp, DarkCardBorder.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            // 1. Top Balance & Visibility Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBalanceHidden) "Rp ••••••••" else CurrencyHelper.formatRupiah(displayAmount),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = { isBalanceHidden = !isBalanceHidden },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isBalanceHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle saldo",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            // 2. Trend Badge Pill: e.g. [ ↗ +Rp2.001.737  +125.8%  1M ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DarkSurfaceElevated,
                    border = BorderStroke(1.dp, trendColor.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = trendIcon,
                            contentDescription = null,
                            tint = trendColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${deltaSign}${CurrencyHelper.formatRupiah(displayDelta)}",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = trendColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        val pctFormatted = String.format(java.util.Locale.US, "%.1f", displayPercent).replace('.', ',')
                        Text(
                            text = "${deltaSign}${pctFormatted}%",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = trendColor
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = selectedPeriod,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3. Subtext Plan note & Total Saldo caption
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (scrubbedPoint != null) {
                        "📅 ${scrubbedPoint!!.dateLabel} · Saldo: ${scrubbedPoint!!.formattedValue}"
                    } else {
                        subtitle ?: "📈 Dalam 30 hari, sesuai rencana: ${CurrencyHelper.formatRupiah(planAmount)}"
                    },
                    fontSize = 10.5.sp,
                    color = if (scrubbedPoint != null) effectiveChartColor else TextSubtle,
                    fontWeight = if (scrubbedPoint != null) FontWeight.SemiBold else FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.height(1.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title ?: "Total Saldo Tersedia · $walletCount Dompet",
                    fontSize = 11.5.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Timeframe Selector Capsule Tabs & Fullscreen Expand button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timeframe Selector Tabs (Capsule style: neon green active, dark transparent inactive)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkSurfaceElevated,
                    border = BorderStroke(1.dp, DarkCardBorder.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(2.5.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        periods.forEach { p ->
                            val isSelected = selectedPeriod == p
                            val tabBg = if (isSelected) effectiveChartColor else Color(0x33334155)
                            val tabTextColor = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8)

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = tabBg,
                                modifier = Modifier.clickable {
                                    selectedPeriod = p
                                    scrubbedPoint = null
                                }
                            ) {
                                Text(
                                    text = p,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = tabTextColor,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Fullscreen Expand icon button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceElevated,
                    border = BorderStroke(1.dp, DarkCardBorder.copy(alpha = 0.5f)),
                    modifier = Modifier.clickable { showExpandedSheet = true }
                ) {
                    Box(
                        modifier = Modifier.size(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Perluas grafik",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Interactive Canvas Chart
            FinancialLineCanvas(
                points = activePoints,
                lineColor = effectiveChartColor,
                onScrub = { point -> scrubbedPoint = point },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 6. X-Axis Date Labels (Left and Right dates)
            val firstDate = activePoints.firstOrNull()?.shortDateLabel ?: ""
            val lastDate = activePoints.lastOrNull()?.shortDateLabel ?: ""

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = firstDate,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = lastDate,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // 7. Expanded Modal Bottom Sheet matching video at 00:18
    if (showExpandedSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showExpandedSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF101725),
            dragHandle = {
                Surface(
                    shape = RoundedCornerShape(2.dp),
                    color = Color(0xFF475569),
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(width = 36.dp, height = 4.dp)
                ) {}
            }
        ) {
            ExpandedFinancialChartView(
                points = activePoints,
                selectedPeriod = selectedPeriod,
                title = title ?: "Total Saldo Tersedia",
                lineColor = effectiveChartColor,
                onDismiss = { showExpandedSheet = false }
            )
        }
    }
}

/**
 * Expanded full-view chart displayed inside the ModalBottomSheet with Y-axis scale labels
 * (e.g. 4.3M, 3.3M, 2.2M, 1.1M) as shown in the reference video at 00:18-00:20.
 */
@Composable
fun ExpandedFinancialChartView(
    points: List<FinancialDataPoint>,
    selectedPeriod: String,
    title: String = "Total Saldo Tersedia",
    lineColor: Color? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val isGraphRising = if (points.size >= 2) {
        points.last().value >= points.first().value
    } else {
        true
    }
    val effectiveLineColor = lineColor ?: if (isGraphRising) FinancialChartTheme.NeonGreen else ExpenseRed

    val minVal = points.minOf { it.value }
    val maxVal = points.maxOf { it.value }

    // Format Y-axis scale values (e.g. 4.3M, 3.3M, 2.2M, 1.1M)
    val yLabels = remember(minVal, maxVal) {
        val count = 4
        if (maxVal - minVal < 0.001) {
            val v = maxVal
            listOf(v * 1.25, v, v * 0.75, v * 0.5).map { CurrencyHelper.formatCompact(it) }
        } else {
            val step = (maxVal - minVal) / (count - 1).coerceAtLeast(1).toDouble()
            (0 until count).map { i ->
                val v = maxVal - i * step
                CurrencyHelper.formatCompact(v)
            }
        }
    }

    var scrubbedPoint by remember { mutableStateOf<FinancialDataPoint?>(null) }
    val displayPoint = scrubbedPoint ?: points.lastOrNull()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Sheet Title & Close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup",
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Large balance and date readout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = displayPoint?.formattedValue ?: "-",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = displayPoint?.dateLabel ?: "-",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = effectiveLineColor.copy(alpha = 0.18f),
                border = BorderStroke(1.dp, effectiveLineColor.copy(alpha = 0.4f))
            ) {
                Text(
                    text = selectedPeriod,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = effectiveLineColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Chart area with Y-axis labels on left
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Y-Axis labels
            Column(
                modifier = Modifier
                    .width(42.dp)
                    .height(240.dp)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                yLabels.forEach { lbl ->
                    Text(
                        text = lbl,
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Canvas
            FinancialLineCanvas(
                points = points,
                lineColor = effectiveLineColor,
                onScrub = { scrubbedPoint = it },
                modifier = Modifier
                    .weight(1f)
                    .height(240.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // X-Axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = points.firstOrNull()?.shortDateLabel ?: "",
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = points.lastOrNull()?.shortDateLabel ?: "",
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
