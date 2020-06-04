package com.mohit.BookAppV2.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mohit.BookAppV2.R

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {
    lateinit var sharedPreferences:SharedPreferences
    lateinit var etNameProfile:TextView
    lateinit var etEmailProfile:TextView
    lateinit var etMobileProfile:TextView
    lateinit var etAddressProfile:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreferences =
            this.activity!!.getSharedPreferences(getString(R.string.Preferences_file_name), Context.MODE_PRIVATE)
        val Name=sharedPreferences.getString("name","Mohit")
        val Email=sharedPreferences.getString("email","Mohit")
        val Address=sharedPreferences.getString("address","Mohit")
        val Mobile_Number=sharedPreferences.getString("MobileNumber","Mohit")
        etNameProfile=view.findViewById(R.id.etNameProfile)
        etEmailProfile=view.findViewById(R.id.etEmailProfile)
        etAddressProfile=view.findViewById(R.id.etAddressProfile)
        etMobileProfile=view.findViewById(R.id.etMobileProfile)
        etMobileProfile.text=Mobile_Number
        etNameProfile.text=Name
        etAddressProfile.text=Address
        etEmailProfile.text=Email


        return view
    }

}

