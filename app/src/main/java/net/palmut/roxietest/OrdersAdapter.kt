package net.palmut.roxietest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.palmut.roxietest.net.Address
import net.palmut.roxietest.net.Order
import net.palmut.roxietest.net.Price
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class OrdersAdapter(private val onClick: (Order) -> Unit) : ListAdapter<Order, ViewHolder>(OrderComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = getItem(position)
        holder.itemView.findViewById<TextView>(R.id.startAddress).text = order.startAddress.format()
        holder.itemView.findViewById<TextView>(R.id.endAddress).text = order.endAddress.format()
        holder.itemView.findViewById<TextView>(R.id.orderTime).text = order.orderTime.format()
        holder.itemView.findViewById<TextView>(R.id.price).text = order.price.format()
        holder.itemView.setOnClickListener { onClick(order) }
    }

    private class OrderComparator : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private val priceFormat = DecimalFormat("###,###,##0.00")
fun Address.format(): String = "$city, $address"
fun Price.format(): String = "${priceFormat.format(amount / 100)} $currency"
fun Date.format(): String = DateFormat.getDateTimeInstance().format(this)