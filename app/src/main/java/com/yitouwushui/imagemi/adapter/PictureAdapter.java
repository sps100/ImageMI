package com.yitouwushui.imagemi.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yitouwushui.imagemi.R;
import com.yitouwushui.imagemi.application.MyApplication;
import com.yitouwushui.imagemi.bean.ImageBean;
import com.yitouwushui.imagemi.bean.MyImage;
import com.yitouwushui.imagemi.mvp.picture.PictureFragment;
import com.yitouwushui.imagemi.uitls.DensityUtils;
import com.yitouwushui.imagemi.uitls.ScreenUtils;
import com.yitouwushui.imagemi.uitls.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ding on 2017/4/18.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private List<MyImage> mValues = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;
    private PictureItemAdapter[] pictureItemAdapters;
    private PictureFragmentItem pictureFragmentItem;
    /**
     * imageSpace0 image size;
     * imageSpace1 每行显示图片的数量
     * imageSpace2 图片的边距
     */
    private int[] imageSpace = new int[3];

    public PictureAdapter(List<MyImage> mValues, Context mContext) {
        this.mValues.addAll(mValues);
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        measureImageSpace();
        // 存适配器
        pictureItemAdapters = new PictureItemAdapter[mValues.size()];

    }

    public void setPictureFragmentItem(PictureFragmentItem pictureFragmentItem) {
        this.pictureFragmentItem = pictureFragmentItem;
    }

    public List<MyImage> getValues() {
        return mValues;
    }

    public void setValues(List<MyImage> mValues) {
        this.mValues.clear();
        this.mValues.addAll(mValues);
        pictureItemAdapters = new PictureItemAdapter[mValues.size()];
    }

    public void addValues(List<MyImage> mValues) {
        this.mValues.addAll(mValues);
    }

    /**
     * 测量图片尺寸间距
     */
    public void measureImageSpace() {
        int w = ScreenUtils.getScreenWidth(mContext);
        int h = ScreenUtils.getScreenHeight(mContext);
        int dpUnit = DensityUtils.dp2px(mContext, 1f);
        if (w > h) {
            imageSpace[0] = h / 4 - dpUnit;
            imageSpace[1] = w * 4 / h;
        } else {
            imageSpace[0] = w / 4 - dpUnit;
            imageSpace[1] = 4;
        }
        imageSpace[2] = dpUnit * imageSpace[1] / (imageSpace[1] - 1);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_picture_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyImage myImage = mValues.get(position);
        if (TextUtils.isEmpty(myImage.content)) {
            holder.line.setVisibility(View.GONE);
            holder.tvLocation.setVisibility(View.GONE);
        } else {
            holder.tvLocation.setText(myImage.content);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) mContext.getResources().getDimension(R.dimen.item_text_height), Gravity.TOP);
        holder.position = position;
        holder.itemRecyclerTitle.setLayoutParams(params);
        holder.tvDate.setText(myImage.getData());
        holder.itemRecyclerView.setLayoutManager(new GridLayoutManager(mContext, imageSpace[1], GridLayoutManager.VERTICAL, false));
        if (position < pictureItemAdapters.length) {
            PictureItemAdapter pictureItemAdapter;
            if (pictureItemAdapters[position] != null) {
                pictureItemAdapter = pictureItemAdapters[position];
            } else {
                pictureItemAdapter = new PictureItemAdapter(myImage.imageBeanList, mContext, imageSpace, pictureItemOnClick);
                pictureItemAdapters[position] = pictureItemAdapter;
            }
            holder.itemRecyclerView.setAdapter(pictureItemAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_recycler_view)
        RecyclerView itemRecyclerView;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.line)
        TextView line;
        @Bind(R.id.tv_location)
        TextView tvLocation;
        @Bind(R.id.tv_button)
        TextView tvButton;
        @Bind(R.id.item_recycler_title)
        LinearLayout itemRecyclerTitle;

        private int position;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.tv_button)
        public void onClick(View v) {
            if (pictureFragmentItem != null) {
                PictureItemAdapter pictureItemAdapter = pictureItemAdapters[position];
//                pictureItemAdapter.
//                pictureFragmentItem.onItemClick();
            }
        }
    }

    public interface PictureFragmentItem {
        /**
         * @param ids 选取的图片id
         */
        void onItemClick(long[] ids);
    }

    PictureItemAdapter.PictureItemOnClick pictureItemOnClick = new PictureItemAdapter.PictureItemOnClick() {
        @Override
        public void onItemClick(View view, ImageBean imageBean, int position) {
            UIUtils.showToast(mContext.getApplicationContext(), String.valueOf(imageBean.getId()));
        }

        @Override
        public void onLongClick(View view, boolean isSelected, int position, long id) {
            UIUtils.showToast(mContext.getApplicationContext(), String.valueOf(id));
            MyApplication.isSelectionMode = true;
//            if (position < pictureItemAdapters.length && pictureItemAdapters[position] != null) {
//                pictureItemAdapters[position].notifyDataSetChanged();
//            }
            for (int i = 0; i < pictureItemAdapters.length; i++) {
                if (pictureItemAdapters[i] != null) {
                    pictureItemAdapters[i].notifyDataSetChanged();
                }
            }
        }
    };
}
