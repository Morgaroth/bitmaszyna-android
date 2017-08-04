package io.github.morgaroth.android.bitmaszyna

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
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

    fun getFunds(): String {
        val urlParameters = "nonce=${System.currentTimeMillis()}"

        val client = OkHttpClient()

        val body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), urlParameters)
        val request = Request.Builder()
                .url("https://bitmaszyna.pl/api/funds")
                .post(body)
                .header("Rest-Key", Keys.key)
                .header("Rest-Sign", ECDSAExample.sign(urlParameters))
                .build()
        val response = client.newCall(request).execute()
        return response.body()!!.string()

    }
}