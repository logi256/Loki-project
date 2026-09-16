package com.example.smartbikepass

import android.app.Application
import com.example.smartbikepass.data.db.AppDatabase
import com.example.smartbikepass.data.repository.BikePassRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class SmartBikePassApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {
        BikePassRepository(
            database.applicationDao(),
            database.userDao(),
            database.auditLogDao()
        )
    }
}
