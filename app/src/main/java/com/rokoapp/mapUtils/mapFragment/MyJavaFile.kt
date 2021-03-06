package com.rokoapp.mapUtils.mapFragment

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.oneclickaway.opensource.placeautocomplete.api.bean.place_details.PlaceDetails
import com.oneclickaway.opensource.placeautocomplete.components.SearchPlacesStatusCodes
import com.oneclickaway.opensource.placeautocomplete.ui.SearchPlaceActivity
import com.rokoapp.R

class MyJavaFile : AppCompatActivity() {

    lateinit var searchLocationET: EditText
    lateinit var placeDetailsTV: TextView


    var API_KEY = "AIzaSyAsOByI5ByaQCyzDqNyKyNZ4tmuXaDxW2E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_location_search)

        searchLocationET = this.findViewById(R.id.searchLocationBTN)
        placeDetailsTV = this.findViewById(R.id.resultPlaceDetailsTV)

        val intent = Intent(this, SearchPlaceActivity::class.java)
        intent.putExtra(
                SearchPlacesStatusCodes.CONFIG,
                SearchPlaceActivity.Config(
                        apiKey = API_KEY,
                        searchBarTitle = "Enter Source Location",
                        location = "12.9716,77.5946",
                        enclosingRadius = "500"
                )
        )

        searchLocationET.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                /*val pair = Pair.create(searchLocationET as View, SearchPlacesStatusCodes.PLACEHOLDER_TRANSITION)
                val options = ActivityOptions.makeSceneTransitionAnimation(this, pair).toBundle()*/
                startActivityForResult(intent, 700)

            } else {
                startActivityForResult(intent, 700)
//                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 700 && resultCode == Activity.RESULT_OK) {

            val placeDetails = data?.getParcelableExtra<PlaceDetails>(SearchPlacesStatusCodes.PLACE_DATA)
            Toast.makeText(this, placeDetails?.name + "", Toast.LENGTH_LONG).show()
            /*searchLocationET.setText(placeDetails?.name)

            placeDetailsTV.text = placeDetails.toString()*/
            Log.i(javaClass.simpleName, "onActivityResult: ${placeDetails}  ")
        }
    }
}