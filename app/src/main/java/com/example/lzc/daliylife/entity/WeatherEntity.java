package com.example.lzc.daliylife.entity;

import java.util.ArrayList;

/**
 * Created by lzc on 2017/3/10.
 */

public class WeatherEntity {
    String msg;
    String retCode;
    ArrayList<Result> result;

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "msg='" + msg + '\'' +
                ", retCode='" + retCode + '\'' +
                ", result=" + result +
                '}';
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public ArrayList<Result> getresult() {
        return result;
    }

    public void setresult(ArrayList<Result> result) {
        this.result = result;
    }

    public class Result {
        String airCondition;
        String city;
        String coldIndex;

        @Override
        public String toString() {
            return "Result{" +
                    "airCondition='" + airCondition + '\'' +
                    ", city='" + city + '\'' +
                    ", coldIndex='" + coldIndex + '\'' +
                    ", date='" + date + '\'' +
                    ", distrct='" + distrct + '\'' +
                    ", dressingIndex='" + dressingIndex + '\'' +
                    ", exerciseIndex='" + exerciseIndex + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", pollutionIndex='" + pollutionIndex + '\'' +
                    ", province='" + province + '\'' +
                    ", sunrise='" + sunrise + '\'' +
                    ", sunset='" + sunset + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", time='" + time + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", washIndex='" + washIndex + '\'' +
                    ", weather='" + weather + '\'' +
                    ", week='" + week + '\'' +
                    ", wind='" + wind + '\'' +
                    ", future=" + future +
                    '}';
        }

        public String getAirCondition() {
            return airCondition;
        }

        public void setAirCondition(String airCondition) {
            this.airCondition = airCondition;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getColdIndex() {
            return coldIndex;
        }

        public void setColdIndex(String coldIndex) {
            this.coldIndex = coldIndex;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDistrct() {
            return distrct;
        }

        public void setDistrct(String distrct) {
            this.distrct = distrct;
        }

        public String getDressingIndex() {
            return dressingIndex;
        }

        public void setDressingIndex(String dressingIndex) {
            this.dressingIndex = dressingIndex;
        }

        public String getExerciseIndex() {
            return exerciseIndex;
        }

        public void setExerciseIndex(String exerciseIndex) {
            this.exerciseIndex = exerciseIndex;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPollutionIndex() {
            return pollutionIndex;
        }

        public void setPollutionIndex(String pollutionIndex) {
            this.pollutionIndex = pollutionIndex;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWashIndex() {
            return washIndex;
        }

        public void setWashIndex(String washIndex) {
            this.washIndex = washIndex;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public ArrayList<Future> getfuture() {
            return future;
        }

        public void setfuture(ArrayList<Future> future) {
            this.future = future;
        }

        String date;
        String distrct;
        String dressingIndex;
        String exerciseIndex;
        String humidity;
        String pollutionIndex;
        String province;
        String sunrise;
        String sunset;
        String temperature;
        String time;
        String updateTime;
        String washIndex;
        String weather;
        String week;
        String wind;
        ArrayList<Future> future;

        public class Future {
            @Override
            public String toString() {
                return "Future{" +
                        "date='" + date + '\'' +
                        ", dayTime='" + dayTime + '\'' +
                        ", night='" + night + '\'' +
                        ", temperature='" + temperature + '\'' +
                        ", week='" + week + '\'' +
                        ", wind='" + wind + '\'' +
                        '}';
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDayTime() {
                return dayTime;
            }

            public void setDayTime(String dayTime) {
                this.dayTime = dayTime;
            }

            public String getNight() {
                return night;
            }

            public void setNight(String night) {
                this.night = night;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }

            String date;
            String dayTime;
            String night;
            String temperature;
            String week;
            String wind;
        }
    }
}
