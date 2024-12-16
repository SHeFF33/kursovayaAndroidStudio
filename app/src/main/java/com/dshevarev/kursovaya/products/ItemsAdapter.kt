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
import com.dshevarev.kursovaya.models.Item

class ItemsAdapter(var items: List<Item>, var context: Context) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.item_list_image)
        val brand: TextView = view.findViewById(R.id.item_list_brand)
        val model: TextView = view.findViewById(R.id.item_list_model)
        val description: TextView = view.findViewById(R.id.item_list_description)
        val magprice: TextView = view.findViewById(R.id.item_list_magprice)
        val btn: Button = view.findViewById(R.id.item_list_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.brand.text = items[position].brand
        holder.model.text = items[position].model
        holder.description.text = items[position].description
        holder.magprice.text = items[position].magprice.toString()

        val imageId = context.resources.getIdentifier(
            items[position].image,
            "drawable",
            context.packageName
        )

        holder.image.setImageResource(imageId)

        holder.btn.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("itemId", items[position].id)
            intent.putExtra("itemImage", items[position].image)
            intent.putExtra("itemBrand", items[position].brand)
            intent.putExtra("itemModel", items[position].model)
            intent.putExtra("itemText", items[position].text)
            intent.putExtra("itemMagprice", items[position].magprice.toString())
            (context as ItemsActivity).startActivityForResult(intent, 1)
        }
    }

    fun updateItems(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }
}
