package com.t2xm.utils.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.t2xm.R;
import com.t2xm.application.activity.DetailsActivity;
import com.t2xm.application.activity.MyReviewsActivity;
import com.t2xm.dao.ReviewDao;
import com.t2xm.entity.Item;
import com.t2xm.entity.Review;
import com.t2xm.utils.ToastUtil;
import com.t2xm.utils.values.RequestCode;
import com.t2xm.utils.valuesConverter.JsonUtil;
import com.t2xm.utils.valuesConverter.NumberFormatUtil;

import java.util.ArrayList;
import java.util.List;

public class MyReviewsAdapter extends RecyclerView.Adapter<MyReviewsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Review> reviewList;
    private List<Item> itemList;

    private AlertDialog.Builder deleteReviewBuilder;


    public MyReviewsAdapter(Context context, List<Review> reviewList, List<Item> itemList) {
        this.context = context;
        this.reviewList = reviewList;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(this.context);
        deleteReviewBuilder = new AlertDialog.Builder(context)
                .setTitle("Delete this review?")
                .setNegativeButton("Don't Delete", null);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Review review = getReviewByPosition(position);
        Item item = getItemByPosition(position);
        try {
            String firstImageName = (String) JsonUtil.mapJsonToObject(item.image, List.class).get(0);
            viewHolder.iv_itemImage.setImageResource(this.context.getResources().getIdentifier(firstImageName, "drawable", context.getPackageName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.tv_itemName.setText(item.itemName);
        viewHolder.tv_itemRating.setText(String.valueOf(NumberFormatUtil.get2dpDouble(item.avgRating)));
        updateRatingStars(viewHolder, item.avgRating);
        viewHolder.tv_itemContent.setText(review.reviewText);

        viewHolder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("itemId", item.itemId);
                ((Activity) context).startActivityForResult(intent, RequestCode.VIEW_REVIEWED_ITEM);
            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReviewBuilder.setPositiveButton("OK", getDeleteOnClickListener(review.reviewId)).create().show();
            }
        });
    }

    private DialogInterface.OnClickListener getDeleteOnClickListener(Integer reviewId) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = ReviewDao.deleteReviewByReviewId(reviewId);
                if (result) {
                    ToastUtil.createAndShowToast(context.getApplicationContext(), "Review has been deleted");
                    ((MyReviewsActivity) context).updateRecyclerView();
                } else {
                    ToastUtil.createAndShowToast(context.getApplicationContext(), "Error: Please try again");
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    private Item getItemByPosition(int position) {
        return itemList.get(position);
    }

    private Review getReviewByPosition(int position) {
        return reviewList.get(position);
    }

    private void updateRatingStars(ViewHolder viewHolder, Double rating) {
        int index = 0;
        while (index < 5) {
            if (rating >= 1) {
                viewHolder.iv_starList.get(index).setImageResource(R.drawable.ic_baseline_star_24);
                rating -= 1;
            } else if (rating >= 0.5) {
                viewHolder.iv_starList.get(index).setImageResource(R.drawable.ic_baseline_star_half_24);
                rating = Math.floor(rating);
            } else {
                viewHolder.iv_starList.get(index).setImageResource(R.drawable.ic_baseline_star_border_24);
                rating = Math.floor(rating);
            }
            index++;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_container;
        public ImageView iv_itemImage;
        public TextView tv_itemName;
        public TextView tv_itemRating;
        public TextView tv_itemContent;
        public List<ImageView> iv_starList = new ArrayList<>();
        public ImageButton btn_delete;

        public ViewHolder(View view) {
            super(view);
            ll_container = view.findViewById(R.id.ll_container);
            iv_itemImage = view.findViewById(R.id.iv_item_image);
            tv_itemName = view.findViewById(R.id.tv_item_name);
            tv_itemRating = view.findViewById(R.id.tv_item_rating);
            tv_itemContent = view.findViewById(R.id.tv_item_content);
            iv_starList.add(view.findViewById(R.id.iv_star_1));
            iv_starList.add(view.findViewById(R.id.iv_star_2));
            iv_starList.add(view.findViewById(R.id.iv_star_3));
            iv_starList.add(view.findViewById(R.id.iv_star_4));
            iv_starList.add(view.findViewById(R.id.iv_star_5));
            btn_delete = view.findViewById(R.id.btn_delete);
        }
    }
}
