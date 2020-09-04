package com.showcase.pricetracker.usecase

/**
 * A use-case class used to analyse stocks for charting purposes
 */
class StockAnalyser {

    /**
     * Use this function to arrange quotation in an
     * ### Arithmetic Progression where d = fetch interval.

     * The quotation list returned in the history is extrapolated
     * such that during the time period the recording is turned off,
     * the change in quotation is considered to be constant with time.
     *
     * @param quotes: List of recorded quotes for a stock
     * @return The history of the stock.
     */
    fun getStockHistory(quotes: List<Quote>): StockHistory {
        return when {
            quotes.isEmpty() -> StockHistory()
            quotes.size < 2 -> getStockHistory(
                quotes.last(),
                emptyList()
            )
            else -> getStockHistory(
                quotes.last(),
                getHistoryInArithmeticProgression(quotes)
            )
        }
    }

    private fun getStockHistory(
        latestQuote: Quote,
        orderedQuoteList: List<Quote>
    ): StockHistory {
        return StockHistory(
            latestQuote.sid,
            latestQuote.price,
            latestQuote.change,
            getPercentChange(latestQuote),
            latestQuote.epoch,
            orderedQuoteList
        )
    }

    private fun getHistoryInArithmeticProgression(
        quoteList: List<Quote>
    ): List<Quote> {
        val orderedList = mutableListOf<Quote>()
        lateinit var lastItem: Quote
        for (index in quoteList.indices) {
            val currentItem = quoteList[index]
            if (isNotFirstElement(index))
                orderedList.assertInAP(lastItem, currentItem)
            lastItem = currentItem
            orderedList.add(lastItem)
        }
        return orderedList
    }

    private fun MutableList<Quote>.assertInAP(
        lastItem: Quote,
        currentItem: Quote
    ) {
        calcTermsBetween(currentItem, lastItem)
            .also { noOfTerms ->
                if (noOfTerms > 0)
                    addToList(noOfTerms, lastItem)
            }
    }

    private fun calcTermsBetween(currentItem: Quote, lastItem: Quote) =
        ((currentItem.epoch - lastItem.epoch) / INTERVAL) - 1

    private fun MutableList<Quote>.addToList(numberOfTerms: Long, lastItem: Quote) {
        for (multiplier in 1..numberOfTerms)
            add(lastItem.copy(epoch = lastItem.epoch + (INTERVAL * multiplier)))
    }

    private fun getPercentChange(latestQuote: Quote) =
        (latestQuote.change / latestQuote.price) * 100

    private fun isNotFirstElement(index: Int) = index != 0
}
