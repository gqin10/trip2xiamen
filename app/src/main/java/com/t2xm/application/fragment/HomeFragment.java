package com.t2xm.application.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t2xm.R;
import com.t2xm.dao.ItemDao;
import com.t2xm.dao.ReviewDao;
import com.t2xm.entity.Item;
import com.t2xm.entity.Review;
import com.t2xm.utils.adapter.HomeReviewAdapter;
import com.t2xm.utils.adapter.TopPlacesAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Item> itemList = ItemDao.getItemListByCategory(1);

        if(itemList != null) {
            RecyclerView rv_topPlaces = view.findViewById(R.id.rv_top_places);
            TopPlacesAdapter adapter1 = new TopPlacesAdapter(getContext(), itemList);
            rv_topPlaces.setAdapter(adapter1);
            rv_topPlaces.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        //TODO ViewPager
        ImageView iv_homeAnimation = view.findViewById(R.id.iv_home_animation);
        iv_homeAnimation.setBackgroundResource(R.drawable.animation_list);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) iv_homeAnimation.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();


        List<Review> topReviewList = ReviewDao.get10LatestReview();
        if(topReviewList != null) {
            List<Item> reviewItemList = new ArrayList<>();
            for (Review review : topReviewList) {
                Item item = ItemDao.getItemByItemId(review.itemId) ;
                if(item != null) {
                    reviewItemList.add(item);
                }
            }
            if(reviewItemList.size() > 0) {
                RecyclerView rv = view.findViewById(R.id.rv_latest_reviews);
                HomeReviewAdapter adapter2 = new HomeReviewAdapter(getContext(), topReviewList, reviewItemList);
                rv.setAdapter(adapter2);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    }
}
