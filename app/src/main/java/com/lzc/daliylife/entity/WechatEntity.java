package com.lzc.daliylife.entity;

import java.util.ArrayList;

/**
 * Created by lzc on 2017/3/20.
 */

public class WechatEntity {
    String reason;
    String error_code;
    Result result;

    @Override
    public String toString() {
        return "WechatEntity{" +
                "reason='" + reason + '\'' +
                ", error_code='" + error_code + '\'' +
                ", result=" + result +
                '}';
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        String totalPage;
        String ps;//每页返回条数，最大50，默认20
        String pno;	//当前页数，默认1
        ArrayList<ResultDetail> list;

        @Override
        public String toString() {
            return "Result{" +
                    "totalPage='" + totalPage + '\'' +
                    ", ps='" + ps + '\'' +
                    ", pno='" + pno + '\'' +
                    ", list=" + list +
                    '}';
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getPs() {
            return ps;
        }

        public void setPs(String ps) {
            this.ps = ps;
        }

        public String getPno() {
            return pno;
        }

        public void setPno(String pno) {
            this.pno = pno;
        }

        public ArrayList<ResultDetail> getList() {
            return list;
        }

        public void setList(ArrayList<ResultDetail> list) {
            this.list = list;
        }

        public class ResultDetail {
            String id;
            String title;
            String source;
            String firstImg;
            String mark;
            String url;

            @Override
            public String toString() {
                return "ResultDetail{" +
                        "id='" + id + '\'' +
                        ", title='" + title + '\'' +
                        ", source='" + source + '\'' +
                        ", firstImg='" + firstImg + '\'' +
                        ", mark='" + mark + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getFirstImg() {
                return firstImg;
            }

            public void setFirstImg(String firstImg) {
                this.firstImg = firstImg;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
