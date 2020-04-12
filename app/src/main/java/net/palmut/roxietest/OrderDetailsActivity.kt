package net.palmut.roxietest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_order_details.*
import net.palmut.roxietest.net.Order
import net.palmut.roxietest.net.toExtra
import net.palmut.roxietest.net.toOrder

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        order = requireNotNull(intent?.getStringExtra(EXTRA_ORDER)?.toOrder())

        title = getString(R.string.order_details_title, order.id.toString())

        startAddress.text = order.startAddress.format()
        endAddress.text = order.endAddress.format()
        orderTime.text = order.orderTime.format()
        price.text = order.price.format()

    }

    companion object {
        private const val EXTRA_ORDER = "extra_order"

        fun showOrder(context: Context, order: Order) {
            context.startActivity(Intent(context, OrderDetailsActivity::class.java).putExtra(EXTRA_ORDER, order.toExtra()))
        }
    }
}