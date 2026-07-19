package dev.sora.protohax.ui.components.screen.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.sora.protohax.util.ContextUtils.readIntOrDefault
import dev.sora.protohax.util.ContextUtils.writeInt

class IntSetting(
    override val name: Int,
    override val description: Int,
    val key: String,
    val default: Int,
    val range: ClosedRange<Int> = 1..65535,
    override val restartRequired: Boolean = false,
    val callback: (Int) -> Unit = {}
) : ISetting<Int> {

    @Composable
    override fun Draw(restartRequiredCallback: () -> Unit) {
        val mContext = LocalContext.current
        var value by remember { mutableStateOf(getValue(mContext)) }

        SettingsTab(
            name = name, description = description,
            modifier = Modifier,
            extraPadding = false
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(
                    text = value.toString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Slider(
                    value = value.toFloat(),
                    valueRange = range.start.toFloat()..range.endInclusive.toFloat(),
                    onValueChange = {
                        value = it.toInt()
                        setValue(mContext, value)
                    },
                    onValueChangeFinished = {
                        callback(value)
                        if (restartRequired) {
                            restartRequiredCallback()
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .padding(0.dp)
                        .widthIn(max = 150.dp)
                )
            }
        }
    }

    override fun getValue(context: Context): Int {
        return context.readIntOrDefault(key, default)
    }

    override fun setValue(context: Context, value: Int) {
        context.writeInt(key, value)
    }
}
