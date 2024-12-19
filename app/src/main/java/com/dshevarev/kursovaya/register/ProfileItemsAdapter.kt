package com.dshevarev.kursovaya.register

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.models.Item

class ProfileItemsAdapter(var items: List<Item>, var context: Context) : RecyclerView.Adapter<ProfileItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.profile_item_list_image)
        val brand: TextView = view.findViewById(R.id.profile_item_list_brand)
        val model: TextView = view.findViewById(R.id.profile_item_list_model)
        val description: TextView = view.findViewById(R.id.profile_item_list_description)
        val price: TextView = view.findViewById(R.id.profile_item_list_price)
        val sold: TextView = view.findViewById(R.id.profile_item_list_sold)
        val see: Button = view.findViewById(R.id.profile_item_list_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.brand.text = items[position].brand
        holder.model.text = items[position].model
        holder.description.text = items[position].description
        holder.price.text = items[position].price.toString()

        val imageId = context.resources.getIdentifier(
            items[position].image,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(imageId)

        if (!items[position].status) {
            holder.sold.visibility = View.VISIBLE
        } else {
            holder.sold.visibility = View.GONE
        }
    }
}