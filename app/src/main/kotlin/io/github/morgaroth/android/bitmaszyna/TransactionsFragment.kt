package io.github.morgaroth.android.bitmaszyna

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class TransactionsFragment : ListFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.transactions, null) as ViewGroup
        val data = arguments.getParcelableArrayList<Transaction>(Data.TRANSACTIONS_KEY).toList().sortedBy { it.date }.reversed()
        val adapter = ArrayAdapter<String>(inflater.context, android.R.layout.simple_list_item_1, data.map { it.toString() })

        listAdapter = adapter
        return root
    }

    companion object {
        fun newInstance(data: Bundle): Fragment {
            val f = TransactionsFragment()
            f.arguments = data
            return f
        }

        val className = TransactionsFragment::class.java.canonicalName
    }

}