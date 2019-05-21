package com.rokoapp.mapUtils.mapFragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.oneclickaway.opensource.placeautocomplete.api.bean.place_details.PlaceDetails;
import com.oneclickaway.opensource.placeautocomplete.components.SearchPlacesStatusCodes;
import com.oneclickaway.opensource.placeautocomplete.ui.SearchPlaceActivity;
import com.rokoapp.R;

import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public class KotlinClassCode extends AppCompatActivity{

    public EditText searchLocationET;
    public TextView placeDetailsTV;

    private String API_KEY = "AIzaSyAsOByI5ByaQCyzDqNyKyNZ4tmuXaDxW2E";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_example_location_search);

        searchLocationET = this.findViewById(R.id.searchLocationBTN);
        placeDetailsTV = findViewById(R.id.resultPlaceDetailsTV);

        final Intent intent = new Intent(this, SearchPlaceActivity.class);
        String searchPlacesStatusCodes = SearchPlacesStatusCodes.INSTANCE.getCONFIG();
        String enclosingRadius = "500";
        String location = "12.9716,77.5946";
        String searchBarTitle = "Enter Source Location";
        intent.putExtra(searchPlacesStatusCodes, (Parcelable)(new SearchPlaceActivity.Config(this.API_KEY, location, enclosingRadius, searchBarTitle)));

        searchLocationET.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Pair pair = Pair.create(searchLocationET, SearchPlacesStatusCodes.INSTANCE.getPLACEHOLDER_TRANSITION());
                    Bundle options = ActivityOptions.makeSceneTransitionAnimation((Activity)KotlinClassCode.this, new Pair[]{pair}).toBundle();
                    startActivityForResult(intent, 700, options);
                } else {
                    startActivityForResult(intent, 700);
                    overridePendingTransition(-14, -13);
                }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 700 && resultCode == -1) {
            PlaceDetails placeDetails = data != null ? (PlaceDetails)data.getParcelableExtra(SearchPlacesStatusCodes.INSTANCE.getPLACE_DATA()) : null;

            searchLocationET.setText((placeDetails != null ? placeDetails.getName() : null));
            TextView var5 = this.placeDetailsTV;
            if (var5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("placeDetailsTV");
            }

            var5.setText(String.valueOf(placeDetails));
            Log.i(this.getClass().getSimpleName(), "onActivityResult: " + placeDetails + "  ");
        }

    }
}