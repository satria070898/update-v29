package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AccountWallet
import com.example.data.model.AssetItem
import com.example.data.model.DebtItem
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueBright
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.IncomeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.TextWhite
import com.example.util.AssetLogoBadge
import com.example.util.CategoryIconHelper
import com.example.util.CurrencyHelper
import com.example.util.IndonesianInstitutionHelper
import com.example.util.InstitutionLogoBadge

@Composable
fun ReorderAccountsDialog(
    initialWallets: List<AccountWallet>,
    initialAssets: List<AssetItem> = emptyList(),
    initialDebts: List<DebtItem> = emptyList(),
    initialTab: Int = 0, // 0 = Dompet, 1 = Aset, 2 = Utang
    onSaveWallets: (List<AccountWallet>) -> Unit,
    onSaveAssets: (List<AssetItem>) -> Unit = {},
    onSaveDebts: (List<DebtItem>) -> Unit = {},
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(initialTab.coerceIn(0, 2)) }

    var currentWallets by remember(initialWallets) { mutableStateOf(initialWallets) }
    var currentAssets by remember(initialAssets) { mutableStateOf(initialAssets) }
    var currentDebts by remember(initialDebts) { mutableStateOf(initialDebts) }

    fun <T> moveItem(list: List<T>, fromIndex: Int, toIndex: Int): List<T> {
        if (fromIndex !in list.indices || toIndex !in list.indices || fromIndex == toIndex) return list
        val mutable = list.toMutableList()
        val item = mutable.removeAt(fromIndex)
        mutable.add(toIndex, item)
        return mutable
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(20.dp),
            color = DarkSurface,
            border = BorderStroke(1.dp, DarkCardBorder.copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header with Title & Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(AccentBlue.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = null,
                                tint = AccentBlueBright,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Atur & Geser Urutan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Text(
                                text = "Geser posisi sesuai keinginan Anda",
                                fontSize = 11.sp,
                                color = TextSubtle
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Segmented Tabs: Dompet, Aset, Utang
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceElevated)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = listOf(
                        Pair(0, "Dompet (${currentWallets.size})"),
                        Pair(1, "Aset (${currentAssets.size})"),
                        Pair(2, "Utang (${currentDebts.size})")
                    )
                    tabs.forEach { (index, label) ->
                        val isSelected = selectedTab == index
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) AccentBlue else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = index }
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextMuted,
                                modifier = Modifier.padding(vertical = 7.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick presets row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reverse
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkSurfaceElevated,
                        border = BorderStroke(0.5.dp, DarkCardBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                when (selectedTab) {
                                    0 -> currentWallets = currentWallets.reversed()
                                    1 -> currentAssets = currentAssets.reversed()
                                    2 -> currentDebts = currentDebts.reversed()
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.SwapVert, null, tint = AccentBlueBright, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Balik", fontSize = 10.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Sort by Balance
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkSurfaceElevated,
                        border = BorderStroke(0.5.dp, DarkCardBorder),
                        modifier = Modifier
                            .weight(1.3f)
                            .clickable {
                                when (selectedTab) {
                                    0 -> currentWallets = currentWallets.sortedByDescending { it.balance }
                                    1 -> currentAssets = currentAssets.sortedByDescending { it.realValue }
                                    2 -> currentDebts = currentDebts.sortedByDescending { it.totalAmount }
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Sort, null, tint = AccentBlueBright, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Saldo Tertinggi", fontSize = 10.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Sort Name A-Z
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkSurfaceElevated,
                        border = BorderStroke(0.5.dp, DarkCardBorder),
                        modifier = Modifier
                            .weight(1.1f)
                            .clickable {
                                when (selectedTab) {
                                    0 -> currentWallets = currentWallets.sortedBy { it.name }
                                    1 -> currentAssets = currentAssets.sortedBy { it.name }
                                    2 -> currentDebts = currentDebts.sortedBy { it.name }
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("A - Z", fontSize = 10.sp, color = AccentBlueBright, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Nama", fontSize = 10.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    // Reset
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkSurfaceElevated,
                        border = BorderStroke(0.5.dp, DarkCardBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                when (selectedTab) {
                                    0 -> currentWallets = initialWallets
                                    1 -> currentAssets = initialAssets
                                    2 -> currentDebts = initialDebts
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Refresh, null, tint = TextMuted, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Reset", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable List of Items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    when (selectedTab) {
                        0 -> {
                            if (currentWallets.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Belum ada dompet", fontSize = 12.sp, color = TextSubtle)
                                    }
                                }
                            } else {
                                itemsIndexed(currentWallets, key = { _, w -> w.id }) { index, wallet ->
                                    val inst = IndonesianInstitutionHelper.resolveInstitution(wallet.name, wallet.iconName)
                                    ReorderItemCard(
                                        index = index,
                                        isFirst = index == 0,
                                        isLast = index == currentWallets.lastIndex,
                                        title = wallet.name,
                                        subtitle = wallet.bankType.split("·").firstOrNull()?.trim() ?: wallet.bankType,
                                        amount = CurrencyHelper.formatRupiah(wallet.balance),
                                        leadingLogo = {
                                            InstitutionLogoBadge(
                                                walletName = wallet.name,
                                                iconName = wallet.iconName,
                                                size = 32.dp
                                            )
                                        },
                                        onMoveUp = { currentWallets = moveItem(currentWallets, index, index - 1) },
                                        onMoveDown = { currentWallets = moveItem(currentWallets, index, index + 1) }
                                    )
                                }
                            }
                        }
                        1 -> {
                            if (currentAssets.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Belum ada aset", fontSize = 12.sp, color = TextSubtle)
                                    }
                                }
                            } else {
                                itemsIndexed(currentAssets, key = { _, a -> a.id }) { index, asset ->
                                    ReorderItemCard(
                                        index = index,
                                        isFirst = index == 0,
                                        isLast = index == currentAssets.lastIndex,
                                        title = asset.name,
                                        subtitle = asset.platform.ifBlank { asset.assetType },
                                        amount = CurrencyHelper.formatRupiah(asset.realValue),
                                        leadingLogo = {
                                            AssetLogoBadge(
                                                assetName = asset.name,
                                                assetType = asset.assetType,
                                                platform = asset.platform,
                                                size = 32.dp
                                            )
                                        },
                                        onMoveUp = { currentAssets = moveItem(currentAssets, index, index - 1) },
                                        onMoveDown = { currentAssets = moveItem(currentAssets, index, index + 1) }
                                    )
                                }
                            }
                        }
                        2 -> {
                            if (currentDebts.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Belum ada utang atau piutang", fontSize = 12.sp, color = TextSubtle)
                                    }
                                }
                            } else {
                                itemsIndexed(currentDebts, key = { _, d -> d.id }) { index, debt ->
                                    val isLent = debt.type == com.example.data.model.DebtType.LENT
                                    ReorderItemCard(
                                        index = index,
                                        isFirst = index == 0,
                                        isLast = index == currentDebts.lastIndex,
                                        title = debt.name,
                                        subtitle = if (isLent) "Piutang (Dipinjamkan)" else "Hutang (Dipinjam)",
                                        amount = CurrencyHelper.formatRupiah(debt.remainingAmount),
                                        leadingLogo = {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isLent) IncomeGreen.copy(alpha = 0.2f) else ExpenseRed.copy(alpha = 0.2f),
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = CategoryIconHelper.getCategoryIcon(debt.categoryIcon),
                                                        contentDescription = null,
                                                        tint = if (isLent) IncomeGreen else ExpenseRed,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        },
                                        onMoveUp = { currentDebts = moveItem(currentDebts, index, index - 1) },
                                        onMoveDown = { currentDebts = moveItem(currentDebts, index, index + 1) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal", color = TextSubtle, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            onSaveWallets(currentWallets)
                            if (currentAssets.isNotEmpty()) onSaveAssets(currentAssets)
                            if (currentDebts.isNotEmpty()) onSaveDebts(currentDebts)
                            Toast.makeText(context, "Urutan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("btn_save_custom_order")
                    ) {
                        Text(
                            text = "Simpan Urutan",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReorderItemCard(
    index: Int,
    isFirst: Boolean,
    isLast: Boolean,
    title: String,
    subtitle: String,
    amount: String,
    leadingLogo: @Composable () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        border = BorderStroke(1.dp, DarkCardBorder.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Drag handle icon
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = null,
                    tint = TextSubtle,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Position index badge (e.g. 1, 2, 3)
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(AccentBlue.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentBlueBright
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Institution / Logo
                leadingLogo()

                Spacer(modifier = Modifier.width(8.dp))

                // Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite,
                        maxLines = 1
                    )
                    Text(
                        text = "$subtitle · $amount",
                        fontSize = 10.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }
            }

            // Up / Down action buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = onMoveUp,
                    enabled = !isFirst,
                    modifier = Modifier.size(32.dp).testTag("btn_move_up_$index")
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Pindah Ke Atas",
                        tint = if (!isFirst) AccentBlueBright else TextSubtle.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onMoveDown,
                    enabled = !isLast,
                    modifier = Modifier.size(32.dp).testTag("btn_move_down_$index")
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Pindah Ke Bawah",
                        tint = if (!isLast) AccentBlueBright else TextSubtle.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
