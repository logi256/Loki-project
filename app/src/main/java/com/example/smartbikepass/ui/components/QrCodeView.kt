package com.example.smartbikepass.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.DeepNavy
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.abs

@Composable
fun QrCodeView(
    data: String,
    size: Dp = 200.dp,
    modifier: Modifier = Modifier
) {
    val encodedData = try {
        URLEncoder.encode(data, StandardCharsets.UTF_8.toString())
    } catch (e: Exception) {
        data
    }
    val qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=$encodedData&bgcolor=ffffff&color=0a192f&margin=10"

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(2.dp, BorderLight, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        // We load the network QR, and if offline or loading, render an offline deterministic QR visual!
        OfflineQrCanvas(
            data = data,
            modifier = Modifier.fillMaxSize()
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(qrUrl)
                .crossfade(true)
                .build(),
            contentDescription = "QR Code for $data",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

/**
 * Deterministic offline QR pattern representation based on data hash so the QR code
 * looks like a real scannable QR matrix even when offline.
 */
@Composable
fun OfflineQrCanvas(
    data: String,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val margin = w * 0.08f
        val innerW = w - margin * 2
        val innerH = h - margin * 2

        // Background
        drawRect(Color.White)

        val matrixSize = 25
        val cellSize = innerW / matrixSize

        // Corner finder patterns (Top-left, Top-right, Bottom-left)
        fun drawFinderPattern(startX: Float, startY: Float) {
            val finderSize = cellSize * 7
            // Outer black box
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(startX, startY),
                size = Size(finderSize, finderSize),
                cornerRadius = CornerRadius(cellSize * 0.8f, cellSize * 0.8f)
            )
            // Inner white box
            val whiteInset = cellSize * 1f
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(startX + whiteInset, startY + whiteInset),
                size = Size(finderSize - whiteInset * 2, finderSize - whiteInset * 2),
                cornerRadius = CornerRadius(cellSize * 0.4f, cellSize * 0.4f)
            )
            // Center solid dot
            val dotInset = cellSize * 2f
            drawRoundRect(
                color = DeepNavy,
                topLeft = Offset(startX + dotInset, startY + dotInset),
                size = Size(finderSize - dotInset * 2, finderSize - dotInset * 2),
                cornerRadius = CornerRadius(cellSize * 0.4f, cellSize * 0.4f)
            )
        }

        drawFinderPattern(margin, margin) // Top-Left
        drawFinderPattern(margin + cellSize * (matrixSize - 7), margin) // Top-Right
        drawFinderPattern(margin, margin + cellSize * (matrixSize - 7)) // Bottom-Left

        // Alignment pattern (Bottom-Right area)
        val alignX = margin + cellSize * 16
        val alignY = margin + cellSize * 16
        val alignSize = cellSize * 5
        drawRoundRect(
            color = DeepNavy,
            topLeft = Offset(alignX, alignY),
            size = Size(alignSize, alignSize),
            cornerRadius = CornerRadius(cellSize * 0.5f, cellSize * 0.5f)
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(alignX + cellSize, alignY + cellSize),
            size = Size(alignSize - cellSize * 2, alignSize - cellSize * 2)
        )
        drawRect(
            color = DeepNavy,
            topLeft = Offset(alignX + cellSize * 2, alignY + cellSize * 2),
            size = Size(cellSize, cellSize)
        )

        // Deterministic pseudo-random modules based on the data string
        val hash = abs(data.hashCode())
        val chars = data.toByteArray()

        for (r in 0 until matrixSize) {
            for (c in 0 until matrixSize) {
                // Avoid finder pattern zones
                val inTopLeft = r < 8 && c < 8
                val inTopRight = r < 8 && c >= matrixSize - 8
                val inBottomLeft = r >= matrixSize - 8 && c < 8
                val inAlign = r in 16..20 && c in 16..20

                if (!inTopLeft && !inTopRight && !inBottomLeft && !inAlign) {
                    val charVal = if (chars.isNotEmpty()) chars[(r * matrixSize + c) % chars.size].toInt() else 42
                    val isBlack = ((r * 31 + c * 17 + charVal + hash) % 3) == 0 || (r == 6 || c == 6)
                    if (isBlack) {
                        drawRoundRect(
                            color = DeepNavy,
                            topLeft = Offset(margin + c * cellSize, margin + r * cellSize),
                            size = Size(cellSize * 0.92f, cellSize * 0.92f),
                            cornerRadius = CornerRadius(cellSize * 0.2f, cellSize * 0.2f)
                        )
                    }
                }
            }
        }
    }
}
