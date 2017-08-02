package io.github.morgaroth.android.bitmaszyna

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView


data class OfferViewHolder(val price: TextView, val amount: TextView, val priceInPLN: TextView, val action: Button)

class OfferArrayAdapter(val data: List<Offer>, ctx: Context) : BaseAdapter() {

    val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inf = if (convertView == null) {
            val view = inflater.inflate(R.layout.offer_row, null)
            val holder = OfferViewHolder(
                    view.findViewById<TextView>(R.id.price),
                    view.findViewById<TextView>(R.id.amount),
                    view.findViewById<TextView>(R.id.cost),
                    view.findViewById<Button>(R.id.offer_action))
            view.tag = holder
            Pair(view, holder)
        } else {
            Pair(convertView, convertView.tag as OfferViewHolder)
        }
        val r = data[position]
        val h = inf.second
        h.price.text = r.price.toString()
        h.amount.text = r.amount.toString()
        h.priceInPLN.text = (r.amount * r.price).toString()
        h.action.text = "Buy"
        return inf.first
    }

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = data.size
}