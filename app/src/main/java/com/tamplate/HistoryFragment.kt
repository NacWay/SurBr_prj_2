package com.tamplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn:ImageButton = view.findViewById(R.id.backBtn)


        val dataBase = getActivity()?.let { DataBase(it.getApplicationContext()) }

        val recyclerView: RecyclerView = view.findViewById(R.id.historyList)
        val adapter = HistoryAdapter(IconList().listImage, dataBase!!.getWin, dataBase!!.getIcon1, dataBase!!.getIcon2, dataBase!!.getIcon3)
        recyclerView.adapter= adapter


        backBtn.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                remove(this@HistoryFragment)
                commit()
            }
        }
    }
}