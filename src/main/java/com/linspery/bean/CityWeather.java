package com.linspery.bean;

/**
 * Created by Linspery on 15/12/4.
 */
public class CityWeather {
    private String city;
    private String wendu;
    private String ganmao;
    private Forecast[] forecast;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public Forecast[] getForecast() {
        return forecast;
    }

    public void setForecast(Forecast[] forecast) {
        this.forecast = forecast;
    }
}
