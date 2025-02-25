package com.example.chatapp.layouts.sharedComponents.inputFields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chatapp.differentScreensSupport.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier
        .fillMaxWidth(0.92f)
        .height(65.sdp),
    query: String,
    placeHolder: String,
    onSearchQueryChange: (String) -> Unit,
) {
    SearchBar(
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        inputField = {
            SearchBarDefaults.InputField(
                colors = TextFieldDefaults.colors(

                ),
                query = query,
                onQueryChange = onSearchQueryChange,
                onSearch = {  },
                expanded = false,
                onExpandedChange = {},
                placeholder = {
                    Text(
                        text = placeHolder,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = 6.sdp),
                        onClick = { }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.sdp),
                            imageVector = Icons.Filled.Search,
                            contentDescription = "search",
                        )
                    }
                }
            )
        },
        expanded = false,
        onExpandedChange = {}
    ) {

    }
}