package com.showcase.pricetracker.usecase

class StockAnalyser {

    fun getStockHistory(quotes: List<Stock>): QuoteHistory {
        return if (quotes.isEmpty())
            return QuoteHistory()
        else
            getStockHistory(
                quotes.last(),
                getHistoryInArithmeticProgression(quotes)
            )
    }

    private fun getStockHistory(
        latestStock: Stock,
        orderedStockList: List<Stock>
    ): QuoteHistory {
        return QuoteHistory(
            latestStock.price,
            latestStock.change,
            getPercentChange(latestStock),
            latestStock.epoch,
            orderedStockList
        )
    }

    private fun getHistoryInArithmeticProgression(
        quoteList: List<Stock>
    ): List<Stock> {
        val orderedList = mutableListOf<Stock>()
        lateinit var lastItem: Stock
        for (index in quoteList.indices) {
            val currentItem = quoteList[index]
            if (isNotFirstElement(index))
                orderedList.assertInAP(lastItem, currentItem)
            lastItem = currentItem
            orderedList.add(lastItem)
        }
        return orderedList
    }

    private fun MutableList<Stock>.assertInAP(
        lastItem: Stock,
        currentItem: Stock
    ) {
        calcTermsBetween(currentItem, lastItem)
            .also { noOfTerms ->
                if (noOfTerms > 0)
                    addToList(noOfTerms, lastItem)
            }
    }

    private fun calcTermsBetween(currentItem: Stock, lastItem: Stock) =
        ((currentItem.epoch - lastItem.epoch) / INTERVAL) - 1

    private fun MutableList<Stock>.addToList(numberOfTerms: Long, lastItem: Stock) {
        for (multiplier in 1..numberOfTerms)
            add(lastItem.copy(epoch = lastItem.epoch + (INTERVAL * multiplier)))
    }

    private fun getPercentChange(latestQuote: Stock) =
        (latestQuote.change / latestQuote.price) * 100

    private fun isNotFirstElement(index: Int) = index != 0
}
