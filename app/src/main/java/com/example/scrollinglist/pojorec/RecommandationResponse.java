
package com.example.scrollinglist.pojorec;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecommandationResponse {

    @SerializedName("responseHeader")
    @Expose
    private ResponseHeader responseHeader;
    @SerializedName("response")
    @Expose
    private Response response;

    /**
     * 
     * @return
     *     The responseHeader
     */
    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    /**
     * 
     * @param responseHeader
     *     The responseHeader
     */
    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    /**
     * 
     * @return
     *     The response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * 
     * @param response
     *     The response
     */
    public void setResponse(Response response) {
        this.response = response;
    }

}
