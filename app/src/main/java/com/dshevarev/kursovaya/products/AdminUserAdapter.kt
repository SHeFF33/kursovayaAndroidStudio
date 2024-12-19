package com.dshevarev.kursovaya.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.models.User

class AdminUserAdapter(private val users: List<User>) : RecyclerView.Adapter<AdminUserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val email: TextView = itemView.findViewById(R.id.user_item_email)
        val login: TextView = itemView.findViewById(R.id.user_item_login)
        val phone: TextView = itemView.findViewById(R.id.user_item_phone)
        val isAdmin: TextView = itemView.findViewById(R.id.user_item_is_admin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.email.text = user.email
        holder.login.text = user.login
        holder.phone.text = user.phone
        holder.isAdmin.text = if (user.isAdmin) "Admin" else "User"
    }

    override fun getItemCount(): Int {
        return users.size
    }
}