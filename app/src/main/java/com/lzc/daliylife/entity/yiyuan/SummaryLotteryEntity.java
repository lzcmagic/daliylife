package com.lzc.daliylife.entity.yiyuan;

import java.util.List;

/**
 * Created by lzc on 2017/9/12.
 */

public class SummaryLotteryEntity {
    /**
     * showapi_res_code : 0
     * showapi_res_error :
     * showapi_res_body : {"result":[{"timestamp":1505046700,"expect":"2017106","time":"2017-09-10 20:31:40","name":"七星彩","code":"qxc","openCode":"6,7,1,6,5,2,8"},{"timestamp":1505133120,"expect":"2017247","time":"2017-09-11 20:32:00","name":"排列3","code":"pl3","openCode":"9,4,5"},{"timestamp":1505135950,"expect":"2017106","time":"2017-09-11 21:19:10","name":"七乐彩","code":"qlc","openCode":"01,03,11,17,23,25,28+04"},{"timestamp":1505049500,"expect":"2017106","time":"2017-09-10 21:18:20","name":"双色球","code":"ssq","openCode":"12,15,20,25,27,31+02"},{"timestamp":1505136000,"expect":"2017247","time":"2017-09-11 21:20:00","name":"福彩3d","code":"fc3d","openCode":"3,7,0"},{"timestamp":1505133200,"expect":"2017106","time":"2017-09-11 20:33:20","name":"超级大乐透","code":"dlt","openCode":"09,12,18,23,29+02,04"},{"timestamp":1505133120,"expect":"2017247","time":"2017-09-11 20:32:00","name":"排列5","code":"pl5","openCode":"9,4,5,3,2"}],"ret_code":0}
     */

    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiResBodyBean showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {
        /**
         * result : [{"timestamp":1505046700,"expect":"2017106","time":"2017-09-10 20:31:40","name":"七星彩","code":"qxc","openCode":"6,7,1,6,5,2,8"},{"timestamp":1505133120,"expect":"2017247","time":"2017-09-11 20:32:00","name":"排列3","code":"pl3","openCode":"9,4,5"},{"timestamp":1505135950,"expect":"2017106","time":"2017-09-11 21:19:10","name":"七乐彩","code":"qlc","openCode":"01,03,11,17,23,25,28+04"},{"timestamp":1505049500,"expect":"2017106","time":"2017-09-10 21:18:20","name":"双色球","code":"ssq","openCode":"12,15,20,25,27,31+02"},{"timestamp":1505136000,"expect":"2017247","time":"2017-09-11 21:20:00","name":"福彩3d","code":"fc3d","openCode":"3,7,0"},{"timestamp":1505133200,"expect":"2017106","time":"2017-09-11 20:33:20","name":"超级大乐透","code":"dlt","openCode":"09,12,18,23,29+02,04"},{"timestamp":1505133120,"expect":"2017247","time":"2017-09-11 20:32:00","name":"排列5","code":"pl5","openCode":"9,4,5,3,2"}]
         * ret_code : 0
         */

        private int ret_code;
        private List<ResultBean> result;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * timestamp : 1505046700
             * expect : 2017106
             * time : 2017-09-10 20:31:40
             * name : 七星彩
             * code : qxc
             * openCode : 6,7,1,6,5,2,8
             */

            private String timestamp;
            private String expect;
            private String time;
            private String name;
            private String code;
            private String openCode;

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getExpect() {
                return expect;
            }

            public void setExpect(String expect) {
                this.expect = expect;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getOpenCode() {
                return openCode;
            }

            public void setOpenCode(String openCode) {
                this.openCode = openCode;
            }
        }
    }
}
