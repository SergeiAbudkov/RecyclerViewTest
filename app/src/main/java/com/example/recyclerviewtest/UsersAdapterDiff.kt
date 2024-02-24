package com.example.recyclerviewtest

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerviewtest.databinding.ItemUserBinding
import com.example.recyclerviewtest.model.User

interface ActionsUsers {

    fun moveUp(user: User, moveBy: Int)

    fun moveDown(user: User, moveBy: Int)

    fun deleteUser(user: User)

    fun userDetails(user: User)

    fun fireUser(user: User)

}

class UsersAdapterDiff(
    private val actionsUsers: ActionsUsers
) : ListAdapter<User,UsersAdapterDiff.UsersViewHolder>(DiffCallback()), View.OnClickListener {

    var users: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class UsersViewHolder(
        val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val user = v.tag as User
        when (v.id) {
            R.id.moreImageViewButton -> {
                showPopupMenu(v)
            }

            else -> {
                actionsUsers.userDetails(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.moreImageViewButton.setOnClickListener(this)

        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]

        with(holder.binding) {
            holder.itemView.tag = user
            moreImageViewButton.tag = user

            userNameTextView.text = user.name
            userCompanyTextView.text = user.company
            Glide
                .with(photoImageView.context)
                .load(user.photo)
                .centerCrop()
                .placeholder(R.drawable.ic_user_avatar)
                .error(R.drawable.ic_user_avatar)
                .into(photoImageView)

        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = android.widget.PopupMenu(view.context, view)
        val context = view.context
        val user = view.tag as User
        val position = users.indexOfFirst { it.id == user.id }

        popupMenu.menu.add(0, MOVE_UP, Menu.NONE, context.getString(R.string.move_up)).apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, MOVE_DOWN, Menu.NONE, context.getString(R.string.move_down)).apply {
            isEnabled = position < users.size - 1
        }
        popupMenu.menu.add(0, USER_DELETE, Menu.NONE, context.getString(R.string.remove))
        popupMenu.menu.add(0, USER_FIRE, Menu.NONE, context.getString(R.string.fire))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                MOVE_UP -> {
                    actionsUsers.moveUp(user, -1)
                }

                MOVE_DOWN -> {
                    actionsUsers.moveDown(user, 1)
                }

                USER_DELETE -> {
                    actionsUsers.deleteUser(user)
                }
                USER_FIRE -> {
                    actionsUsers.fireUser(user)
                }
            }
            return@setOnMenuItemClickListener true
        }


        popupMenu.show()
    }

    class DiffCallback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }


    companion object {

        const val MOVE_UP = 1
        const val MOVE_DOWN = 2
        const val USER_DELETE = 3
        const val USER_FIRE = 4
    }




}