package com.clover.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.clover.R;
import com.clover.entities.Mood;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

/**
 * Created by dan on 2015/6/29.
 */
public class MdListViewAdapter extends BaseAdapter{
    private Context context;
    private List<Mood> moodList;

    public MdListViewAdapter(Context context, List<Mood> moodList) {
        this.context = context;
        this.moodList = moodList;
    }

    @Override
    public int getCount() {
        return moodList.size();
    }

    @Override
    public Mood getItem(int position) {
        return moodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int newPosition = getCount()-1-position;
        Mood mood = getItem(newPosition);
        View view;
        ViewHolder viewHolder;
       // if(convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.mood_item, null);
            viewHolder.tv_mood = (TextView) view.findViewById(R.id.tv_mood);
            viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            viewHolder.iv_mood = (ImageView) view.findViewById(R.id.iv_mood);
        //    view.setTag(viewHolder);
       // }else{
       //     view = convertView;
         //   viewHolder = (ViewHolder) view.getTag();
       // }
        viewHolder.tv_mood.setText(mood.getMd_content());
        viewHolder.tv_date.setText(dateFormat(mood.getCreatedAt().toString()));
        if(newPosition!=0){
            if(dateFormat(getItem(newPosition-1).getCreatedAt()).equals(dateFormat(getItem(newPosition).getCreatedAt()))){
                viewHolder.tv_date.setVisibility(View.INVISIBLE);
            }
        }

      //  if(mood.getMd_path()!=null&&(!mood.getMd_path().equals(""))){
         //   viewHolder.iv_mood.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(mood.getMd_path(), viewHolder.iv_mood,
                    new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisc(true)
                            .considerExifParams(true)
                            .imageScaleType(ImageScaleType.EXACTLY)//设置图片编码方式
                            .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                            .considerExifParams(true)
                            .resetViewBeforeLoading(true)
                            .displayer(new FadeInBitmapDisplayer(100))// 淡入
                            .build());
      //  }else {
          //  viewHolder.iv_mood.setVisibility(View.GONE);
      //  }


        return view;
    }

    public class ViewHolder{
        TextView tv_mood;
        ImageView iv_mood;
        TextView tv_date;
    }

    public String dateFormat(String date){
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        return month+"."+day;
    }
}
