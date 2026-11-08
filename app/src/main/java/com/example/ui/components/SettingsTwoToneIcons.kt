package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentBlue
import kotlin.math.cos
import kotlin.math.sin

/**
 * Enumeration of all settings menu items with corresponding 2-tone icons.
 * Each icon is crafted with two-tone styling: theme color + Fixed Yellow (warna kuning fix).
 */
enum class SettingsIconType {
    LANGUAGE,               // Bahasa
    DARK_MODE,              // Mode Gelap
    THEME_COLOR,            // Warna Tema
    VIBRATION,              // Getaran & Haptic Feedback
    TEXT_SIZE,              // Ukuran Teks
    CURRENCY,               // Mata Uang Utama
    EXCHANGE_RATE,          // Kurs Mata Uang
    NUMBER_FORMAT,          // Format Angka
    DATE_FORMAT,            // Format Tanggal
    START_DAY_WEEK,         // Hari Pertama Mingguan
    START_DAY_MONTH,        // Tanggal Mulai Bulanan / Gajian
    SAVINGS_TARGET,         // Target Rasio Menabung
    CASH_ACCOUNTS_ONLY,     // Akun Tunai Saja
    SHOW_PLANNED,           // Tampilkan Uang Terencana di Saldo
    DAILY_REMINDER,         // Pengingat Catat Harian
    REMINDER_TIME,          // Waktu Pengingat Harian
    BUDGET_ALERT,           // Peringatan Anggaran
    BUDGET_THRESHOLD,       // Ambang Batas Peringatan
    BILL_REMINDER,          // Pengingat Tagihan & Tempo
    SAVE_REMINDER,          // Pengingat Menabung Rutin
    SMART_INSIGHTS,         // Smart Financial Insights
    CATEGORIES,             // Kategori Transaksi
    LABELS,                 // Label Transaksi
    RECURRING,              // Transaksi Berulang
    NAV_ORDER,              // Urutan Menu Utama
    EXPORT_PDF,             // Ekspor Laporan PDF
    EXPORT_CSV,             // Ekspor Data Excel / CSV
    APP_PASSCODE,           // Kode Sandi / PIN Aplikasi
    HIDE_BALANCE,           // Mode Privasi (Sembunyikan Saldo)
    CONFIRM_DELETE,         // Konfirmasi Hapus Transaksi
    BACKUP_RESTORE,         // Cadangkan & Pulihkan
    ABOUT_APP,              // Tentang Money Tracker
    PRIVACY_POLICY,         // Kebijakan Privasi
    FEEDBACK,               // Saran & Masukan
    SHARE,                  // Bagikan ke Teman
    RESET_DATA              // Reset Data Aplikasi
}

@Composable
fun SettingsTwoToneIcon(
    type: SettingsIconType,
    themeColor: Color = AccentBlue,
    modifier: Modifier = Modifier.size(22.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        when (type) {
            SettingsIconType.LANGUAGE -> {
                // 1. Globe body with latitude/longitude
                val radius = minOf(w, h) * 0.42f
                val center = Offset(w / 2f, h / 2f)
                drawCircle(color = themeColor.copy(alpha = 0.20f), radius = radius, center = center)
                drawCircle(color = themeColor, radius = radius, center = center, style = Stroke(1.8.dp.toPx()))

                // Longitude ellipse
                drawOval(
                    color = themeColor,
                    topLeft = Offset(center.x - radius * 0.45f, center.y - radius),
                    size = Size(radius * 0.90f, radius * 2f),
                    style = Stroke(1.2.dp.toPx())
                )
                // Latitude line
                drawLine(
                    color = themeColor,
                    start = Offset(center.x - radius, center.y),
                    end = Offset(center.x + radius, center.y),
                    strokeWidth = 1.2.dp.toPx()
                )

                // 2. Fixed Yellow Accent: Golden location star at top-right orbit
                val starC = Offset(w * 0.78f, h * 0.24f)
                val sR = w * 0.14f
                val starPath = Path()
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45.0) - 90.0)
                    val rCur = if (i % 2 == 0) sR else sR * 0.4f
                    val px = starC.x + (rCur * cos(angle)).toFloat()
                    val py = starC.y + (rCur * sin(angle)).toFloat()
                    if (i == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)
                }
                starPath.close()
                drawPath(path = starPath, color = FixedYellow)
            }

            SettingsIconType.DARK_MODE -> {
                // 1. Crescent Moon in themeColor
                val moonPath = Path().apply {
                    val r = minOf(w, h) * 0.40f
                    val cx = w * 0.44f
                    val cy = h * 0.52f
                    moveTo(cx, cy - r)
                    arcTo(
                        rect = androidx.compose.ui.geometry.Rect(cx - r, cy - r, cx + r, cy + r),
                        startAngleDegrees = -90f,
                        sweepAngleDegrees = 240f,
                        forceMoveTo = false
                    )
                    quadraticTo(cx + r * 0.2f, cy + r * 0.3f, cx, cy - r)
                    close()
                }
                drawPath(path = moonPath, color = themeColor)

                // 2. Fixed Yellow Twinkle Stars
                val star1 = Offset(w * 0.76f, h * 0.24f)
                val star2 = Offset(w * 0.82f, h * 0.62f)
                drawCircle(color = FixedYellow, radius = 2.4.dp.toPx(), center = star1)
                drawCircle(color = FixedYellowLight, radius = 1.1.dp.toPx(), center = star1)
                drawCircle(color = FixedYellow, radius = 1.8.dp.toPx(), center = star2)
            }

            SettingsIconType.THEME_COLOR -> {
                // 1. Palette shape
                val palW = w * 0.78f
                val palH = h * 0.68f
                val palX = (w - palW) / 2f
                val palY = (h - palH) / 2f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(palX, palY),
                    size = Size(palW, palH),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
                // Thumb hole
                drawCircle(color = Color(0xFF131822), radius = 2.0.dp.toPx(), center = Offset(palX + palW * 0.80f, palY + palH * 0.50f))

                // Paint dots
                drawCircle(color = Color.White.copy(alpha = 0.8f), radius = 1.6.dp.toPx(), center = Offset(palX + palW * 0.25f, palY + palH * 0.35f))
                drawCircle(color = Color.White.copy(alpha = 0.8f), radius = 1.6.dp.toPx(), center = Offset(palX + palW * 0.25f, palY + palH * 0.65f))

                // 2. Fixed Yellow Main Color Spot & Paintbrush tip
                drawCircle(color = FixedYellow, radius = 3.2.dp.toPx(), center = Offset(palX + palW * 0.52f, palY + palH * 0.40f))
                drawCircle(color = FixedYellowDark, radius = 1.8.dp.toPx(), center = Offset(palX + palW * 0.52f, palY + palH * 0.40f))
            }

            SettingsIconType.VIBRATION -> {
                // 1. Smartphone body in themeColor
                val phW = w * 0.42f
                val phH = h * 0.74f
                val phX = (w - phW) / 2f
                val phY = (h - phH) / 2f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(phX, phY),
                    size = Size(phW, phH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    style = Stroke(1.8.dp.toPx())
                )
                // Screen fill subtle
                drawRoundRect(
                    color = themeColor.copy(alpha = 0.25f),
                    topLeft = Offset(phX + 2.dp.toPx(), phY + 4.dp.toPx()),
                    size = Size(phW - 4.dp.toPx(), phH - 8.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )

                // 2. Fixed Yellow Vibration wave brackets
                val wavePathL = Path().apply {
                    moveTo(phX - w * 0.16f, phY + phH * 0.25f)
                    quadraticTo(phX - w * 0.24f, phY + phH * 0.50f, phX - w * 0.16f, phY + phH * 0.75f)
                }
                val wavePathR = Path().apply {
                    moveTo(phX + phW + w * 0.16f, phY + phH * 0.25f)
                    quadraticTo(phX + phW + w * 0.24f, phY + phH * 0.50f, phX + phW + w * 0.16f, phY + phH * 0.75f)
                }
                drawPath(path = wavePathL, color = FixedYellow, style = Stroke(2.0.dp.toPx(), cap = StrokeCap.Round))
                drawPath(path = wavePathR, color = FixedYellow, style = Stroke(2.0.dp.toPx(), cap = StrokeCap.Round))
            }

            SettingsIconType.TEXT_SIZE -> {
                // 1. Large 'A' in themeColor
                val bigPath = Path().apply {
                    moveTo(w * 0.14f, h * 0.82f)
                    lineTo(w * 0.36f, h * 0.18f)
                    lineTo(w * 0.58f, h * 0.82f)
                }
                drawPath(path = bigPath, color = themeColor, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                drawLine(
                    color = themeColor,
                    start = Offset(w * 0.22f, h * 0.60f),
                    end = Offset(w * 0.50f, h * 0.60f),
                    strokeWidth = 2.0.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // 2. Small 'a' & cursor base in Fixed Yellow
                val smallPath = Path().apply {
                    moveTo(w * 0.66f, h * 0.52f)
                    quadraticTo(w * 0.88f, h * 0.44f, w * 0.88f, h * 0.82f)
                }
                drawPath(path = smallPath, color = FixedYellow, style = Stroke(2.0.dp.toPx(), cap = StrokeCap.Round))
                drawCircle(color = FixedYellow, radius = 2.8.dp.toPx(), center = Offset(w * 0.76f, h * 0.66f), style = Stroke(1.8.dp.toPx()))
                // Baseline indicator
                drawLine(
                    color = FixedYellow,
                    start = Offset(w * 0.62f, h * 0.88f),
                    end = Offset(w * 0.92f, h * 0.88f),
                    strokeWidth = 2.0.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            SettingsIconType.CURRENCY -> {
                // 1. Banknote in themeColor
                val noteW = w * 0.78f
                val noteH = h * 0.54f
                val noteX = (w - noteW) / 2f
                val noteY = (h - noteH) / 2f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(noteX, noteY),
                    size = Size(noteW, noteH),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )

                // 2. Fixed Yellow center embossed gold coin
                val coinC = Offset(w / 2f, h / 2f)
                val coinR = minOf(w, h) * 0.20f
                drawCircle(color = FixedYellow, radius = coinR, center = coinC)
                drawCircle(color = FixedYellowDark, radius = coinR * 0.70f, center = coinC, style = Stroke(1.2.dp.toPx()))
                drawCircle(color = Color.White.copy(alpha = 0.9f), radius = 1.2.dp.toPx(), center = coinC)
            }

            SettingsIconType.EXCHANGE_RATE -> {
                // 1. Top arrow curving right (themeColor)
                val topPath = Path().apply {
                    moveTo(w * 0.20f, h * 0.38f)
                    lineTo(w * 0.72f, h * 0.38f)
                    lineTo(w * 0.58f, h * 0.22f)
                }
                drawPath(path = topPath, color = themeColor, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

                // 2. Bottom arrow curving left (Fixed Yellow)
                val botPath = Path().apply {
                    moveTo(w * 0.80f, h * 0.64f)
                    lineTo(w * 0.28f, h * 0.64f)
                    lineTo(w * 0.42f, h * 0.80f)
                }
                drawPath(path = botPath, color = FixedYellow, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }

            SettingsIconType.NUMBER_FORMAT -> {
                // 1. Hash matrix grid in themeColor
                val strokeW = 1.8.dp.toPx()
                drawLine(color = themeColor, start = Offset(w * 0.38f, h * 0.16f), end = Offset(w * 0.32f, h * 0.84f), strokeWidth = strokeW, cap = StrokeCap.Round)
                drawLine(color = themeColor, start = Offset(w * 0.68f, h * 0.16f), end = Offset(w * 0.62f, h * 0.84f), strokeWidth = strokeW, cap = StrokeCap.Round)
                drawLine(color = themeColor, start = Offset(w * 0.18f, h * 0.38f), end = Offset(w * 0.82f, h * 0.38f), strokeWidth = strokeW, cap = StrokeCap.Round)
                drawLine(color = themeColor, start = Offset(w * 0.18f, h * 0.64f), end = Offset(w * 0.82f, h * 0.64f), strokeWidth = strokeW, cap = StrokeCap.Round)

                // 2. Fixed Yellow golden dots/digits
                drawCircle(color = FixedYellow, radius = 2.4.dp.toPx(), center = Offset(w * 0.50f, h * 0.51f))
                drawCircle(color = FixedYellow, radius = 1.8.dp.toPx(), center = Offset(w * 0.82f, h * 0.78f))
            }

            SettingsIconType.DATE_FORMAT -> {
                // 1. Calendar Body in themeColor
                val calW = w * 0.70f
                val calH = h * 0.66f
                val calX = (w - calW) / 2f
                val calY = h * 0.24f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(calX, calY),
                    size = Size(calW, calH),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                // Grid ticks
                val dotC = Color.White.copy(alpha = 0.8f)
                drawCircle(color = dotC, radius = 1.2.dp.toPx(), center = Offset(calX + calW * 0.30f, calY + calH * 0.50f))
                drawCircle(color = dotC, radius = 1.2.dp.toPx(), center = Offset(calX + calW * 0.70f, calY + calH * 0.50f))
                drawCircle(color = dotC, radius = 1.2.dp.toPx(), center = Offset(calX + calW * 0.30f, calY + calH * 0.75f))

                // 2. Fixed Yellow top binder bar and rings
                drawRoundRect(
                    color = FixedYellow,
                    topLeft = Offset(calX, calY - 3.dp.toPx()),
                    size = Size(calW, 5.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
                drawCircle(color = FixedYellow, radius = 2.0.dp.toPx(), center = Offset(calX + calW * 0.70f, calY + calH * 0.75f))
            }

            SettingsIconType.START_DAY_WEEK -> {
                // 1. 3 week column bars in themeColor
                val barW = w * 0.18f
                val barH = h * 0.60f
                val gap = w * 0.08f
                val startX = (w - (barW * 3 + gap * 2)) / 2f
                val startY = h * 0.22f

                // Normal days
                drawRoundRect(
                    color = themeColor.copy(alpha = 0.45f),
                    topLeft = Offset(startX + barW + gap, startY),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                drawRoundRect(
                    color = themeColor.copy(alpha = 0.45f),
                    topLeft = Offset(startX + (barW + gap) * 2, startY),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )

                // 2. Fixed Yellow First Day Column highlighted
                drawRoundRect(
                    color = FixedYellow,
                    topLeft = Offset(startX, startY),
                    size = Size(barW, barH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                drawCircle(color = FixedYellowDark, radius = 2.0.dp.toPx(), center = Offset(startX + barW / 2f, startY + barH * 0.35f))
            }

            SettingsIconType.START_DAY_MONTH -> {
                // 1. Calendar frame in themeColor
                val calW = w * 0.68f
                val calH = h * 0.68f
                val calX = (w - calW) / 2f
                val calY = (h - calH) / 2f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(calX, calY),
                    size = Size(calW, calH),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )

                // 2. Fixed Yellow Payday Golden Coin in bottom right
                val coinC = Offset(calX + calW * 0.75f, calY + calH * 0.75f)
                val coinR = w * 0.18f
                drawCircle(color = FixedYellow, radius = coinR, center = coinC)
                drawCircle(color = FixedYellowDark, radius = coinR * 0.75f, center = coinC, style = Stroke(1.2.dp.toPx()))
                // Small coin dollar sign or dot
                drawCircle(color = Color.White, radius = 1.2.dp.toPx(), center = coinC)
            }

            SettingsIconType.SAVINGS_TARGET -> {
                // 1. Concentric target circles in themeColor
                val center = Offset(w / 2f, h / 2f)
                val outerR = minOf(w, h) * 0.40f
                drawCircle(color = themeColor.copy(alpha = 0.30f), radius = outerR, center = center)
                drawCircle(color = themeColor, radius = outerR, center = center, style = Stroke(1.8.dp.toPx()))
                drawCircle(color = themeColor, radius = outerR * 0.62f, center = center, style = Stroke(1.5.dp.toPx()))

                // 2. Fixed Yellow Bullseye Center & Dart Tip
                val innerR = outerR * 0.28f
                drawCircle(color = FixedYellow, radius = innerR, center = center)
                drawCircle(color = FixedYellowLight, radius = innerR * 0.45f, center = center)

                // Mini dart needle
                drawLine(
                    color = FixedYellow,
                    start = Offset(center.x + outerR * 0.2f, center.y - outerR * 0.2f),
                    end = Offset(center.x + outerR * 0.9f, center.y - outerR * 0.9f),
                    strokeWidth = 2.0.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            SettingsIconType.CASH_ACCOUNTS_ONLY -> {
                // 1. Wallet in themeColor
                val wallW = w * 0.72f
                val wallH = h * 0.52f
                val wallX = (w - wallW) / 2f
                val wallY = h * 0.36f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(wallX, wallY),
                    size = Size(wallW, wallH),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )

                // 2. Fixed Yellow Cash bill peeking out of wallet
                val billW = wallW * 0.65f
                val billH = wallH * 0.45f
                val billX = wallX + (wallW - billW) / 2f
                val billY = wallY - billH * 0.45f
                drawRoundRect(
                    color = FixedYellow,
                    topLeft = Offset(billX, billY),
                    size = Size(billW, billH),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
                drawCircle(color = FixedYellowDark, radius = 1.4.dp.toPx(), center = Offset(billX + billW / 2f, billY + billH / 2f))
            }

            SettingsIconType.SHOW_PLANNED -> {
                // 1. Realized Trend line in themeColor
                val p1 = Offset(w * 0.16f, h * 0.74f)
                val p2 = Offset(w * 0.44f, h * 0.50f)
                val p3 = Offset(w * 0.60f, h * 0.58f)
                val p4 = Offset(w * 0.86f, h * 0.22f)

                val linePath = Path().apply {
                    moveTo(p1.x, p1.y)
                    lineTo(p2.x, p2.y)
                    lineTo(p3.x, p3.y)
                }
                drawPath(path = linePath, color = themeColor, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round))
                drawCircle(color = themeColor, radius = 2.0.dp.toPx(), center = p1)
                drawCircle(color = themeColor, radius = 2.0.dp.toPx(), center = p2)
                drawCircle(color = themeColor, radius = 2.0.dp.toPx(), center = p3)

                // 2. Fixed Yellow Projected Trend line (dotted/dashed)
                val planPath = Path().apply {
                    moveTo(p3.x, p3.y)
                    lineTo(p4.x, p4.y)
                }
                drawPath(
                    path = planPath,
                    color = FixedYellow,
                    style = Stroke(
                        width = 2.2.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    )
                )
                drawCircle(color = FixedYellow, radius = 3.6.dp.toPx(), center = p4)
                drawCircle(color = Color.White, radius = 1.4.dp.toPx(), center = p4)
            }

            SettingsIconType.DAILY_REMINDER -> {
                // 1. Clock body in themeColor
                val r = minOf(w, h) * 0.32f
                val center = Offset(w / 2f, h * 0.56f)
                drawCircle(color = themeColor, radius = r, center = center)
                // Clock hands
                drawLine(color = Color.White, start = center, end = Offset(center.x, center.y - r * 0.6f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color = Color.White, start = center, end = Offset(center.x + r * 0.5f, center.y), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)

                // 2. Fixed Yellow Twin Alarm Bells on top
                val bell1 = Offset(center.x - r * 0.72f, center.y - r * 0.72f)
                val bell2 = Offset(center.x + r * 0.72f, center.y - r * 0.72f)
                drawCircle(color = FixedYellow, radius = 3.2.dp.toPx(), center = bell1)
                drawCircle(color = FixedYellow, radius = 3.2.dp.toPx(), center = bell2)
            }

            SettingsIconType.REMINDER_TIME -> {
                // 1. Clock circle in themeColor
                val r = minOf(w, h) * 0.38f
                val center = Offset(w / 2f, h / 2f)
                drawCircle(color = themeColor.copy(alpha = 0.22f), radius = r, center = center)
                drawCircle(color = themeColor, radius = r, center = center, style = Stroke(2.0.dp.toPx()))

                // 2. Fixed Yellow Clock hands & center pin
                val hHand = Offset(center.x, center.y - r * 0.55f)
                val mHand = Offset(center.x + r * 0.45f, center.y + r * 0.15f)
                drawLine(color = FixedYellow, start = center, end = hHand, strokeWidth = 2.0.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color = FixedYellow, start = center, end = mHand, strokeWidth = 2.0.dp.toPx(), cap = StrokeCap.Round)
                drawCircle(color = FixedYellowDark, radius = 2.4.dp.toPx(), center = center)
            }

            SettingsIconType.BUDGET_ALERT, SettingsIconType.BUDGET_THRESHOLD -> {
                // 1. Warning Triangle in themeColor
                val topP = Offset(w / 2f, h * 0.15f)
                val leftP = Offset(w * 0.15f, h * 0.82f)
                val rightP = Offset(w * 0.85f, h * 0.82f)

                val triPath = Path().apply {
                    moveTo(topP.x, topP.y)
                    lineTo(rightP.x, rightP.y)
                    lineTo(leftP.x, leftP.y)
                    close()
                }
                drawPath(path = triPath, color = themeColor.copy(alpha = 0.25f))
                drawPath(path = triPath, color = themeColor, style = Stroke(2.0.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

                // 2. Fixed Yellow Exclamation mark
                val midX = w / 2f
                drawLine(
                    color = FixedYellow,
                    start = Offset(midX, h * 0.36f),
                    end = Offset(midX, h * 0.58f),
                    strokeWidth = 2.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawCircle(color = FixedYellow, radius = 1.6.dp.toPx(), center = Offset(midX, h * 0.70f))
            }

            SettingsIconType.BILL_REMINDER -> {
                // 1. Bell body in themeColor
                val bellPath = Path().apply {
                    val cx = w / 2f
                    moveTo(cx, h * 0.18f)
                    cubicTo(cx - w * 0.28f, h * 0.32f, cx - w * 0.34f, h * 0.65f, cx - w * 0.40f, h * 0.72f)
                    lineTo(cx + w * 0.40f, h * 0.72f)
                    cubicTo(cx + w * 0.34f, h * 0.65f, cx + w * 0.28f, h * 0.32f, cx, h * 0.18f)
                    close()
                }
                drawPath(path = bellPath, color = themeColor)

                // 2. Fixed Yellow clapper & suspension loop
                drawCircle(color = FixedYellow, radius = 2.2.dp.toPx(), center = Offset(w / 2f, h * 0.14f))
                drawCircle(color = FixedYellow, radius = 2.8.dp.toPx(), center = Offset(w / 2f, h * 0.80f))
            }

            SettingsIconType.SAVE_REMINDER -> {
                // 1. Piggy bank body in themeColor
                drawOval(
                    color = themeColor,
                    topLeft = Offset(w * 0.18f, h * 0.34f),
                    size = Size(w * 0.66f, h * 0.48f)
                )
                // Snout & ear
                drawRoundRect(color = themeColor, topLeft = Offset(w * 0.12f, h * 0.46f), size = Size(w * 0.12f, h * 0.24f), cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()))

                // 2. Fixed Yellow Golden coin entering slot
                val coinC = Offset(w * 0.54f, h * 0.24f)
                drawCircle(color = FixedYellow, radius = w * 0.14f, center = coinC)
                drawCircle(color = FixedYellowDark, radius = w * 0.10f, center = coinC, style = Stroke(1.0.dp.toPx()))
            }

            SettingsIconType.SMART_INSIGHTS -> {
                // 1. Large 4-pointed sparkle in themeColor
                val c1 = Offset(w * 0.40f, h * 0.54f)
                val r1 = w * 0.34f
                val p1 = Path().apply {
                    moveTo(c1.x, c1.y - r1)
                    quadraticTo(c1.x, c1.y, c1.x + r1, c1.y)
                    quadraticTo(c1.x, c1.y, c1.x, c1.y + r1)
                    quadraticTo(c1.x, c1.y, c1.x - r1, c1.y)
                    quadraticTo(c1.x, c1.y, c1.x, c1.y - r1)
                    close()
                }
                drawPath(path = p1, color = themeColor)

                // 2. Fixed Yellow Companion golden stars
                val c2 = Offset(w * 0.78f, h * 0.26f)
                val r2 = w * 0.18f
                val p2 = Path().apply {
                    moveTo(c2.x, c2.y - r2)
                    quadraticTo(c2.x, c2.y, c2.x + r2, c2.y)
                    quadraticTo(c2.x, c2.y, c2.x, c2.y + r2)
                    quadraticTo(c2.x, c2.y, c2.x - r2, c2.y)
                    quadraticTo(c2.x, c2.y, c2.x, c2.y - r2)
                    close()
                }
                drawPath(path = p2, color = FixedYellow)
                drawCircle(color = FixedYellowLight, radius = 1.2.dp.toPx(), center = c2)
            }

            SettingsIconType.CATEGORIES -> {
                // 1. Category geometric elements in themeColor
                val sz = w * 0.32f
                // Top-Left Circle
                drawCircle(color = themeColor, radius = sz / 2f, center = Offset(w * 0.28f, h * 0.28f))
                // Bottom-Left Square
                drawRoundRect(color = themeColor, topLeft = Offset(w * 0.12f, h * 0.54f), size = Size(sz, sz), cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()))
                // Bottom-Right Triangle
                val tri = Path().apply {
                    moveTo(w * 0.72f, h * 0.54f)
                    lineTo(w * 0.88f, h * 0.86f)
                    lineTo(w * 0.56f, h * 0.86f)
                    close()
                }
                drawPath(path = tri, color = themeColor)

                // 2. Fixed Yellow Diamond Category Highlight
                val dia = Path().apply {
                    val cx = w * 0.72f
                    val cy = h * 0.28f
                    val dR = sz * 0.55f
                    moveTo(cx, cy - dR)
                    lineTo(cx + dR, cy)
                    lineTo(cx, cy + dR)
                    lineTo(cx - dR, cy)
                    close()
                }
                drawPath(path = dia, color = FixedYellow)
            }

            SettingsIconType.LABELS -> {
                // 1. Tag body in themeColor
                val tagPath = Path().apply {
                    moveTo(w * 0.20f, h * 0.48f)
                    lineTo(w * 0.44f, h * 0.18f)
                    lineTo(w * 0.84f, h * 0.58f)
                    lineTo(w * 0.60f, h * 0.88f)
                    close()
                }
                drawPath(path = tagPath, color = themeColor)

                // 2. Fixed Yellow Tag eyelet & ribbon thread
                val eyeC = Offset(w * 0.40f, h * 0.32f)
                drawCircle(color = FixedYellow, radius = 2.4.dp.toPx(), center = eyeC)
                drawLine(
                    color = FixedYellow,
                    start = eyeC,
                    end = Offset(w * 0.18f, h * 0.16f),
                    strokeWidth = 1.8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            SettingsIconType.RECURRING -> {
                // 1. Circular loop arrow in themeColor
                val r = minOf(w, h) * 0.36f
                val center = Offset(w / 2f, h / 2f)
                drawArc(
                    color = themeColor,
                    startAngle = 45f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = Offset(center.x - r, center.y - r),
                    size = Size(r * 2f, r * 2f),
                    style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round)
                )

                // 2. Fixed Yellow arrowhead & center cycle dot
                val arrowP = Path().apply {
                    val ax = center.x + r
                    val ay = center.y
                    moveTo(ax - 3.dp.toPx(), ay - 4.dp.toPx())
                    lineTo(ax + 3.dp.toPx(), ay)
                    lineTo(ax - 3.dp.toPx(), ay + 4.dp.toPx())
                }
                drawPath(path = arrowP, color = FixedYellow, style = Stroke(2.0.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
                drawCircle(color = FixedYellow, radius = 2.6.dp.toPx(), center = center)
            }

            SettingsIconType.NAV_ORDER -> {
                // 1. 3 horizontal list bars in themeColor
                val barW = w * 0.60f
                val barH = h * 0.14f
                val barX = w * 0.14f
                val r = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                drawRoundRect(color = themeColor, topLeft = Offset(barX, h * 0.22f), size = Size(barW, barH), cornerRadius = r)
                drawRoundRect(color = themeColor, topLeft = Offset(barX, h * 0.44f), size = Size(barW, barH), cornerRadius = r)
                drawRoundRect(color = themeColor, topLeft = Offset(barX, h * 0.66f), size = Size(barW, barH), cornerRadius = r)

                // 2. Fixed Yellow Reorder handle (Up/Down arrow dots)
                val dotX = w * 0.84f
                drawCircle(color = FixedYellow, radius = 2.2.dp.toPx(), center = Offset(dotX, h * 0.29f))
                drawCircle(color = FixedYellow, radius = 2.2.dp.toPx(), center = Offset(dotX, h * 0.51f))
                drawCircle(color = FixedYellow, radius = 2.2.dp.toPx(), center = Offset(dotX, h * 0.73f))
            }

            SettingsIconType.EXPORT_PDF -> {
                // 1. Document in themeColor with folded corner
                val docW = w * 0.64f
                val docH = h * 0.76f
                val docX = (w - docW) / 2f
                val docY = (h - docH) / 2f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(docX, docY),
                    size = Size(docW, docH),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                // Text lines
                val lineC = Color.White.copy(alpha = 0.8f)
                drawLine(color = lineC, start = Offset(docX + docW * 0.2f, docY + docH * 0.55f), end = Offset(docX + docW * 0.8f, docY + docH * 0.55f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color = lineC, start = Offset(docX + docW * 0.2f, docY + docH * 0.75f), end = Offset(docX + docW * 0.6f, docY + docH * 0.75f), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)

                // 2. Fixed Yellow "PDF" Top Banner / Ribbon
                drawRoundRect(
                    color = FixedYellow,
                    topLeft = Offset(docX - 2.dp.toPx(), docY + docH * 0.16f),
                    size = Size(docW * 0.72f, docH * 0.26f),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
                drawCircle(color = FixedYellowDark, radius = 1.2.dp.toPx(), center = Offset(docX + docW * 0.25f, docY + docH * 0.29f))
            }

            SettingsIconType.EXPORT_CSV -> {
                // 1. Spreadsheet grid in themeColor
                val tbW = w * 0.74f
                val tbH = h * 0.66f
                val tbX = (w - tbW) / 2f
                val tbY = (h - tbH) / 2f
                drawRoundRect(
                    color = themeColor.copy(alpha = 0.20f),
                    topLeft = Offset(tbX, tbY),
                    size = Size(tbW, tbH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(tbX, tbY),
                    size = Size(tbW, tbH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    style = Stroke(1.8.dp.toPx())
                )
                // Grid lines
                drawLine(color = themeColor, start = Offset(tbX, tbY + tbH * 0.40f), end = Offset(tbX + tbW, tbY + tbH * 0.40f), strokeWidth = 1.2.dp.toPx())
                drawLine(color = themeColor, start = Offset(tbX, tbY + tbH * 0.70f), end = Offset(tbX + tbW, tbY + tbH * 0.70f), strokeWidth = 1.2.dp.toPx())
                drawLine(color = themeColor, start = Offset(tbX + tbW * 0.45f, tbY), end = Offset(tbX + tbW * 0.45f, tbY + tbH), strokeWidth = 1.2.dp.toPx())

                // 2. Fixed Yellow Active Sheet / Cell Highlight
                drawRoundRect(
                    color = FixedYellow,
                    topLeft = Offset(tbX + tbW * 0.47f, tbY + 1.dp.toPx()),
                    size = Size(tbW * 0.50f, tbH * 0.38f),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
            }

            SettingsIconType.APP_PASSCODE -> {
                // 1. Padlock body in themeColor
                val bodyW = w * 0.58f
                val bodyH = h * 0.46f
                val bodyX = (w - bodyW) / 2f
                val bodyY = h * 0.44f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(bodyX, bodyY),
                    size = Size(bodyW, bodyH),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )

                // 2. Fixed Yellow Shackle & Keyhole
                val shacklePath = Path().apply {
                    val sW = bodyW * 0.60f
                    val sL = (w - sW) / 2f
                    val sR = sL + sW
                    val sTop = h * 0.16f
                    moveTo(sL, bodyY + 2.dp.toPx())
                    lineTo(sL, sTop + sW / 2f)
                    arcTo(
                        rect = androidx.compose.ui.geometry.Rect(sL, sTop, sR, sTop + sW),
                        startAngleDegrees = 180f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = false
                    )
                    lineTo(sR, bodyY + 2.dp.toPx())
                }
                drawPath(path = shacklePath, color = FixedYellow, style = Stroke(2.4.dp.toPx(), cap = StrokeCap.Round))

                // Keyhole in Fixed Yellow Light
                drawCircle(color = FixedYellowLight, radius = 2.0.dp.toPx(), center = Offset(w / 2f, bodyY + bodyH * 0.40f))
                drawLine(color = FixedYellowLight, start = Offset(w / 2f, bodyY + bodyH * 0.40f), end = Offset(w / 2f, bodyY + bodyH * 0.70f), strokeWidth = 1.6.dp.toPx(), cap = StrokeCap.Round)
            }

            SettingsIconType.HIDE_BALANCE -> {
                // 1. Eye contour in themeColor
                val eyePath = Path().apply {
                    val cx = w / 2f
                    val cy = h / 2f
                    val eW = w * 0.40f
                    val eH = h * 0.28f
                    moveTo(cx - eW, cy)
                    quadraticTo(cx, cy - eH, cx + eW, cy)
                    quadraticTo(cx, cy + eH, cx - eW, cy)
                    close()
                }
                drawPath(path = eyePath, color = themeColor.copy(alpha = 0.25f))
                drawPath(path = eyePath, color = themeColor, style = Stroke(2.0.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))

                // 2. Fixed Yellow Diagonal Privacy Slash & Center Pupil
                drawCircle(color = FixedYellow, radius = 3.2.dp.toPx(), center = Offset(w / 2f, h / 2f))
                drawLine(
                    color = FixedYellow,
                    start = Offset(w * 0.20f, h * 0.82f),
                    end = Offset(w * 0.80f, h * 0.18f),
                    strokeWidth = 2.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            SettingsIconType.CONFIRM_DELETE -> {
                // 1. Trash can body with ribs in themeColor
                val canW = w * 0.54f
                val canH = h * 0.54f
                val canX = (w - canW) / 2f
                val canY = h * 0.36f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(canX, canY),
                    size = Size(canW, canH),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                val ribC = Color.White.copy(alpha = 0.7f)
                drawLine(color = ribC, start = Offset(canX + canW * 0.32f, canY + 3.dp.toPx()), end = Offset(canX + canW * 0.32f, canY + canH - 3.dp.toPx()), strokeWidth = 1.2.dp.toPx())
                drawLine(color = ribC, start = Offset(canX + canW * 0.68f, canY + 3.dp.toPx()), end = Offset(canX + canW * 0.68f, canY + canH - 3.dp.toPx()), strokeWidth = 1.2.dp.toPx())

                // 2. Fixed Yellow Open Lid & Handle
                val lidW = canW * 1.24f
                val lidX = (w - lidW) / 2f
                val lidY = canY - 4.dp.toPx()
                drawLine(color = FixedYellow, start = Offset(lidX, lidY), end = Offset(lidX + lidW, lidY), strokeWidth = 2.2.dp.toPx(), cap = StrokeCap.Round)
                drawRoundRect(
                    color = FixedYellow,
                    topLeft = Offset(w / 2f - 3.dp.toPx(), lidY - 4.dp.toPx()),
                    size = Size(6.dp.toPx(), 4.dp.toPx()),
                    cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                )
            }

            SettingsIconType.BACKUP_RESTORE -> {
                // 1. Cloud silhouette in themeColor
                val cloudPath = Path().apply {
                    val cx = w / 2f
                    val cy = h * 0.48f
                    val r = w * 0.22f
                    moveTo(cx - r * 1.3f, cy + r * 0.5f)
                    quadraticTo(cx - r * 1.5f, cy - r * 0.5f, cx - r * 0.5f, cy - r * 0.8f)
                    quadraticTo(cx, cy - r * 1.4f, cx + r * 0.7f, cy - r * 0.6f)
                    quadraticTo(cx + r * 1.5f, cy - r * 0.3f, cx + r * 1.3f, cy + r * 0.5f)
                    close()
                }
                drawPath(path = cloudPath, color = themeColor)

                // 2. Fixed Yellow Upward Sync Arrow
                val arrowX = w / 2f
                val arrowTop = h * 0.24f
                val arrowBot = h * 0.78f
                drawLine(color = FixedYellow, start = Offset(arrowX, arrowBot), end = Offset(arrowX, arrowTop), strokeWidth = 2.4.dp.toPx(), cap = StrokeCap.Round)
                val head = Path().apply {
                    moveTo(arrowX - 4.dp.toPx(), arrowTop + 4.dp.toPx())
                    lineTo(arrowX, arrowTop)
                    lineTo(arrowX + 4.dp.toPx(), arrowTop + 4.dp.toPx())
                }
                drawPath(path = head, color = FixedYellow, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }

            SettingsIconType.ABOUT_APP -> {
                // 1. Badge circle in themeColor
                val r = minOf(w, h) * 0.40f
                val center = Offset(w / 2f, h / 2f)
                drawCircle(color = themeColor, radius = r, center = center)

                // 2. Fixed Yellow 'i' information letter
                drawCircle(color = FixedYellow, radius = 2.0.dp.toPx(), center = Offset(center.x, center.y - r * 0.45f))
                drawLine(
                    color = FixedYellow,
                    start = Offset(center.x, center.y - r * 0.15f),
                    end = Offset(center.x, center.y + r * 0.50f),
                    strokeWidth = 2.4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            SettingsIconType.PRIVACY_POLICY -> {
                // 1. Shield in themeColor
                val shieldPath = Path().apply {
                    val cx = w / 2f
                    moveTo(cx, h * 0.16f)
                    lineTo(w * 0.84f, h * 0.28f)
                    quadraticTo(w * 0.84f, h * 0.64f, cx, h * 0.86f)
                    quadraticTo(w * 0.16f, h * 0.64f, w * 0.16f, h * 0.28f)
                    close()
                }
                drawPath(path = shieldPath, color = themeColor)

                // 2. Fixed Yellow Checkmark
                val check = Path().apply {
                    moveTo(w * 0.35f, h * 0.50f)
                    lineTo(w * 0.46f, h * 0.62f)
                    lineTo(w * 0.66f, h * 0.38f)
                }
                drawPath(path = check, color = FixedYellow, style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            }

            SettingsIconType.FEEDBACK -> {
                // 1. Speech bubble in themeColor
                val bubW = w * 0.72f
                val bubH = h * 0.56f
                val bubX = (w - bubW) / 2f
                val bubY = h * 0.18f
                drawRoundRect(
                    color = themeColor,
                    topLeft = Offset(bubX, bubY),
                    size = Size(bubW, bubH),
                    cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                )
                // Speech pointer
                val ptr = Path().apply {
                    moveTo(bubX + 4.dp.toPx(), bubY + bubH)
                    lineTo(bubX + 1.dp.toPx(), bubY + bubH + 5.dp.toPx())
                    lineTo(bubX + 12.dp.toPx(), bubY + bubH)
                    close()
                }
                drawPath(path = ptr, color = themeColor)

                // 2. Fixed Yellow Star / Message dots
                drawCircle(color = FixedYellow, radius = 2.0.dp.toPx(), center = Offset(bubX + bubW * 0.30f, bubY + bubH * 0.50f))
                drawCircle(color = FixedYellow, radius = 2.0.dp.toPx(), center = Offset(bubX + bubW * 0.50f, bubY + bubH * 0.50f))
                drawCircle(color = FixedYellow, radius = 2.0.dp.toPx(), center = Offset(bubX + bubW * 0.70f, bubY + bubH * 0.50f))
            }

            SettingsIconType.SHARE -> {
                // 1. 2 base nodes and branches in themeColor
                val n1 = Offset(w * 0.32f, h * 0.50f)
                val n2 = Offset(w * 0.72f, h * 0.76f)
                val nTop = Offset(w * 0.72f, h * 0.24f)

                drawLine(color = themeColor, start = n1, end = nTop, strokeWidth = 1.8.dp.toPx(), cap = StrokeCap.Round)
                drawLine(color = themeColor, start = n1, end = n2, strokeWidth = 1.8.dp.toPx(), cap = StrokeCap.Round)

                drawCircle(color = themeColor, radius = 3.6.dp.toPx(), center = n1)
                drawCircle(color = themeColor, radius = 3.6.dp.toPx(), center = n2)

                // 2. Fixed Yellow Apex sharing node
                drawCircle(color = FixedYellow, radius = 4.4.dp.toPx(), center = nTop)
                drawCircle(color = FixedYellowDark, radius = 2.0.dp.toPx(), center = nTop)
            }

            SettingsIconType.RESET_DATA -> {
                // 1. Reset circular arrow in themeColor (e.g. ExpenseRed)
                val r = minOf(w, h) * 0.36f
                val center = Offset(w / 2f, h / 2f)
                drawArc(
                    color = themeColor,
                    startAngle = 60f,
                    sweepAngle = 260f,
                    useCenter = false,
                    topLeft = Offset(center.x - r, center.y - r),
                    size = Size(r * 2f, r * 2f),
                    style = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round)
                )

                // 2. Fixed Yellow Center Alert Triangle & Exclamation
                val tri = Path().apply {
                    val tH = r * 0.65f
                    moveTo(center.x, center.y - tH)
                    lineTo(center.x + tH * 0.8f, center.y + tH * 0.6f)
                    lineTo(center.x - tH * 0.8f, center.y + tH * 0.6f)
                    close()
                }
                drawPath(path = tri, color = FixedYellow)
                drawCircle(color = Color(0xFF131822), radius = 1.2.dp.toPx(), center = Offset(center.x, center.y + r * 0.3f))
            }
        }
    }
}
