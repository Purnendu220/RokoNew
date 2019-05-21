package com.rokoapp.mapUtils.mapFragment


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.jakewharton.rxbinding2.widget.RxTextView
import com.oneclickaway.opensource.placeautocomplete.api.bean.places_response.PredictionsItem
import com.oneclickaway.opensource.placeautocomplete.components.SearchPlacesStatusCodes
import com.oneclickaway.opensource.placeautocomplete.components.SearchPlacesViewModel
import com.oneclickaway.opensource.placeautocomplete.interfaces.PlaceClickListerner
import com.oneclickaway.opensource.placeautocomplete.ui.SearchPlaceActivity
import com.oneclickaway.opensource.placeautocomplete.ui.SearchResultAdapter
import com.rokoapp.R
import com.rokoapp.adapter.SearchHistoryAdapter
import com.rokoapp.dbHelper.RokoDbAdapter
import com.rokoapp.mapUtils.responseModel.SearchHistoryModel
import com.rokoapp.utils.ParamUtils.*
import com.rokoapp.utils.SaveCache
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class MyRouteSearch : AppCompatActivity(), PlaceClickListerner, View.OnClickListener {

    private lateinit var viewModel: SearchPlacesViewModel
    private var compositeDisposable = CompositeDisposable()
    private lateinit var searchListAdapter: SearchResultAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private var locationList = ArrayList<SearchHistoryModel>()

    private lateinit var rokoDbAdapter: RokoDbAdapter

    private var apiKey: String? = null
    private var location: String? = null
    private var enclosingRadius: String? = null
    var codeType: String? = null
    var from: String? = null

    private var home: String? = null
    private var office: String? = null


    /*view*/

    lateinit var searchTitleTV: TextView
    lateinit var searchProgressBar: ProgressBar
    lateinit var noPlacesFoundLL: LinearLayout
    lateinit var placeNamET: EditText
    lateinit var searchResultsRV: RecyclerView
    lateinit var backImageBtn: ImageButton

    /*my views*/
    lateinit var cardCurrentLocation: CardView
    lateinit var homeLLayout: LinearLayout
    lateinit var workLLayout: LinearLayout
    lateinit var layoutExtra: LinearLayout
    lateinit var recyclerRecentSearch: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_route_search)
        inflateViews()

        initializeDependency()

        setViewModel()

        setOnClickListeners()

        setRecyclerView()

        setOnQueryChangeListener()

        attachLiveObservers()

        if (from.equals("SuggestNew"))
            layoutExtra.visibility = View.GONE
        else
            layoutExtra.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        val homeAddress = SaveCache.getString(this@MyRouteSearch, HOME_ADDRESS)
        val officeAddress = SaveCache.getString(this@MyRouteSearch, OFFICE_ADDRESS)
        if (homeAddress != null && homeAddress.isNotEmpty()){
            homeLLayout.visibility = View.VISIBLE
        }else
            homeLLayout.visibility = View.GONE
        if (officeAddress != null && officeAddress.isNotEmpty()){
            workLLayout.visibility = View.VISIBLE
        }else
            workLLayout.visibility = View.GONE

    }

    private fun inflateViews() {
        rokoDbAdapter = RokoDbAdapter(this)

        /*Inflate my views*/
        cardCurrentLocation = findViewById(R.id.cardCurrentLocation)
        homeLLayout = findViewById(R.id.homeLLayout)
        workLLayout = findViewById(R.id.workLLayout)
        layoutExtra = findViewById(R.id.myExtraLayout)
        recyclerRecentSearch = findViewById(R.id.recyclerRecentSearch)

        /*inflate Views */
        searchTitleTV = findViewById(R.id.searchTitleTV)
        searchProgressBar = findViewById(R.id.searchProgressBar)
        noPlacesFoundLL = findViewById(R.id.noPlacesFoundLL)
        placeNamET = findViewById(R.id.placeNamET)
        searchResultsRV = findViewById(R.id.searchResultsRV)
        backImageBtn = findViewById(R.id.backImageBtn)

    }

    private fun setOnClickListeners() {
        backImageBtn.setOnClickListener(this)
        cardCurrentLocation.setOnClickListener(this)
        homeLLayout.setOnClickListener(this)
        workLLayout.setOnClickListener(this)
    }

    private fun initializeDependency() {


        if (intent.hasExtra(SearchPlacesStatusCodes.CONFIG)) {

            val configuration = intent.extras?.getParcelable<SearchPlaceActivity.Config>(SearchPlacesStatusCodes.CONFIG)
            apiKey = configuration?.apiKey
            location = configuration?.location
            enclosingRadius = configuration?.enclosingRadius
            searchTitleTV.text = configuration?.searchBarTitle
            codeType = intent.getStringExtra("codeType")
            from = intent.getStringExtra("from")

        } else {
            /*finish*/
            Toast.makeText(this, "Please mention the api key in put-extra", Toast.LENGTH_LONG).show()
            finish()
        }

    }

    private fun attachLiveObservers() {

        viewModel.getLiveListOfSearchResultsStream().observe(this, Observer {
            //              refresh the adapter here
            searchProgressBar.visibility = View.GONE
            searchListAdapter.setSearchCandidates(it)
            if (it?.size == 0) {
                if (placeNamET.text.toString().isNotEmpty()) {
                    noPlacesFoundLL.visibility = View.VISIBLE
                    Log.i(javaClass.simpleName, "attachLiveObservers: List is empty!")
                } else {
                    noPlacesFoundLL.visibility = View.GONE
                }

            } else {
                noPlacesFoundLL.visibility = View.GONE
                Log.i(javaClass.simpleName, "attachLiveObservers: List has contents!")
            }
        })



        viewModel.getPlaceDetailsLiveDataStream().observe(this, Observer {
            searchProgressBar.visibility = View.GONE
            Log.d(javaClass.simpleName, "attachLiveObservers:  ${it?.geometry?.location?.lat} $it ")
            val resultData = Intent()
            resultData.putExtra(SearchPlacesStatusCodes.PLACE_DATA, it)
            setResult(Activity.RESULT_OK, resultData)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
                overridePendingTransition(com.oneclickaway.opensource.placeautocomplete.R.anim.abc_fade_in, com.oneclickaway.opensource.placeautocomplete.R.anim.abc_fade_out)
            }

        })

    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchPlacesViewModel::class.java)
    }

    private fun setRecyclerView() {

        searchResultsRV.layoutManager = LinearLayoutManager(this)
        searchListAdapter = SearchResultAdapter(placeClickListerner = this)
        searchResultsRV.adapter = searchListAdapter


        recyclerRecentSearch.layoutManager = LinearLayoutManager(this )
        locationList = rokoDbAdapter.readSearchHistory()
//        locationList.reverse()
        searchHistoryAdapter = SearchHistoryAdapter(this, locationList, /*rokoDbAdapter,*/ this )
        recyclerRecentSearch.adapter = searchHistoryAdapter


    }

    private fun setOnQueryChangeListener() {
        compositeDisposable.add(

                RxTextView.textChanges(placeNamET)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .filter {

                            runOnUiThread {
                                if (it.toString().isNotBlank()) {
                                    layoutExtra.visibility = View.GONE
                                    searchResultsRV.visibility = View.VISIBLE
                                }else {
                                    if (from.equals("SuggestNew"))
                                        layoutExtra.visibility = View.GONE
                                    else
                                        layoutExtra.visibility = View.VISIBLE
                                        searchResultsRV.visibility = View.GONE
                                }
                            }

                            it.toString().isNotBlank()
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribeWith(object : DisposableObserver<CharSequence?>() {
                            override fun onComplete() {
                            }

                            override fun onNext(t: CharSequence) {
                                Log.d(javaClass.simpleName, "setOnQueryChangeListener: ${t}")
                                searchProgressBar.visibility = View.VISIBLE
                                viewModel.requestListOfSearchResults(
                                        placeHint = t.toString(),
                                        apiKey = apiKey!!,
                                        location = location ?: "",
                                        radius = enclosingRadius ?: "500"
                                )

                            }

                            override fun onError(e: Throwable) {

                            }

                        })
        )

    }

    override fun onPlaceClicked(candidateItem: PredictionsItem?) {

        searchProgressBar.visibility = View.VISIBLE
        viewModel.requestPlaceDetails(candidateItem?.placeId.toString(), apiKey = apiKey!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
        compositeDisposable.clear()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(com.oneclickaway.opensource.placeautocomplete.R.anim.abc_fade_in, com.oneclickaway.opensource.placeautocomplete.R.anim.abc_fade_out)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cardCurrentLocation -> {
//                selectCurrentLocation()
            }
            R.id.homeLLayout -> {
                val homeAddress = SaveCache.getString(this@MyRouteSearch, HOME_ADDRESS)
                if (homeAddress != null && homeAddress.isNotEmpty() && !homeAddress.equals("")){
                    val homeLatLong = SaveCache.getString(this@MyRouteSearch, HOME_LAT_LONG)
                    val values = homeLatLong.split(",")
                    homeLatitude = java.lang.Double.parseDouble(values[0])
                    homeLongitude = java.lang.Double.parseDouble(values[1])
                    homeLocation = homeAddress
                    sourceName = homeLocation
                    finish();
                }
            }
            R.id.workLLayout -> {
                val officeAddress = SaveCache.getString(this@MyRouteSearch, OFFICE_ADDRESS)
                if (officeAddress != null && officeAddress.isNotEmpty() && !officeAddress.equals("")){
                    val officeLatLong = SaveCache.getString(this@MyRouteSearch, OFFICE_LAT_LONG)
                    Log.d("officeAddress","========================"+officeLatLong)

                    val valueOffice = officeLatLong.split(",")
                    officeLatitude = java.lang.Double.parseDouble(valueOffice[0])
                    officeLongitude = java.lang.Double.parseDouble(valueOffice[1])
                    officeLocation = officeAddress
                    destName = officeLocation
                    finish()
                }
            }
            R.id.backImageBtn -> {
                onBackPressed()
            }
        }
    }

    private fun selectCurrentLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}