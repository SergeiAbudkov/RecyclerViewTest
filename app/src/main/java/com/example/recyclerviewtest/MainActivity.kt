package com.example.recyclerviewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerviewtest.databinding.ActivityMainBinding
import com.example.recyclerviewtest.model.User
import com.example.recyclerviewtest.model.UsersListener
import com.example.recyclerviewtest.model.UsersService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val usersService: UsersService
        get() = (applicationContext as App).usersService

    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }


        adapter = UsersAdapter(object: UserActionListener {

            override fun moveUser(user: User, moveBy: Int) {
                usersService.moveUser(user, moveBy)

            }

            override fun deleteUser(user: User) {
                usersService.deleteUser(user)
            }

            override fun userDetails(user: User) {
                Toast.makeText(this@MainActivity, user.name,Toast.LENGTH_SHORT).show()
            }

            override fun onUserFire(user: User) {
                usersService.fireUser(user)
            }

        })

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        usersService.addListener(usersListener)
    }


    override fun onDestroy() {
        super.onDestroy()
        usersService.removeListener(usersListener)
    }


    private val usersListener: UsersListener = {
        adapter.users = it
    }



}