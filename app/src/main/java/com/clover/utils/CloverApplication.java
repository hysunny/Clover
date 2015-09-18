package com.clover.utils;

import android.app.Application;
import android.content.Context;

import com.clover.entities.Relationship;
import com.clover.entities.User;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


/**
 * Created by zpfang on 2015/6/30.
 */
public class CloverApplication extends Application{

    public static CloverApplication application;
    private User one_user;
    private Relationship relationship;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // 是否开启debug模式--默认开启状态
        //BmobChat.DEBUG_MODE = true;
        application = this;
        initImageLoader(getApplicationContext());
    }

    public User getOne_user() {
        return one_user;
    }

    public void setOne_user(User one_user) {
        this.one_user = one_user;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    /** 初始化ImageLoader */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "bmobim/Cache");// 获取到缓存的目录地址
        // 创建配置ImageLoader，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                        // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }
}
