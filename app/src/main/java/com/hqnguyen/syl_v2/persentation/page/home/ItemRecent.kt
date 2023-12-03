package com.hqnguyen.syl_v2.persentation.page.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hqnguyen.syl_v2.R
import com.hqnguyen.syl_v2.data.entity.RecordAndInfo
import com.hqnguyen.syl_v2.utils.secondToHourMinuteSecond
import com.hqnguyen.syl_v2.utils.toTimeWithFormat


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemRecent(record: RecordAndInfo) {
    Log.d("ItemRecent", "record: $record")
    Row(
        modifier = Modifier
            .padding(8.dp, 8.dp, 8.dp, 8.dp)
            .height(IntrinsicSize.Max)
            .clickable {

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_running),
            contentDescription = null,
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Time Start: ${record.record?.timeStart?.toTimeWithFormat() ?: ""}",
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Run in: ${record.record?.countTime?.secondToHourMinuteSecond() ?: ""}",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row {
                Text(
                    text = "${
                        String.format(
                            "%.1f",
                            record.infoRecord?.lastOrNull { true }?.calo ?: 0.0
                        )
                    } calo",
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${record.infoRecord?.lastOrNull { true }?.distance ?: 0.0} km",
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
