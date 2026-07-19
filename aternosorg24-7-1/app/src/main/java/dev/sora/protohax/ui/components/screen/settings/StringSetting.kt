package dev.sora.protohax.ui.components.screen.settings

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.sora.protohax.R
import dev.sora.protohax.util.ContextUtils.readStringOrDefault
import dev.sora.protohax.util.ContextUtils.writeString

class StringSetting(
    override val name: Int,
    override val description: Int,
    val key: String,
    val default: String,
    override val restartRequired: Boolean = false,
    val callback: (String) -> Unit = {}
) : ISetting<String> {

    @Composable
    override fun Draw(restartRequiredCallback: () -> Unit) {
        val mContext = LocalContext.current
        var value by remember { mutableStateOf(getValue(mContext)) }
        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            var dialogValue by remember { mutableStateOf(value) }
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(name)) },
                text = {
                    OutlinedTextField(
                        value = dialogValue,
                        onValueChange = { dialogValue = it },
                        singleLine = true,
                        label = { Text(stringResource(name)) }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        value = dialogValue
                        setValue(mContext, value)
                        callback(value)
                        showDialog = false
                        if (restartRequired) {
                            restartRequiredCallback()
                        }
                    }) {
                        Text(stringResource(R.string.dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.dialog_cancel))
                    }
                }
            )
        }

        SettingsTab(
            name = name, description = description,
            modifier = Modifier.clickable { showDialog = true },
            extraPadding = false
        ) {
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.config_rename))
            }
        }
    }

    override fun getValue(context: Context): String {
        return context.readStringOrDefault(key, default)
    }

    override fun setValue(context: Context, value: String) {
        context.writeString(key, value)
    }
}
