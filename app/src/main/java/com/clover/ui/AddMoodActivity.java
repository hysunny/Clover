package com.clover.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.clover.R;
import com.clover.entities.Mood;
import com.clover.entities.User;
import com.clover.utils.Config;
import com.clover.utils.PhotoUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


public class AddMoodActivity extends BaseActivity {

    private EditText et_mood;//发布动态文本
    private ImageView iv_mood;//发布动态图片
    private Button btn_add_mood;//发布动态
    String path;
    String url;
    String txt_mood;
    Mood myMood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        initToolbar(getResources().getString(R.string.title_activity_add_mood), new Intent(this, SpaceActivity.class), this);
        initView();
    }

    private void initView(){
        et_mood = (EditText) findViewById(R.id.mood_txt);
        iv_mood = (ImageView) findViewById(R.id.mood_img);
        btn_add_mood = (Button) findViewById(R.id.add_mood);
        iv_mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddMoodActivity.this);
                final String[] avator = {AddMoodActivity.this.getResources().getString(R.string.takephoto), AddMoodActivity.this.getResources().getString(R.string.choosefrommibile)};
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

        btn_add_mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_mood = et_mood.getText().toString();
                myMood = new Mood();
                myMood.setMd_content(txt_mood);
                myMood.setBelongId(BmobChatUser.getCurrentUser(AddMoodActivity.this, User.class).getObjectId());
                if(TextUtils.isEmpty(txt_mood)&&url==null){
                    return;
                }
                if(url!=null){
                    myMood.setMd_path(url);
                }
                myMood.save(AddMoodActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddMoodActivity.this, AddMoodActivity.this.getResources().getString(R.string.uploadmoodsuc), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddMoodActivity.this, SpaceActivity.class);
                        startActivity(intent);
                        AddMoodActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AddMoodActivity.this, AddMoodActivity.this.getResources().getString(R.string.uploadmoodfai), Toast.LENGTH_LONG).show();
                    }
                });
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
                iv_mood.setImageBitmap(photo); //把图片显示在ImageView控件上
                String filename = new SimpleDateFormat("yyMMddHHmmss")
                        .format(new Date())+".png";
                path = Config.MyAvatarDir + filename;
                PhotoUtils.saveBitmap(Config.MyAvatarDir, filename,
                        photo, true);
                uploadMoodIamge();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 上传图片
     */
    private void uploadMoodIamge(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在上传图片，请稍等...");
        dialog.setCancelable(false);
        dialog.show();
        final BmobFile avatar = new BmobFile(new File(path));
        avatar.upload(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                url = avatar.getFileUrl(AddMoodActivity.this);
                dialog.dismiss();
                Toast.makeText(AddMoodActivity.this, "上传成功！！！", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int i, String s) {
                dialog.dismiss();
                Toast.makeText(AddMoodActivity.this, "上传失败！！！", Toast.LENGTH_LONG).show();
            }
        });
    }

}
