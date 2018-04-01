package com.example.tonycurrie.assignment_3;

public class WeatherDetails {

    public String weather;
    public String degree;
    public String time;

    WeatherDetails(String weather, String degree, String time)
    {
        this.weather = weather;
        this.degree = degree;
        this.time = time;
    }


    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDegree() {
        return degree;
    }

    public String getTime() {
        return time;
    }

    public String getWeather() {
        return weather;
    }
}
