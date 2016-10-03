
package com.example.scrollinglist.pojorec;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Params {

    @SerializedName("q")
    @Expose
    private String q;
    @SerializedName("indent")
    @Expose
    private String indent;
    @SerializedName("fq")
    @Expose
    private String fq;
    @SerializedName("wt")
    @Expose
    private String wt;

    /**
     * 
     * @return
     *     The q
     */
    public String getQ() {
        return q;
    }

    /**
     * 
     * @param q
     *     The q
     */
    public void setQ(String q) {
        this.q = q;
    }

    /**
     * 
     * @return
     *     The indent
     */
    public String getIndent() {
        return indent;
    }

    /**
     * 
     * @param indent
     *     The indent
     */
    public void setIndent(String indent) {
        this.indent = indent;
    }

    /**
     * 
     * @return
     *     The fq
     */
    public String getFq() {
        return fq;
    }

    /**
     * 
     * @param fq
     *     The fq
     */
    public void setFq(String fq) {
        this.fq = fq;
    }

    /**
     * 
     * @return
     *     The wt
     */
    public String getWt() {
        return wt;
    }

    /**
     * 
     * @param wt
     *     The wt
     */
    public void setWt(String wt) {
        this.wt = wt;
    }

}
