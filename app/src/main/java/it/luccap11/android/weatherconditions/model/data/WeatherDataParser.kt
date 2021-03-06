package it.luccap11.android.weatherconditions.model.data

import android.util.Log
import androidx.annotation.NonNull
import org.json.JSONException
import org.json.JSONObject

/**
 * @author Luca Capitoli
 */
object WeatherDataParser {
    private const val NUM_OF_DETECTIONS_PER_DAY = 8

    fun getWeatherLiveData(@NonNull json: JSONObject): List<WeatherData> {
        val result = mutableListOf<WeatherData>()

        try {
            val listOfDays = json.getJSONArray("list")
            for (i in 0 until listOfDays.length() step NUM_OF_DETECTIONS_PER_DAY) {
                val dayWeatherData = listOfDays.getJSONObject(i)
                val weatherObject = dayWeatherData.getJSONArray("weather").getJSONObject(0)
                val descr = weatherObject.getString("main")
                val date = dayWeatherData.getString("dt_txt")
                val icon = weatherObject.getString("icon")
                val temp = dayWeatherData.getJSONObject("main").getDouble("temp")
                result.add(WeatherData(descr, temp.toFloat(), date, icon))
            }
        } catch (exception: JSONException) {
            Log.e("TAG", exception.toString())
        } finally {
            return result
        }
    }
}