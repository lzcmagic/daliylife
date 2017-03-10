package com.example.lzc.daliylife.entity;

/**
 * Created by lzc on 2017/3/10.
 */

public class LaoHuangLiEntity {
    String msg;
    String retCode;
    LaoHuangLiEntity.Result result;

    @Override
    public String toString() {
        return "LaoHuangliEntity{" +
                "msg='" + msg + '\'' +
                ", retCode='" + retCode + '\'' +
                ", result=" + result +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public LaoHuangLiEntity.Result getResult() {
        return result;
    }

    public void setResult(LaoHuangLiEntity.Result result) {
        this.result = result;
    }

    class Result{
        String avoid;
        String date;
        String jishen;
        String lunar;
        String suit;
        String xiongshen;

        @Override
        public String toString() {
            return "Result{" +
                    "avoid='" + avoid + '\'' +
                    ", date='" + date + '\'' +
                    ", jishen='" + jishen + '\'' +
                    ", lunar='" + lunar + '\'' +
                    ", suit='" + suit + '\'' +
                    ", xiongshen='" + xiongshen + '\'' +
                    '}';
        }

        public String getAvoid() {
            return avoid;
        }

        public void setAvoid(String avoid) {
            this.avoid = avoid;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getJishen() {
            return jishen;
        }

        public void setJishen(String jishen) {
            this.jishen = jishen;
        }

        public String getLunar() {
            return lunar;
        }

        public void setLunar(String lunar) {
            this.lunar = lunar;
        }

        public String getSuit() {
            return suit;
        }

        public void setSuit(String suit) {
            this.suit = suit;
        }

        public String getXiongshen() {
            return xiongshen;
        }

        public void setXiongshen(String xiongshen) {
            this.xiongshen = xiongshen;
        }
    }
}
