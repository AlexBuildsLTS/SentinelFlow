/**
 * SentinelFlow Neural Trend Module
 * Implements high-performance Canvas-based line charts.
 * Matches visual requirements from sentintenlivefeedon.png.
 */

package com.sentinel.mobile.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SentinelTrendChart(
    dataPoints: List<Float>,
    lineColor: Color = Color(0xFF10B981), // Sentinel Green
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
) {
    if (dataPoints.size < 2) return

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxData = dataPoints.maxOrNull() ?: 1f
        val minData = dataPoints.minOrNull() ?: 0f
        val range = (maxData - minData).coerceAtLeast(1f)

        val path = Path().apply {
            dataPoints.forEachIndexed { index, point ->
                val x = index * (width / (dataPoints.size - 1))
                val y = height - ((point - minData) / range * height)
                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }

        // Draw the smooth trend line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Draw the neon gradient under the line for depth
        val fillPath = android.graphics.Path(path.asAndroidPath()).asComposePath().apply {
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.2f), Color.Transparent)
            )
        )
    }
}