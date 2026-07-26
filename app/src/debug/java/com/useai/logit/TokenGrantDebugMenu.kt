package com.useai.logit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.useai.core.data.repository.TokenGrantEventBus
import com.useai.core.model.account.TokenGrant
import kotlinx.coroutines.launch

@Composable
internal fun TokenGrantDebugMenu(
    tokenGrantEventBus: TokenGrantEventBus,
) {
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun emit(tokenGrant: TokenGrant) {
        scope.launch {
            tokenGrantEventBus.emit(tokenGrant)
        }
    }

    Popup(
        alignment = Alignment.BottomEnd,
        properties = PopupProperties(focusable = false),
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (expanded) {
                    Surface {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Button(
                                onClick = {
                                    emit(TokenGrant(signupBonusAmount = 100))
                                },
                            ) {
                                Text("Signup +100")
                            }
                            Button(
                                onClick = {
                                    emit(TokenGrant(monthlyGrantAmount = 200))
                                },
                            ) {
                                Text("Monthly +200")
                            }
                            Button(
                                onClick = {
                                    emit(TokenGrant(attendanceAmount = 3))
                                },
                            ) {
                                Text("Attendance +3")
                            }
                            Button(
                                onClick = {
                                    emit(TokenGrant(referralRewardAmount = 50))
                                },
                            ) {
                                Text("Referral +50")
                            }
                            Button(
                                onClick = {
                                    emit(
                                        TokenGrant(
                                            signupBonusAmount = 100,
                                            monthlyGrantAmount = 200,
                                            attendanceAmount = 3,
                                            referralRewardAmount = 50,
                                        )
                                    )
                                },
                            ) {
                                Text("All +353")
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { expanded = !expanded },
                ) {
                    Text("TOKEN")
                }
            }
        }
    }
}
