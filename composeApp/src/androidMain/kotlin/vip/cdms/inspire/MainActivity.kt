package vip.cdms.inspire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import vip.cdms.inspire.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                App()
            }
        }
    }
}
