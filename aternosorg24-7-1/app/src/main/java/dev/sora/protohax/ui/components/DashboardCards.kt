package dev.sora.protohax.ui.components

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import dev.sora.protohax.R
import dev.sora.protohax.relay.AccountManager
import dev.sora.protohax.relay.MinecraftRelay
import dev.sora.protohax.relay.service.AppService
import dev.sora.protohax.ui.activities.AppPickerActivity
import dev.sora.protohax.ui.navigation.PHaxTopLevelDestination
import dev.sora.protohax.ui.navigation.TOP_LEVEL_DESTINATIONS
import dev.sora.protohax.util.ContextUtils.getApplicationName
import dev.sora.protohax.util.ContextUtils.getPackageInfo
import dev.sora.relay.game.GameSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardLoginAlert(
    navigateToTopLevelDestination: (PHaxTopLevelDestination) -> Unit
) {
    if (AccountManager.currentAccount == null) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp, 10.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            onClick = {
                navigateToTopLevelDestination(TOP_LEVEL_DESTINATIONS.find { it.iconTextId == R.string.tab_accounts } ?: return@ElevatedCard)
            }
        ) {
            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.size(12.dp))
                Text(
                    stringResource(R.string.dashboard_no_account_selected),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardCurrentApplication(
    applicationSelected: MutableState<String>,
    pickAppActivityLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val ctx = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp, 10.dp),
        onClick = {
            pickAppActivityLauncher.launch(Intent(ctx, AppPickerActivity::class.java))
        }
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    stringResource(if (applicationSelected.value.isEmpty()) R.string.dashboard_select_application else R.string.dashboard_selected_application),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.size(0.dp, 8.dp))
            if (applicationSelected.value.isEmpty()) {
                Text(
                    stringResource(R.string.dashboard_no_application),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val lineHeight = 14.sp
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberDrawablePainter(ctx.packageManager.getApplicationIcon(applicationSelected.value)),
                        contentDescription = applicationSelected.value,
                        modifier = Modifier
                            .size(with(LocalDensity.current) { lineHeight.toDp() } + 6.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.size(6.dp, 0.dp))
                    Text(
                        "${ctx.packageManager.getApplicationName(applicationSelected.value)} (${applicationSelected.value})",
                        fontSize = lineHeight,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.size(0.dp, 6.dp))
                Text(
                    stringResource(R.string.dashboard_current_version, ctx.packageManager.getPackageInfo(applicationSelected.value).versionName),
                    fontSize = lineHeight,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    stringResource(R.string.dashboard_recommended_version, GameSession.RECOMMENDED_VERSION),
                    fontSize = lineHeight,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CardStatusIndicator(
    connectionState: State<Boolean>
) {
    val isConnected = connectionState.value
    val indicatorColor by animateColorAsState(
        targetValue = if (isConnected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
        animationSpec = tween(500)
    )
    val containerColor by animateColorAsState(
        targetValue = if (isConnected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer,
        animationSpec = tween(500)
    )
    val contentColor by animateColorAsState(
        targetValue = if (isConnected) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onErrorContainer,
        animationSpec = tween(500)
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp, 10.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isConnected) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = indicatorColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.size(12.dp))
            Column {
                Text(
                    stringResource(R.string.dashboard_connection_status),
                    fontSize = 12.sp,
                    color = contentColor.copy(alpha = 0.7f)
                )
                Text(
                    stringResource(if (isConnected) R.string.dashboard_status_connected else R.string.dashboard_status_disconnected),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun CardNetworkStats() {
    val packetCount = remember { mutableFloatStateOf(0f) }
    val latencyMs = remember { mutableStateOf(0L) }

    val isConnected = AppService.isActive

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp, 10.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    stringResource(R.string.dashboard_packets),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.size(0.dp, 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    icon = Icons.Default.NetworkCheck,
                    label = "Status",
                    value = if (isConnected) "Active" else "Idle"
                )
                StatItem(
                    icon = Icons.Default.Speed,
                    label = "Relay",
                    value = MinecraftRelay.session.netSession?.let { "Running" } ?: "Stopped"
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(0.dp, 4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
