package com.useai.core.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.designsystem.theme.LogitTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DUE_DATE_DISPLAY_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy. MM. dd")

@Composable
fun LogitDateInputField(
    modifier: Modifier = Modifier,
    dueDate: LocalDate?,
    enabled: Boolean,
    onDateSelected: (LocalDate?) -> Unit,
) {
    val context = LocalContext.current

    Text(
        modifier = modifier
            .border(
                width = 1.dp,
                color = LogitTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable(enabled = enabled) {
                val initial = dueDate ?: LocalDate.now()
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                    },
                    initial.year,
                    initial.monthValue - 1,
                    initial.dayOfMonth,
                ).show()
            }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        text = dueDate?.format(DUE_DATE_DISPLAY_FORMATTER)
            ?: stringResource(R.string.project_field_due_date_placeholder),
        color = if (dueDate == null) LogitTheme.colors.gray200 else LogitTheme.colors.black,
        style = LogitTheme.typography.body6_1,
    )
}

@Preview
@Composable
private fun LogitDateInputFieldPreview() {
    LogitTheme {
        LogitDateInputField(
            dueDate = null,
            enabled = false,
            onDateSelected = {}
        )
    }
}
