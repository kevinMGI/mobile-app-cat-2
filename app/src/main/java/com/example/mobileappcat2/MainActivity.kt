package com.example.mobileappcat2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.mobileappcat2.ui.navigation.AppNavHost
import com.example.mobileappcat2.ui.theme.MobileAppCAT2Theme
import com.example.mobileappcat2.ui.viewmodel.RealEstateViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RealEstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAppCAT2Theme {
                AppNavHost(viewModel = viewModel)
            }
        }
    }
}
