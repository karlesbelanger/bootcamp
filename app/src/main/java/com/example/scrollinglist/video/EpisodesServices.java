package com.example.scrollinglist.video;

import com.example.scrollinglist.pojo.EpisodeResponse;
import com.example.scrollinglist.pojorec.RecommandationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by belangek on 9/22/16.
 */

public interface EpisodesServices {
    @GET("contents/v2/brands/cc/episodes?offset=0&limit=20")
    Call<EpisodeResponse> getEpisodes(@Header("x-api-key") String apiKey);
    @GET("solr/cc/select?fq=contentType_s:Episode&wt=json&indent=true")
    Call<RecommandationResponse> getRecommandation(@Query("q") String q);
}

