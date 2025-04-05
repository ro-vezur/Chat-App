package com.example.chatapp.layouts.sharedComponents.validation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.ui.theme.FriendColor

@Composable
fun ValidatedTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (text: String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    validationResult: ValidationResult,
    placeHolderText: String,
    onDone: KeyboardActionScope.() -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Unspecified),
) {
    val isError = validationResult is ValidationResult.Error
    val isSuccess = validationResult is ValidationResult.Success

    Column(
        modifier = modifier

    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = textStyle,
            label = { Text(placeHolderText) },
            isError = isError,
            singleLine = true,
            shape = RoundedCornerShape(30),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onDone = onDone
            ),
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth()
        )

        if(isError || isSuccess && validationResult.resultMessage != null) {
            Text(
                text = validationResult.resultMessage.toString(),
                color = if(isSuccess) FriendColor else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 8.sdp)
                    .weight(1f)
            )
        }
    }
}