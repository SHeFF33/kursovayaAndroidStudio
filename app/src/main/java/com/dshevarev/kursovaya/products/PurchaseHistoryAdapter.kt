package com.dshevarev.kursovaya.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dshevarev.kursovaya.R
import com.dshevarev.kursovaya.models.Sale

class PurchaseHistoryAdapter(var sales: List<Sale>) : RecyclerView.Adapter<PurchaseHistoryAdapter.SaleViewHolder>() {

    class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brand: TextView = itemView.findViewById(R.id.sale_item_brand)
        val model: TextView = itemView.findViewById(R.id.sale_item_model)
        val price: TextView = itemView.findViewById(R.id.sale_item_price)
        val magprice: TextView = itemView.findViewById(R.id.sale_item_magprice)
        val saleDate: TextView = itemView.findViewById(R.id.sale_item_saledate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_purchase_history, parent, false)
        return SaleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = sales[position]
        holder.brand.text = sale.brand
        holder.model.text = sale.model
        holder.price.text = sale.price.toString()
        holder.magprice.text = sale.magprice.toString()
        holder.saleDate.text = sale.saleDate.toString()
    }

    override fun getItemCount(): Int {
        return sales.size
    }
}