package it.luccap11.android.weatherconditions.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.luccap11.android.weatherconditions.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The ViewModel triggers the network request when the user clicks, for example, on a button
 * @author Luca Capitoli
 */
class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    val liveData = MutableLiveData<Resource<List<WeatherData>>>()

    fun getData(selectedCity: String) {
        liveData.postValue(Resource.Loading())
        if (selectedCity.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                val resource = getApplication<Application>().resources
                val url = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&APPID=%s", selectedCity,
                    resource.getString(R.string.owm_api_key))
                val requestQueue = Volley.newRequestQueue(getApplication())
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                    val listOfDays = response.getJSONArray("list")
                    val result = arrayListOf<WeatherData>()
                    for (i in 0 until listOfDays.length() step 8) {
                        val descr = listOfDays.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main")
                        val date = listOfDays.getJSONObject(i).getString("dt_txt")
                        val icon = listOfDays.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon")
                        val temp = listOfDays.getJSONObject(i).getJSONObject("main").getDouble("temp")
                        result.add(WeatherData(descr, temp.toFloat(), date, icon))
                    }

                    liveData.postValue(Resource.Success(result))
                    }) { error ->
                        Log.e("TAG", error.toString())
                        liveData.postValue(Resource.Error(resource.getString(R.string.error_label)))
                    }

                requestQueue.add(jsonObjectRequest)
            }
        }
    }
}