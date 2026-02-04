package com.useai.feature.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.useai.core.designsystem.R
import com.useai.core.ui.InputFieldLabel
import com.useai.core.ui.InputFormContainer
import com.useai.core.ui.LetterCountInput
import com.useai.core.ui.LogitFormTitle
import com.useai.core.ui.LogitInputField

@Composable
fun AdditionalQuestionInput(
    modifier: Modifier = Modifier,
) {
    InputFormContainer(
        modifier = modifier,
        onClickBackButton = { /*TODO*/ },
        bottomButtonText = "확인",
        onClickBottomButton = { /*TODO*/ },
        bottomButtonEnabled = true,
    ) {
        LogitFormTitle(
            title = "추가 문항 입력",
            desc = "추가할 문항을 입력해주세요",
        )
        Spacer(Modifier.height(41.dp))
        LogitInputField(
            label = "문항",
            isRequired = true,
            input = "", // TODO: state
            onInputChange = { /*TODO*/ },
            placeHolder = "예) 지원동기를 입력해주세요"
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_between_fields)))
        LetterCountField(
            onValueChange = { TODO() }
        )
    }
}

@Composable
private fun LetterCountField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    Column (
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputFieldLabel(
                modifier = Modifier.weight(1f),
                text = "글자수",
                isRequired = true,
            )
        }
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.spacing_label_to_input))
        )
        LetterCountInput(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange,
        )
    }
}

@Preview
@Composable
private fun AdditionalQuestionInputPreview() {
    AdditionalQuestionInput()
}
