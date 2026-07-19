package dev.sora.protohax.ui.components.screen.settings

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import dev.sora.protohax.R
import dev.sora.protohax.util.ContextUtils.readStringOrDefault
import dev.sora.protohax.util.ContextUtils.writeString

class ThemeSetting(
    override val name: Int,
    val key: String,
    val default: ThemeChoice,
    val choices: Array<ThemeChoice>,
    override val restartRequired: Boolean = false
) : ISetting<ThemeSetting.ThemeChoice> {

    override val description: Int
        get() = name

    @Composable
    override fun Draw(restartRequiredCallback: () -> Unit) {
        val mContext = LocalContext.current
        var value by remember { mutableStateOf(getValue(mContext)) }

        SettingsTab(
            name = name, description = R.string.setting_theme_desc,
            modifier = Modifier.clickable { },
            extraPadding = false
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                choices.forEach { choice ->
                    IconButton(
                        onClick = {
                            value = choice
                            setValue(mContext, choice)
                        }
                    ) {
                        Icon(
                            imageVector = choice.icon,
                            contentDescription = stringResource(choice.displayName),
                            tint = if (value == choice) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }

    override fun getValue(context: Context): ThemeChoice {
        val record = context.readStringOrDefault(key, default.internalName)
        return choices.find { it.internalName == record } ?: default
    }

    override fun setValue(context: Context, value: ThemeChoice) {
        context.writeString(key, value.internalName)
    }

    enum class ThemeChoice(override val displayName: Int, override val internalName: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) : TabChoice {
        AUTO(R.string.setting_theme_auto, "auto", Icons.Default.BrightnessAuto),
        LIGHT(R.string.setting_theme_light, "light", Icons.Default.LightMode),
        DARK(R.string.setting_theme_dark, "dark", Icons.Default.DarkMode)
    }
}
