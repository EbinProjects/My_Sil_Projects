package com.example.jskotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.jskotlin.R
import com.example.jskotlin.adapter.DataNodel
import com.example.jskotlin.adapter.RetrofitApi
import kotlinx.android.synthetic.main.fragment_tab_second.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TabSecondFragment:Fragment(),View.OnClickListener  {
    private var nameEdt: EditText? = null
    private  var jobEdt:EditText? = null
    private var postDataBtn: Button? = null
    private var responseTV: TextView? = null
    private var loadingPB: ProgressBar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_tab_second,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameEdt =idEdtName
        jobEdt = idEdtJob
        postDataBtn = idBtnPost
        responseTV = idTVResponse
        loadingPB = idLoadingPB
        postDataBtn?.setOnClickListener(this);
//        postDataBtn.setOnClickListener(View.OnClickListener {
//            if (nameEdt?.text.toString().isEmpty() && jobEdt?.text.toString().isEmpty()) {
//                Toast.makeText(requireContext(), "Please enter both the values", Toast.LENGTH_SHORT).show();
//
//            }
//            // calling a method to post the data and passing our name and job.
//            postData(nameEdt?.text.toString(), jobEdt?.text.toString());
//        })
    }

    private fun postData(name: String, job: String) {
        loadingPB?.visibility = View.VISIBLE;
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/") // as we are sending data in json format so
            // we have to add Gson converter factory
            .addConverterFactory(GsonConverterFactory.create()) // at last we are building our retrofit builder.
            .build()
        val retrofit3: RetrofitApi = retrofit.create(RetrofitApi::class.java)
        val modal = DataNodel(name, job)
        retrofit3.getCategories(modal)?.enqueue(object : Callback<DataNodel?> {
            override fun onResponse(call: Call<DataNodel?>?, response: Response<DataNodel?>) {
                // this method is called when we get response from our api.
                Toast.makeText(requireContext(), "Data added to API", Toast.LENGTH_SHORT).show()

                // below line is for hiding our progress bar.
                loadingPB!!.visibility = View.GONE

                // on below line we are setting empty text
                // to our both edit text.
                jobEdt!!.setText("")
                nameEdt!!.setText("")

                // we are getting response from our body
                // and passing it to our modal class.
                val responseFromAPI: DataNodel? = response.body()

                // on below line we are getting our data from modal class and adding it to our string.
                val responseString = """
                     Response Code : ${response.code().toString()}
                     Name : ${responseFromAPI?.name}
                     Job : ${responseFromAPI?.job}
                     """.trimIndent()

                // below line we are setting our
                // string to our text view.
                responseTV!!.text = responseString
            }

            override fun onFailure(call: Call<DataNodel?>?, t: Throwable) {
                // setting text to our text view when
                // we get error response from API.
                responseTV!!.text = "Error found is : " + t.message
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.idBtnPost -> {
                if (nameEdt?.text.toString().isEmpty() && jobEdt?.text.toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter both the values", Toast.LENGTH_SHORT).show();

                }
                // calling a method to post the data and passing our name and job.
                postData(nameEdt?.text.toString(), jobEdt?.text.toString());

            }

        }
    }
}