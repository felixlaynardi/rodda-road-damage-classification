package com.rodda.roddaapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rodda.roddaapplication.databinding.FragmentHomeBinding
import com.rodda.roddaapplication.model.ResultModel
import com.rodda.roddaapplication.ui.detail.DetailActivity
import com.rodda.roddaapplication.ui.detail.DetailActivity.Companion.DOC_ID

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()

        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        val query : Query = firebaseFirestore.collection("results").orderBy("time", Query.Direction.DESCENDING)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<ResultModel> = FirestoreRecyclerOptions.Builder<ResultModel>()
            .setQuery(query, ResultModel::class.java)
            .build()

        adapter = HomeAdapter((firestoreRecyclerOptions))
        binding.rvListResult.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        binding.rvListResult.setHasFixedSize(true)
        binding.rvListResult.layoutManager = LinearLayoutManager(activity)
        binding.rvListResult.adapter = adapter

        adapter.setOnItemClickCallback(object: HomeAdapter.OnItemClickCallback{
            override fun onItemClicked(documentSnapshot: DocumentSnapshot, position: Int) {
                val docID = documentSnapshot.id
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DOC_ID, docID)
                startActivity(intent)
            }

        })
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}


