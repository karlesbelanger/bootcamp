
package com.example.scrollinglist.pojorec;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseHeader {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("QTime")
    @Expose
    private Integer qTime;
    @SerializedName("params")
    @Expose
    private Params params;

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The qTime
     */
    public Integer getQTime() {
        return qTime;
    }

    /**
     * 
     * @param qTime
     *     The QTime
     */
    public void setQTime(Integer qTime) {
        this.qTime = qTime;
    }

    /**
     * 
     * @return
     *     The params
     */
    public Params getParams() {
        return params;
    }

    /**
     * 
     * @param params
     *     The params
     */
    public void setParams(Params params) {
        this.params = params;
    }

}
