package com.dshevarev.kursovaya.products

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
import com.dshevarev.kursovaya.dbhelper.DBHelper
import com.dshevarev.kursovaya.models.Item
import com.dshevarev.kursovaya.register.EditItemActivity

class AdminProductAdapter(var items: List<Item>, var context: Context) : RecyclerView.Adapter<AdminProductAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val brand: TextView = view.findViewById(R.id.item_list_brand)
        val model: TextView = view.findViewById(R.id.item_list_model)
        val description: TextView = view.findViewById(R.id.item_list_description)
        val text: TextView = view.findViewById(R.id.item_list_text)
        val price: TextView = view.findViewById(R.id.item_list_price)
        val magprice: TextView = view.findViewById(R.id.item_list_magprice)
        val status: TextView = view.findViewById(R.id.item_list_status)
        val edit: Button = view.findViewById(R.id.item_list_edit_button)
        val delete: Button = view.findViewById(R.id.item_list_delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_product_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.brand.text = items[position].brand
        holder.model.text = items[position].model
        holder.description.text = items[position].description
        holder.text.text = items[position].text
        holder.price.text = items[position].price.toString()
        holder.magprice.text = items[position].magprice.toString()
        holder.status.text = if (items[position].status) "В продаже" else "Продан"

        val imageId = context.resources.getIdentifier(
            items[position].image,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(if (imageId != 0) imageId else R.drawable.iphone)

        holder.edit.setOnClickListener {
            val intent = Intent(context, EditItemActivity::class.java)
            intent.putExtra("itemId", items[position].id)
            context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            val dbHelper = DBHelper(context, null)
            dbHelper.deleteItem(items[position].id)
            items = items.filter { it.id != items[position].id }
            notifyDataSetChanged()
        }
    }

    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}