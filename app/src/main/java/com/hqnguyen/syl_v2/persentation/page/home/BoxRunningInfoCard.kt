package com.hqnguyen.syl_v2.persentation.page.home

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
import com.hqnguyen.syl_v2.ui.theme.colorBackgroundColor

@Composable
fun BoxRunningInfo() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 0.dp)
            .height(60.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
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
                    text = "Current jogging",
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "01:09:44",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text(
                    text = "10,9 km",
                    color = Color.White,
                    fontWeight = FontWeight.W500,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "538 kcal",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp
                )
            }
        }
    }
}
