package net.palmut.roxietest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_order_details.*
import net.palmut.roxietest.net.Order
import net.palmut.roxietest.net.OrdersRepository
import net.palmut.roxietest.net.toExtra
import net.palmut.roxietest.net.toOrder

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var order: Order
    private val viewModel by viewModels<OrdersVM> { OrderVMFactory(OrdersRepository) }

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

        model.text = order.vehicle.modelName
        regNumber.text = order.vehicle.regNumber
        driver.text = order.vehicle.driverName

        viewModel.photo(order.vehicle.photo).observe(this, Observer { result ->
            when (result) {
                is ViewModelResult.Success -> photo.setImageBitmap(result.data)
                is ViewModelResult.Error -> showError(result.exception.message)
                is ViewModelResult.Progress -> showProgress()
            }
        })
    }

    private fun showError(message: String?) {
        message?.also {
            Snackbar.make(photo, it, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ResourcesCompat.getColor(resources, R.color.errorBackground, theme)).show()
        }
    }

    private fun showProgress() {
        photo.setImageDrawable(null)
    }

    companion object {
        private const val EXTRA_ORDER = "extra_order"

        fun showOrder(context: Context, order: Order) {
            context.startActivity(Intent(context, OrderDetailsActivity::class.java).putExtra(EXTRA_ORDER, order.toExtra()))
        }
    }
}