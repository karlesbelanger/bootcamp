package com.example.scrollinglist.video;

import com.example.scrollinglist.pojorec.RecommandationResponse;


/**
 * Created by belangek on 9/22/16.
 */

public interface RecommandationListener {
        void onSuccess(RecommandationResponse data);

        void onFailure(String errorMsg);
}
