package com.hqnguyen.syl_v2.persentation.page.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hqnguyen.syl_v2.data.entity.RecordAndInfo


@Composable
fun Recent(
    modifier: Modifier = Modifier,
    listRecord: List<RecordAndInfo>,
    navigation: (() -> Unit)?
) {
    val TAG = "Recent"

    Log.d(TAG, "Recent: $listRecord")

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Text(
                text = "Recent Activity",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Add",
                modifier = modifier.clickable(true) {
                    navigation?.let { it() }
                },
                color = MaterialTheme.colorScheme.primary, fontSize = 13.sp
            )
            Text(text = "All", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (listRecord.size >= 4) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            ) {
                LazyColumn {
                    itemsIndexed(items = listRecord) { index, item ->
                        ItemRecent(record = item)
                        if (index < listRecord.lastIndex)
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(74.dp, 0.dp, 20.dp, 0.dp)
                            )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            ) {
                LazyColumn {
                    itemsIndexed(
                        items = listRecord  ) { index, item ->
                        ItemRecent(record = item)
                        if (index < listRecord.lastIndex)
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(74.dp, 0.dp, 20.dp, 0.dp)
                            )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}