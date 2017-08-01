package io.github.morgaroth.android.bitmaszyna

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray
import org.json.JSONObject

data class Transaction(val tId: Long, val type: Int, val amount: Double, val price: Double, val date: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(tId)
        parcel.writeInt(type)
        parcel.writeDouble(amount)
        parcel.writeDouble(price)
        parcel.writeLong(date)
    }

    override fun describeContents(): Int = 0

    val isBID = type == 1
    val isACK = type == 2

    companion object {
        val CREATOR = object : Parcelable.Creator<Transaction> {
            override fun createFromParcel(parcel: Parcel) = Transaction(parcel)
            override fun newArray(size: Int): Array<Transaction?> = arrayOfNulls(size)
        }

        fun parse(data: JSONObject): Transaction {
            val tid_entry = data.get("tid")
            val date_entry = data.get("date")
            return Transaction(
                    (tid_entry as? Int)?.toLong() ?: tid_entry as Long,
                    data.get("type") as Int,
                    data.get("amount") as Double,
                    data.get("price") as Double,
                    (date_entry as? Int)?.toLong() ?: (date_entry as Long)
            )
        }
    }
}

data class Offer(val price: Double, val amount: Double, val isBID: Boolean) : Parcelable {
    val isASK = !isBID

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readInt() == 1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(price)
        parcel.writeDouble(amount)
        parcel.writeInt(if (isBID) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object {
        val CREATOR = object : Parcelable.Creator<Offer> {
            override fun createFromParcel(parcel: Parcel) = Offer(parcel)
            override fun newArray(size: Int): Array<Offer?> = arrayOfNulls(size)
        }

        fun parse(data: JSONArray, isBid: Boolean): Offer {
            return Offer(data[0].toString().toDouble(), data[1].toString().toDouble(), isBid)
        }
    }
}

data class Ticker(val bid: Double, val ask: Double,
                  val high: Double, val low: Double,
                  val last: Double,
                  val volume1: Double, val volume2: Double,
                  val average: Double, val vwap: Double) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble())

    companion object {
        val CREATOR = object : Parcelable.Creator<Ticker> {
            override fun createFromParcel(parcel: Parcel): Ticker = Ticker(parcel)
            override fun newArray(size: Int): Array<Ticker?> = arrayOfNulls(size)
        }

        fun parse(data: JSONObject) = Ticker(
                data.get("bid") as Double,
                data.get("ask") as Double,
                data.get("high") as Double,
                data.get("low") as Double,
                data.get("last") as Double,
                data.get("volume1") as Double,
                data.get("volume2") as Double,
                data.get("average") as Double,
                data.get("vwap") as Double
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(bid)
        parcel.writeDouble(ask)
        parcel.writeDouble(high)
        parcel.writeDouble(low)
        parcel.writeDouble(last)
        parcel.writeDouble(volume1)
        parcel.writeDouble(volume2)
        parcel.writeDouble(average)
        parcel.writeDouble(vwap)
    }

    override fun describeContents(): Int = 0
}