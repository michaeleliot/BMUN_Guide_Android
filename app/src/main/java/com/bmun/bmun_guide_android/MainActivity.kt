package com.bmun.bmun_guide_android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import org.json.JSONException



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FuelManager.instance.basePath = "https://api.mlab.com"
        val committeeList: ArrayList<String> = ArrayList()

        getCommittees(committeeList)

        recyclerview_committees.layoutManager = LinearLayoutManager(this)
        recyclerview_committees.adapter = CommitteeAdapter(committeeList, this)

    }

    fun getCommittees(list: ArrayList<String>?) {
        try {
            Fuel.get("/api/1/databases/bmunguide/collections/BMUN?apiKey=JI0kCishO2bE688ivZhIUl-bv-UJ3bKg").responseJson { request, response, result ->

                val jsonArray = JSONArray(result.get().content)
                val jsonObj = jsonArray[0] as JSONObject
                val committees = jsonObj.getJSONObject("Committee")
                val committeeIter = committees.keys()
                while (committeeIter.hasNext()) {
                    val ckey = committeeIter.next()
                    try {
                        val bloc = committees.getJSONObject(ckey)

                        val blocIter = bloc.keys()
                        while (blocIter.hasNext()) {
                            val bkey = blocIter.next()
                            try {
                                val committee = bloc.getJSONObject(bkey)

                                val name = committee.getString("name")

                                list?.add(name)
                            } catch (e: JSONException) {
                                // Something went wrong!
                            }

                        }
                    } catch (e: JSONException) {
                        // Something went wrong!
                    }

                }
                recyclerview_committees.adapter.notifyDataSetChanged()

            }
        } catch (e: Exception) {
            System.out.println(e.message)
        }
    }
}
