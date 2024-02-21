package com.example.bloodaid.view.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bloodaid.R
import com.example.bloodaid.adapter.DonorAdapter
import com.example.bloodaid.databinding.FragmentDonorBinding
import com.example.bloodaid.databinding.FragmentLoginBinding
import com.example.bloodaid.model.Donation
import com.example.bloodaid.util.viewBinding
import com.google.firebase.firestore.FirebaseFirestore


class DonorFragment : Fragment(R.layout.fragment_donor) {
    private val binding by viewBinding(FragmentDonorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadDonors()
    }

    private fun loadDonors() {
        val db = FirebaseFirestore.getInstance()
        val donationList = mutableListOf<Donation>()

        db.collection("donations").whereEqualTo("type", "Donor")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val donation = document.toObject(Donation::class.java)
                    donationList.add(donation)
                }
                setupRecyclerView(donationList)
            }
            .addOnFailureListener { exception ->
                Log.w("DonorFragment", "Error getting documents: ", exception)
            }
    }

    private fun setupRecyclerView(donationList: List<Donation>) {
        val adapter = DonorAdapter()
        adapter.submitList(donationList)
        binding.rvDonor.layoutManager = LinearLayoutManager(context)
        binding.rvDonor.adapter = adapter
    }

}