package io.github.morgaroth.android.bitmaszyna

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ASKFragment : ListFragment() {

    lateinit var data: List<Transaction>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.ask, null) as ViewGroup

        val data = arguments.getParcelableArrayList<Offer>(Data.ASKS_KEY).toList().sortedBy { it.price }
        val adapter = OfferArrayAdapter(data, activity)

        listAdapter = adapter
        return root
    }

    companion object {
        fun newInstance(data: Bundle): Fragment {
            val f = ASKFragment()
            f.arguments = data
            return f
        }

        val className = ASKFragment::class.java.canonicalName
    }

}