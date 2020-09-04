package com.showcase.pricetracker.ui.stocks

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.showcase.pricetracker.R
import com.showcase.pricetracker.ui.SharedViewModel
import com.showcase.pricetracker.ui.SharedViewModel.SharedVmFactory
import com.showcase.pricetracker.ui.base.BaseFragment
import com.showcase.pricetracker.usecase.StockOverview
import kotlinx.android.synthetic.main.watch_list_fragment.*

class WatchListFragment : BaseFragment() {

    private lateinit var viewModel: SharedViewModel
    lateinit var adapter: WatchListAdapter
    lateinit var viewModelFactory: SharedVmFactory

    val itemClickListener: (StockOverview) -> Unit = { onItemClicked(it) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.watch_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getInjector().inject(this)
        setupListView()
        setUpViewModel()
        setHasOptionsMenu(true)
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
        viewModel.analyseStockHistory()
        navigateToHistoryFragment()
    }

    private fun onItemClicked(stock: StockOverview) {
        viewModel.analyseStockHistory(stock.sid)
        navigateToHistoryFragment()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(SharedViewModel::class.java)

        viewModel.watchList().observe(
            viewLifecycleOwner,
            { it?.let { adapter.addToDataSet(it) } }
        )
    }

    private fun setupListView() {
        LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        ).also { watchListRecycler.layoutManager = it }

        DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            .also { watchListRecycler.addItemDecoration(it) }
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


}