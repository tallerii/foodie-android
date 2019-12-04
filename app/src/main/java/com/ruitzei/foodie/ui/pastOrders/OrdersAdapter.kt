package com.ruitzei.foodie.ui.pastOrders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruitzei.foodie.R
import com.ruitzei.foodie.model.OrderProperties
import kotlinx.android.synthetic.main.item_order.view.*

class OrdersAdapter(private val orders: List<OrderProperties>,
                    private val listener: (OrderProperties) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {


    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bindOrder(orders[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)

        return OrdersViewHolder(itemView)
    }

    override fun getItemCount(): Int = orders.size

    class OrdersViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bindOrder(order: OrderProperties, listener: (OrderProperties) -> Unit) = with(itemView) {
            itemView.item_order_title.text = "Pedido de ${order?.clientUser?.name} ${order?.clientUser?.lastName}"
            itemView.item_order_subtitle.text = order?.notes
            itemView.item_order_amount.text = "$ ${order?.price}"

            itemView.setOnClickListener {
                listener.invoke(order)
            }
        }
    }
}