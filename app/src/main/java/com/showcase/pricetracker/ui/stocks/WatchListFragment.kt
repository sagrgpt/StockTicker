package com.showcase.pricetracker.ui.stocks

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.showcase.pricetracker.R
import com.showcase.pricetracker.network.NetworkFactory
import com.showcase.pricetracker.schedulers.DefaultScheduler
import com.showcase.pricetracker.ui.SharedViewModel
import com.showcase.pricetracker.ui.SharedViewModel.WatchListVmFactory
import com.showcase.pricetracker.ui.WatchListFragmentDirections
import com.showcase.pricetracker.usecase.StockOverview
import com.showcase.pricetracker.usecase.StockRecorder
import com.showcase.pricetracker.usecase.Watchlist
import kotlinx.android.synthetic.main.watch_list_fragment.*

class WatchListFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel

    private lateinit var adapter: WatchListAdapter

    private val viewStateObserver = Observer<Watchlist> {
        adapter.dataSet = it.stockList
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.watch_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        setupListView()
        initDependencies()
        viewModel.viewState().observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_record -> onActionRecord(item)
            R.id.action_history -> onClickHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onActionRecord(item: MenuItem) {
        viewModel.toggleRecording()
        if (viewModel.isRecording())
            item.setAsPause()
        else
            item.setAsPlay()
    }

    private fun onClickHistory() {
        viewModel.setStockInFocus()
        navigateToHistoryFragment()
    }

    private fun onClick(stock: StockOverview) {
        viewModel.setStockInFocus(stock.sid)
        navigateToHistoryFragment()
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

        watchListRecycler.adapter = adapter
    }

    private fun MenuItem.setAsPause() {
        setDrawable(R.drawable.ic_pause_black)
        title = getString(R.string.action_pause)
    }

    private fun MenuItem.setAsPlay() {
        setDrawable(R.drawable.ic_play_arrow_black)
        title = getString(R.string.action_pause)
    }

    private fun MenuItem.setDrawable(id: Int) {
        context?.let { setIcon(ContextCompat.getDrawable(it, id)) }
    }

    private fun navigateToHistoryFragment() {
        val direction = WatchListFragmentDirections.actionWatchListFragmentToHistoryFragment()
        findNavController().navigate(direction)
    }

    private fun initDependencies() {
        val schedulerProvider = DefaultScheduler()
        val remote = NetworkFactory.createGateway()
            .getQuotationRemote()
        val usecase = StockRecorder(remote, schedulerProvider)
        viewModel = ViewModelProvider(
            requireActivity(),
            WatchListVmFactory(usecase, schedulerProvider)
        ).get(SharedViewModel::class.java)
    }


}