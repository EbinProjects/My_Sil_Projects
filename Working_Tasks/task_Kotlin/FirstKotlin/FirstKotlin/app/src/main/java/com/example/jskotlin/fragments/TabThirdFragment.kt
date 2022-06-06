package com.example.jskotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jskotlin.DataModel
import com.example.jskotlin.R
import com.example.jskotlin.adapter.DataNodel
import com.example.jskotlin.adapter.getRetrofit
import kotlinx.android.synthetic.main.fragment_tab_third.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level

class TabThirdFragment:Fragment() {
    private var loadingPB: ProgressBar? = null
    private var desc: TextView? = null
    private  var charNames: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_tab_third,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingPB=idLoadingPB1
            desc= idTVResponse1
        charNames=idTVResponse2
        LodaData();
    }

    private fun LodaData() {
        loadingPB?.visibility = View.VISIBLE;
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coindesk.com/v1/bpi/") // as we are sending data in json format so
            // we have to add Gson converter factory
            .addConverterFactory(GsonConverterFactory.create()) // at last we are building our retrofit builder.
            .build()
        val retrofit3: getRetrofit = retrofit.create(getRetrofit::class.java)
        val modal = DataModel()
        retrofit3.getPDFs(modal)?.enqueue(object : Callback<DataModel?> {
            override fun onResponse(call: Call<DataModel?>?, response: Response<DataModel?>) {
                // this method is called when we get response from our api.
                Toast.makeText(requireContext(), "Data added to API", Toast.LENGTH_SHORT).show()

                // below line is for hiding our progress bar.
                loadingPB!!.visibility = View.GONE

                // on below line we are setting empty text
                // to our both edit text.
                desc!!.text = ""
                charNames!!.text = ""

                // we are getting response from our body
                // and passing it to our modal class.
                val responseFromAPI: DataModel? = response.body()

                // on below line we are getting our data from modal class and adding it to our string.

                // below line we are setting our
                // string to our text view.
                desc!!.text ="Name : ${responseFromAPI?.chartName}"
                charNames!!.text ="Name : ${responseFromAPI?.disclaimer}"
            }

            override fun onFailure(call: Call<DataModel?>?, t: Throwable) {
                // setting text to our text view when
                // we get error response from API.
                Toast.makeText(requireActivity(),"adchu poi guys",Toast.LENGTH_SHORT).show()
//                responseTV!!.text = "Error found is : " + t.message
            }
        })
    }
}