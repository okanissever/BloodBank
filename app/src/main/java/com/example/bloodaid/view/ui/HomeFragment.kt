package com.example.bloodaid.view.ui

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.bloodaid.R
import com.example.bloodaid.adapter.SectionsPagerAdapter
import com.example.bloodaid.databinding.FragmentHomeBinding
import com.example.bloodaid.databinding.FragmentLoginBinding
import com.example.bloodaid.util.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var auth: FirebaseAuth
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: com.google.android.material.tabs.TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.tab_titles)[position]
        }.attach()

        auth = Firebase.auth
        val userId = auth.currentUser?.uid

        if(userId!= null){
            firestore.collection("users").document(userId).get().addOnSuccessListener {document->
                if(document!=null){
                    val gender = document.getString("gender")

                    if (gender == "Man") {
                        binding.profileImage.setImageResource(R.drawable.man)
                    } else if (gender == "Woman") {
                        binding.profileImage.setImageResource(R.drawable.woman)
                    }

                }else {
                    Log.d(ContentValues.TAG, "No such document")
                }

            } .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)

            }
        }else {
            Log.d(ContentValues.TAG, "User not logged in")

        }

        binding.profileImage.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment(userId!!)
            findNavController().navigate(action)
        }

        binding.fabAdd.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addFragment)
        }


    }
}