package com.lzc.daliylife.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lzc on 2017/3/18.
 */

public class LotteryEntity implements Serializable{
    String retCode;//返回码
    String msg;//返回说明
    Result result;//返回结果集

    @Override
    public String toString() {
        return "LotteryEntity{" +
                "retCode='" + retCode + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{
        String awardDateTime;//
        String name;//彩种
        String period;//期次
        String pool;//奖池金额
        String sales;//销售金额
        ArrayList<String> lotteryNumber;//开奖号码
        ArrayList<LotteryDetail> lotteryDetails;//中奖信息

        @Override
        public String toString() {
            return "Result{" +
                    "awardDateTime='" + awardDateTime + '\'' +
                    ", name='" + name + '\'' +
                    ", period='" + period + '\'' +
                    ", pool='" + pool + '\'' +
                    ", sales='" + sales + '\'' +
                    ", lotteryNumber=" + lotteryNumber +
                    ", lotteryDetails=" + lotteryDetails +
                    '}';
        }

        public String getAwardDateTime() {
            return awardDateTime;
        }

        public void setAwardDateTime(String awardDateTime) {
            this.awardDateTime = awardDateTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getPool() {
            return pool;
        }

        public void setPool(String pool) {
            this.pool = pool;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public ArrayList<String> getLotteryNumber() {
            return lotteryNumber;
        }

        public void setLotteryNumber(ArrayList<String> lotteryNumber) {
            this.lotteryNumber = lotteryNumber;
        }

        public ArrayList<LotteryDetail> getLotteryDetails() {
            return lotteryDetails;
        }

        public void setLotteryDetails(ArrayList<LotteryDetail> lotteryDetails) {
            this.lotteryDetails = lotteryDetails;
        }

        public class LotteryDetail implements Serializable{
            String awardNumber;//	中奖注数
            String awardPrice;//中奖金额
            String awards;//奖项
            String type;//奖项类型

            @Override
            public String toString() {
                return "LotteryDetail{" +
                        "awardNumber='" + awardNumber + '\'' +
                        ", awardPrice='" + awardPrice + '\'' +
                        ", awards='" + awards + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }

            public String getAwardNumber() {
                return awardNumber;
            }

            public void setAwardNumber(String awardNumber) {
                this.awardNumber = awardNumber;
            }

            public String getAwardPrice() {
                return awardPrice;
            }

            public void setAwardPrice(String awardPrice) {
                this.awardPrice = awardPrice;
            }

            public String getAwards() {
                return awards;
            }

            public void setAwards(String awards) {
                this.awards = awards;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
