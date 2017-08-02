package io.github.morgaroth.android.bitmaszyna

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class BIDFragment : ListFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater!!.inflate(R.layout.bid, null) as ViewGroup

        val data = arguments.getParcelableArrayList<Offer>(Data.BIDS_KEY).toList().sortedBy { it.price }.reversed()
        val adapter = OfferArrayAdapter(data, activity)

        listAdapter = adapter
        return root
    }

    companion object {
        fun newInstance(data: Bundle): Fragment {
            val f = BIDFragment()
            f.arguments = data
            return f
        }

        val className = BIDFragment::class.java.canonicalName
    }
}