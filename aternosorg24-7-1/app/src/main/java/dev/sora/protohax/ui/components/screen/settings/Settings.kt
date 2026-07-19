package dev.sora.protohax.ui.components.screen.settings

import android.os.Build
import dev.sora.protohax.R
import dev.sora.protohax.relay.MinecraftRelay

object Settings {

    val offlineSessionEncryption = BoolSetting(R.string.setting_encryption, R.string.setting_encryption_desc, "OFFLINE_SESSION_ENCRYPTION", false)
    val enableCommandManager = BoolSetting(R.string.setting_commands, R.string.setting_commands_desc, "ENABLE_COMMAND_MANAGER", true, restartRequired = true)
    val enableRakReliability = BoolSetting(R.string.setting_rak_reliability, R.string.setting_rak_reliability_desc, "ENABLE_RAK_RELIABILITY", true) {
        MinecraftRelay.updateReliability()
    }
    val trustClicks = BoolSetting(R.string.setting_trust_click,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) R.string.setting_trust_click_desc else R.string.setting_trust_click_disabled,
        "TRUST_CLICK", false, override = Build.VERSION.SDK_INT < Build.VERSION_CODES.S, true)
    val ipv6Status = TabSetting(R.string.setting_ip, "INTERNET_PROTOCOL", IPv6Choices.AUTOMATIC, IPv6Choices.values())
    val relayPort = IntSetting(R.string.setting_port, R.string.setting_port_desc, "RELAY_PORT", 1337, 1024..65535, restartRequired = true)
    val theme = ThemeSetting(R.string.setting_theme, "THEME", ThemeSetting.ThemeChoice.AUTO, ThemeSetting.ThemeChoice.values())
    val enableNotifications = BoolSetting(R.string.setting_notifications, R.string.setting_notifications_desc, "ENABLE_NOTIFICATIONS", true)
    val autoBackup = BoolSetting(R.string.setting_auto_backup, R.string.setting_auto_backup_desc, "AUTO_BACKUP", true)
    val showPingStats = BoolSetting(R.string.setting_ping_stats, R.string.setting_ping_stats_desc, "SHOW_PING_STATS", true)
    val autoReconnect = BoolSetting(R.string.setting_auto_reconnect, R.string.setting_auto_reconnect_desc, "AUTO_RECONNECT", false)

    val settings = arrayOf(
        theme,
        offlineSessionEncryption,
        enableCommandManager,
        enableRakReliability,
        relayPort,
        trustClicks,
        ipv6Status,
        enableNotifications,
        autoBackup,
        showPingStats,
        autoReconnect
    )

    enum class IPv6Choices(override val displayName: Int, override val internalName: String) : TabChoice {
        AUTOMATIC(R.string.setting_ip_auto, "auto"),
        ENABLED(R.string.setting_ip_enabled, "enabled"),
        DISABLED(R.string.setting_ip_disabled, "disabled"),
        V6ONLY(R.string.setting_ip_only, "v6only"),
    }
}
