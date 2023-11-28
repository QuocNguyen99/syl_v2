package com.hqnguyen.syl_v2.persentation.page.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hqnguyen.syl_v2.R
import com.hqnguyen.syl_v2.data.InfoTracking
import com.hqnguyen.syl_v2.ui.theme.colorBackgroundColor
import com.hqnguyen.syl_v2.utils.secondToHourMinuteSecond
import java.math.BigDecimal

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxRunningInfo(
    distance: BigDecimal = BigDecimal(0),
    calo: Float = 0.0f,
    countTime: Long = 0L,
    navigationToMapRecord: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp)
            .height(60.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
        onClick = { navigationToMapRecord() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp, 0.dp)
        ) {
            ConstraintLayout {
                val (backgroundIconRef, iconRef) = createRefs()
                Surface(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .constrainAs(backgroundIconRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                    color = colorBackgroundColor,
                ) {}
                Image(
                    painter = painterResource(id = R.drawable.ic_running),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(0.dp, 12.dp)
                        .constrainAs(iconRef) {
                            top.linkTo(backgroundIconRef.top)
                            end.linkTo(backgroundIconRef.end)
                            bottom.linkTo(backgroundIconRef.bottom)
                            start.linkTo(backgroundIconRef.start)
                        }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Running",
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = countTime.secondToHourMinuteSecond(),
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = "${distance} km",
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${String.format("%.1f", calo)} kcal",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
        }
    }
}
