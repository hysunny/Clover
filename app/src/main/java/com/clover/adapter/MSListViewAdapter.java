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
import com.clover.entities.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;

/**
 * Created by dan on 2015/6/29.
 */
public class MSListViewAdapter extends BaseAdapter{

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    private static final int ITEMCOUNT = 2;
    private List<BmobMsg> coll;
    private LayoutInflater mInflater;
    private String currentObjectId = "";
    private View img;
    private Context context;
    private String loverImgPath;

    public MSListViewAdapter(Context context, List<BmobMsg> coll, String loverImgPath) {
        this.context = context;
        this.loverImgPath = loverImgPath;
        currentObjectId = BmobUserManager.getInstance(context).getCurrentUserObjectId();
        this.coll = coll;
        mInflater = LayoutInflater.from(context);

    }

    public void add(BmobMsg msg){
        coll.add(msg);
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public int getItemViewType(int position) {
        BmobMsg entity = coll.get(position);

        if (entity.getBelongId() == currentObjectId) {
            return IMsgViewType.IMVT_TO_MSG;
        } else {
            return IMsgViewType.IMVT_COM_MSG;
        }
    }


    public int getViewTypeCount() {
        return ITEMCOUNT;
    }
String path;
    public View getView(int position, View convertView, ViewGroup parent) {

        BmobMsg entity = coll.get(position);


        ViewHolder viewHolder = null;
     //   if (convertView == null) {
            if (entity.getToId().equals(currentObjectId)) {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_left, null);
                path = loverImgPath;
            } else if(entity.getBelongId().equals(currentObjectId)){
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_right, null);
                path = BmobChatUser.getCurrentUser(context, User.class).getAvatar();
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView
                    .findViewById(R.id.tv_sendtime);
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_chatcontent);
        viewHolder.photo = (ImageView) convertView.findViewById(R.id.iv_userhead);

        //     convertView.setTag(viewHolder);
      //  } else {
      //      viewHolder = (ViewHolder) convertView.getTag();
      //  }
        //时间格式化
        viewHolder.tvSendTime.setText(getStandardDate(entity.getMsgTime()));
        viewHolder.tvContent.setText(entity.getContent());
        ImageLoader.getInstance().displayImage(path, viewHolder.photo,
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
        return convertView;
    }

    static class ViewHolder {
        public ImageView photo;
        public TextView tvSendTime;
        public TextView tvContent;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
// 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     * @param timeStr	时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t*1000);
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前

        if ((day - 1 > 0) && (day-1) <3) {
            sb.append(day + "天");
        }else if(day-1 >3){
            return getStrTime(timeStr);

        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

}
