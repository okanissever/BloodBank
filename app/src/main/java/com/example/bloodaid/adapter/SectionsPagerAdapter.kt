package com.example.bloodaid.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bloodaid.R
import com.example.bloodaid.view.ui.BloodRecipientFragment
import com.example.bloodaid.view.ui.DonorFragment


class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val TAB_TITLES = arrayOf(
        "Blood Recipient",
        "Donor"
    )

    override fun getItemCount(): Int = 2 // Toplam sekme sayısı

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BloodRecipientFragment()
            1 -> DonorFragment()
            else -> throw IllegalStateException("Position not valid")
        }
    }
}
