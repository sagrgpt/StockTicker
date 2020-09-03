package com.showcase.pricetracker.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.showcase.pricetracker.R
import com.showcase.pricetracker.ui.adapter.WatchListAdapter
import com.showcase.pricetracker.usecase.StockOverview
import kotlinx.android.synthetic.main.watch_list_fragment.*

class WatchListFragment : Fragment() {

    companion object {
        fun newInstance() = WatchListFragment()
    }

    private lateinit var viewModel: WatchListViewModel

    private lateinit var adapter: WatchListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.watch_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        setupListView()
        viewModel = ViewModelProvider(this).get(WatchListViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_record -> Log.i("WatchList", "Record option pressed")
            R.id.action_history -> Log.i("WatchList", "History option pressed")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupListView() {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        ).also { watchListRecycler.layoutManager = it }

        DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            .also { watchListRecycler.addItemDecoration(it) }

        adapter = WatchListAdapter(emptyList()) { onClick(it) }
    }

    private fun onClick(stock: StockOverview) {
        Log.i("WatchList", "${stock.sid} clicked")
    }

}