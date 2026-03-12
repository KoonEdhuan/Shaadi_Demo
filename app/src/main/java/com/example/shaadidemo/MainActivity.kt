package com.example.shaadidemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.shaadidemo.data.connection.NetworkObserver
import com.example.shaadidemo.data.connection.RetrofitClient
import com.example.shaadidemo.data.db.AppDataBase
import com.example.shaadidemo.data.repository.MatchRepository
import com.example.shaadidemo.ui.theme.ShaadiDemoTheme
import com.example.shaadidemo.viewmodel.MatchViewModel
import com.example.shaadidemo.viewmodel.MatchViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDataBase.getDatabase(applicationContext)
        val dao = database.matchDao()

        val repository = MatchRepository(RetrofitClient.service, dao)
        val networkObserver = NetworkObserver(applicationContext)


        val viewModelFactory = MatchViewModelFactory(repository, networkObserver)
        val viewModel = ViewModelProvider(this, viewModelFactory)[MatchViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            ShaadiDemoTheme {
                ProfilesScreen(viewModel)
            }
        }
    }
}