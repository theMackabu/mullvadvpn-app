package net.mullvad.mullvadvpn.compose.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import net.mullvad.mullvadvpn.R
import net.mullvad.mullvadvpn.compose.button.ActionButton
import net.mullvad.mullvadvpn.compose.component.ScaffoldWithTopBar
import net.mullvad.mullvadvpn.compose.state.DeviceRevokedUiState
import net.mullvad.mullvadvpn.compose.theme.AppTheme

@Preview
@Composable
fun PreivewDeviceRevokedScreen() {
    AppTheme { DeviceRevokedScreen(state = DeviceRevokedUiState.SECURED) }
}

@Composable
fun DeviceRevokedScreen(
    state: DeviceRevokedUiState,
    onSettingsClicked: () -> Unit = {},
    onGoToLoginClicked: () -> Unit = {}
) {
    val topColor =
        colorResource(
            if (state == DeviceRevokedUiState.SECURED) {
                R.color.green
            } else {
                R.color.red
            }
        )

    ScaffoldWithTopBar(
        topBarColor = topColor,
        statusBarColor = topColor,
        navigationBarColor = colorResource(id = R.color.darkBlue),
        onSettingsClicked = onSettingsClicked,
    ) {
        ConstraintLayout(
            modifier =
                Modifier.fillMaxHeight()
                    .fillMaxWidth()
                    .padding(it)
                    .background(colorResource(id = R.color.darkBlue))
        ) {
            val (icon, body, actionButtons) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.icon_fail),
                contentDescription = null, // No meaningful user info or action.
                modifier =
                    Modifier.constrainAs(icon) {
                            top.linkTo(parent.top, margin = 30.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(horizontal = 12.dp)
                        .width(80.dp)
                        .height(80.dp)
            )

            Column(
                modifier =
                    Modifier.constrainAs(body) {
                        top.linkTo(icon.bottom, margin = 22.dp)
                        start.linkTo(parent.start, margin = 22.dp)
                        end.linkTo(parent.end, margin = 22.dp)
                        width = Dimension.fillToConstraints
                    },
            ) {
                Text(
                    text = stringResource(id = R.string.device_inactive_title),
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(id = R.string.device_inactive_description),
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 10.dp)
                )

                if (state == DeviceRevokedUiState.SECURED) {
                    Text(
                        text = stringResource(id = R.string.device_inactive_unblock_warning),
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

            Column(
                modifier =
                    Modifier.constrainAs(actionButtons) {
                        bottom.linkTo(parent.bottom, margin = 22.dp)
                        start.linkTo(parent.start, margin = 22.dp)
                        end.linkTo(parent.end, margin = 22.dp)
                        width = Dimension.fillToConstraints
                    }
            ) {
                ActionButton(
                    text = stringResource(id = R.string.go_to_login),
                    onClick = onGoToLoginClicked,
                    colors =
                        ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor =
                                colorResource(
                                    if (state == DeviceRevokedUiState.SECURED) {
                                        R.color.red60
                                    } else {
                                        R.color.blue
                                    }
                                )
                        )
                )
            }
        }
    }
}
