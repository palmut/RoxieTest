package net.palmut.roxietest

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import net.palmut.roxietest.net.Order

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<OrdersVM>()

    private val ordersAdapter = OrdersAdapter { order -> OrderDetailsActivity.showOrder(this, order) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        with(recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = ordersAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.orders.observe(this, Observer { result ->
            when (result) {
                is ViewModelResult.Progress -> showProgress()
                is ViewModelResult.Success -> showOrders(result.data)
                is ViewModelResult.Error -> showError(result.exception.message)
            }
        })
    }

    private fun showError(message: String?) {
        message?.also {
            Snackbar.make(recycler, it, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(ResourcesCompat.getColor(resources, R.color.errorBackground, theme)).show()
        }
    }

    private fun showOrders(data: List<Order>) {
        ordersAdapter.submitList(data)
    }

    private fun showProgress() {
    }
}