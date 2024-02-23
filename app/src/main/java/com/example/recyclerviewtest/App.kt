package com.example.recyclerviewtest

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerviewtest.model.UsersService

class App: Application() {

    val usersService = UsersService()


}