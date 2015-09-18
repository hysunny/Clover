package com.clover.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.R;
import com.clover.entities.Relationship;
import com.clover.entities.User;
import com.clover.utils.Config;
import com.clover.utils.PhotoUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserInfoUpdateActivity extends BaseActivity{

    private RelativeLayout rv_head;//头像
    private RelativeLayout rv_nick;//昵称
    private RelativeLayout rv_age;//年龄
    private RelativeLayout rv_sex;//性别
    private ImageView iv_head;//头像
    private TextView tv_nick;//昵称
    private TextView tv_age;//年龄
    private TextView tv_sex;//性别
    private ImageView iv_arraw;//头像旁的小箭头
    private ImageView iv_nickarraw;//昵称旁的
    private Button btn_deletelover;//解除关系
    User user;
    int age;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_update);

        initView();
    }

    private void initView(){
        rv_head = (RelativeLayout) findViewById(R.id.layout_head);
        rv_nick = (RelativeLayout) findViewById(R.id.layout_nick);
        rv_age = (RelativeLayout) findViewById(R.id.layout_age);
        rv_sex = (RelativeLayout) findViewById(R.id.layout_sex);
        iv_head = (ImageView) findViewById(R.id.avator);
        tv_nick = (TextView) findViewById(R.id.nick);
        tv_age = (TextView) findViewById(R.id.age);
        tv_sex = (TextView) findViewById(R.id.sex);
        iv_arraw = (ImageView) findViewById(R.id.iv_arraw);
        iv_nickarraw = (ImageView) findViewById(R.id.iv_nickarraw);
        btn_deletelover = (Button) findViewById(R.id.deletelover);

        Intent intent = getIntent();
        String tag;
        if(intent.getExtras()==null){
            tag="me";
        }else{
            tag = intent.getStringExtra("tag");
        }

        if(tag.equals("me")) {

            user = userManager.getCurrentUser(User.class);
            initToolbar("编辑个人资料", new Intent(this, MainActivity.class), this);


            rv_head.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoUpdateActivity.this);
                    final String[] avator = {UserInfoUpdateActivity.this.getResources().getString(R.string.takephoto), UserInfoUpdateActivity.this.getResources().getString(R.string.choosefrommibile)};
                    builder.setItems(avator, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent;
                            switch (which) {
                                case 0:
                                    //拍照
                                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                                            .getExternalStorageDirectory(), "temp.jpg")));
                                    startActivityForResult(intent, Config.PHOTO_GRAPH);
                                    break;
                                case 1:
                                    //相册
                                    intent = new Intent(Intent.ACTION_PICK, null);
                                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Config.IMAGE_UNSPECIFIED);
                                    startActivityForResult(intent, Config.PHOTO_ZOOM);
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
            });
            rv_nick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nickIntent = new Intent(UserInfoUpdateActivity.this, UpdateUserNickActivity.class);
                    startActivity(nickIntent);
                }
            });


            rv_age.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();

                    new DatePickerDialog(UserInfoUpdateActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // TODO Auto-generated method stub
                                    age = CalculateAge(dayOfMonth, monthOfYear + 1, year);
                                    String ageOfString = String.valueOf(age);
                                    tv_age.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                    tv_age.setText(ageOfString);
                                    updateAge(age);
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            rv_sex.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoUpdateActivity.this);
                    builder.setTitle(UserInfoUpdateActivity.this.getResources().getString(R.string.pleaseselectsex));
                    final String[] sex = {UserInfoUpdateActivity.this.getResources().getString(R.string.man), UserInfoUpdateActivity.this.getResources().getString(R.string.woman)};
                    builder.setItems(sex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_sex.setText(sex[which]);
                            updateSex(sex[which]);
                        }
                    });
                    builder.show();
                }
            });


            refreshUser(user);
        }else{
            user = application.getOne_user();
            initToolbar("情侣资料", new Intent(this, MainActivity.class), this);
            iv_nickarraw.setVisibility(View.INVISIBLE);
            iv_arraw.setVisibility(View.INVISIBLE);
            btn_deletelover.setVisibility(View.VISIBLE);
            btn_deletelover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    unBindLover();
                }
            });
            refreshUser(user);
        }
    }

    private void refreshUser(User user){
        refreshAvatar(user);
        refreshNick(user);
        refreshAge(user);
        refreshSex(user);
    }

    /*
    解除绑定
     */
    Relationship relationship;
    private void unBindLover(){

        relationship = application.getRelationship();
        relationship.delete(UserInfoUpdateActivity.this, new DeleteListener() {
            @Override
            public void onSuccess() {
                ShowToast("解除成功");
                BmobMsg msg = BmobMsg.createTextSendMsg(UserInfoUpdateActivity.this, application.getOne_user().getObjectId(), BmobChatUser.getCurrentUser(UserInfoUpdateActivity.this).getUsername());
                msg.setExtra("unbind");
                chatManager.sendTextMessage(application.getOne_user(), msg);
                application.setOne_user(null);
                Intent intent1 = new Intent(UserInfoUpdateActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                ShowToast("解除失败");
            }
        });
    }

    /**
     * 刷新头像
     */
    private void refreshAvatar(User user){
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.equals("")) {
            ImageLoader.getInstance().displayImage(avatarUrl, iv_head,
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
        } else {
            iv_head.setImageResource(R.drawable.head);
        }
    }

    /**
     * 刷新昵称
     */
    public void refreshNick(User user){
        if(user.getNick()==null){
            tv_nick.setText("");
        }else{
            tv_nick.setText(user.getNick());
        }
    }

    /**
     * 刷新年龄
     */
    public void refreshAge(User user){
        if(user.getAge()==null){
            tv_age.setText("");
        }else{
            tv_age.setText(String.valueOf(user.getAge()));
        }
    }

    /**
     * 刷新性别
     */
    private void refreshSex(User user){

        if(user.getSex()==null){
            tv_sex.setText("");
        }else if(user.getSex()){
            tv_sex.setText(UserInfoUpdateActivity.this.getResources().getString(R.string.woman));
        }else if(!user.getSex()){
            tv_sex.setText(UserInfoUpdateActivity.this.getResources().getString(R.string.man));
        }
    }

    /**
     * 计算年龄
     */
    private int CalculateAge(int dayOfBirth, int monthOfBirth, int yearOfBirth){
        int age=0;
        Calendar now = Calendar.getInstance();
        int yearOfNow = now.get(Calendar.YEAR);
        int monthOfNow = now.get(Calendar.MONTH)+1;
        int dayOfNow = now.get(Calendar.DAY_OF_MONTH);
        if(yearOfBirth>yearOfNow||(yearOfBirth==yearOfNow&&monthOfBirth>monthOfNow)||(yearOfBirth==yearOfNow&&monthOfBirth==monthOfNow&&dayOfBirth>dayOfNow)){
            Toast.makeText(UserInfoUpdateActivity.this, R.string.wrongbirthdate, Toast.LENGTH_LONG).show();
        }else{
            age = yearOfNow - yearOfBirth;
            if(monthOfNow<=monthOfBirth){
                if(monthOfNow==monthOfBirth){
                    if(dayOfNow<dayOfBirth){
                        age--;
                    }else {
                    }
                }else {
                        age--;
                }
            }else{

            }
        }
        return age;
    }

    /**
     * 修改年龄
     */
    private void updateAge(int age){
        final User user = userManager.getCurrentUser(User.class);
        User u = new User();
        u.setAge(age);
        u.setObjectId(user.getObjectId());
        u.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserInfoUpdateActivity.this, R.string.updatesuccess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserInfoUpdateActivity.this, R.string.updatefailure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 修改性别
     */
    private void updateSex(String sex){
        boolean tag;
        if(sex==this.getResources().getString(R.string.man)){
            tag=false;
        }else{
            tag=true;
        }
        final User user = userManager.getCurrentUser(User.class);
        User u = new User();
        u.setSex(tag);
        u.setObjectId(user.getObjectId());
        u.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserInfoUpdateActivity.this, R.string.updatesuccess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserInfoUpdateActivity.this,R.string.updatefailure, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 收缩图片
     */
    public void cropPhoto(Uri uri) {
        Intent intent = PhotoUtils.startPhotoZoom(uri);
        startActivityForResult(intent, Config.PHOTO_RESOULT);
    }

    /**
     * 根据intent值来判断是拍照还是从相册选择
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Config.NONE)
            return;
        // 拍照
        if (requestCode == Config.PHOTO_GRAPH) {
            // 设置文件保存路径
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            cropPhoto(Uri.fromFile(picture));
        }
        if (data == null)
            return;
        // 读取相册缩放图片
        if (requestCode == Config.PHOTO_ZOOM) {
            cropPhoto(data.getData());
        }
        // 处理结果
        if (requestCode == Config.PHOTO_RESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                iv_head.setImageBitmap(photo); //把图片显示在ImageView控件上
                String filename = new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date())+".png";
                path = Config.MyAvatarDir + filename;
                PhotoUtils.saveBitmap(Config.MyAvatarDir, filename,
                        photo, true);
                uploadAvatar();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    /**
     * 上传头像
     */
    private void uploadAvatar(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在上传图片，请稍等...");
        dialog.setCancelable(false);
        dialog.show();
        final BmobFile avatar = new BmobFile(new File(path));
        avatar.upload(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                String url = avatar.getFileUrl(UserInfoUpdateActivity.this);
                updateAvatar(url);
                dialog.dismiss();
                Toast.makeText(UserInfoUpdateActivity.this, "上传成功！！！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, String s) {
                dialog.dismiss();
                Toast.makeText(UserInfoUpdateActivity.this, "上传失败！！！",Toast.LENGTH_LONG).show();

            }
        });
    }

    /**
     * 修改用户头像
     */
    private void updateAvatar(String url){
        final User user = userManager.getCurrentUser(User.class);
        User u = new User();
        u.setAvatar(url);
        u.setObjectId(user.getObjectId());
        u.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserInfoUpdateActivity.this, R.string.updatesuccess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UserInfoUpdateActivity.this,R.string.updatefailure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshUser(user);
    }

}
