package io.github.morgaroth.android.bitmaszyna

import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

data class BIDsAndASKs(val bids: List<Offer>, val asks: List<Offer>)

object Tasks {
    operator fun JSONArray.iterator(): Iterator<Any>
            = (0 until length()).asSequence().map { get(it) }.iterator()

    fun getTransactions(market: String = "BTCPLN"): List<Transaction> {
        val jsonStr = URL("https://bitmaszyna.pl/api/$market/transactions.json").readText()
        return JSONArray(jsonStr).iterator().asSequence().toList().map { Transaction.parse(it as JSONObject) }
    }

    fun getASKsAndBIDs(market: String = "BTCPLN"): BIDsAndASKs {
        val jsonStr = URL("https://bitmaszyna.pl/api/$market/depthSimple.json").readText()
        val data = JSONObject(jsonStr)
        val asks_data = (data.get("asks") as JSONArray).iterator().asSequence().toList().map { Offer.parse(it as JSONArray, false) }
        val bids_data = (data.get("bids") as JSONArray).iterator().asSequence().toList().map { Offer.parse(it as JSONArray, true) }
        return BIDsAndASKs(bids_data, asks_data)
    }

    fun getMarketState(market: String = "BTCPLN"): Ticker {
        val jsonStr = URL("https://bitmaszyna.pl/api/$market/ticker.json").readText()
        return Ticker.parse(JSONObject(jsonStr))
    }
}