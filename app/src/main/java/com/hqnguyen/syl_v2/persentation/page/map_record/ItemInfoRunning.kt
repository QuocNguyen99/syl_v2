package com.hqnguyen.syl_v2.persentation.page.map_record

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.DecimalFormat

@Composable
fun ItemInfoRunning(
    @DrawableRes iconId: Int,
    number: Float = 0f,
    numberBigDecimal: BigDecimal = BigDecimal(0),
    unit: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ICon(iconId)
        Spacer(modifier = Modifier.width(8.dp))
        TextValue(number, numberBigDecimal, unit)
    }
}

@Composable
fun ICon(@DrawableRes iconId: Int) {
    val painted = painterResource(id = iconId)
    Image(painter = painted, contentDescription = "", modifier = Modifier.size(32.dp))
}

@Composable
fun TextValue(
    number: Float = 0f,
    numberBigDecimal: BigDecimal = BigDecimal(0),
    unit: String
) {
    val decimalFormat = DecimalFormat("0.00")
    Column {
        Text(
            text = if (number != 0f) decimalFormat.format(number) else numberBigDecimal.toString(),
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = unit, color = Color.Black)
    }
}
