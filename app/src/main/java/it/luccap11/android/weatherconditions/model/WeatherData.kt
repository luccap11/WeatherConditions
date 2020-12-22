package it.luccap11.android.weatherconditions.model

/**
 * @author Luca Capitoli
 */
data class WeatherData(
    var descr: String,
    var temp: Float,
    var date: String,
    var icon: String
) {

}