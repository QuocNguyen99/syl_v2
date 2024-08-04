package com.hqnguyen.syl_v2.persentation.page.map_record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hqnguyen.syl_v2.R
import java.math.BigDecimal

@Composable
fun InfoRunning(
    speedMeter: Float = 0f,
    distance: BigDecimal = BigDecimal(0),
    calo: Float = 0f,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ItemInfoRunning(
                iconId = R.drawable.ic_speed_meter,
                number = speedMeter,
                unit = "km/h"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.LightGray)
            )

            ItemInfoRunning(iconId = R.drawable.ic_kcal, number = calo, unit = "kCal")
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color.LightGray)
            )

            ItemInfoRunning(
                iconId = R.drawable.ic_distance,
                numberBigDecimal = distance,
                unit = "km"
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}