package com.clover.net;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.clover.R;
import com.clover.entities.Relationship;
import com.clover.entities.User;
import com.clover.ui.LoverManagerActivity;
import com.clover.ui.MainActivity;
import com.clover.ui.TTTATPActivity;
import com.clover.utils.ActivityCollector;
import com.clover.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.OnReceiveListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dan on 2015/6/26.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    String tag = null;
    String message;
    String json;
    String msg = "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(final Context context, Intent intent) {
        json = intent.getStringExtra("msg");
        preferences = context.getSharedPreferences("lover", context.MODE_PRIVATE);
        editor = preferences.edit();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String ex = jsonObject.getString("ex");
            //收到聊天消息
            if (ex.equals("chat")) {
                message = jsonObject.getString("mc");
                BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {
                    @Override
                    public void onSuccess(BmobMsg bmobMsg) {
                        BmobDB.create(context).saveMessage(bmobMsg);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

                createNotification(context, 1);
            } else if (ex.equals("invisitBind")) {
                BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {
                    @Override
                    public void onSuccess(final BmobMsg msg) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("情侣邀请");

                        builder.setMessage(msg.getContent() + "邀请和您成为情侣");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                final Relationship relationship = new Relationship();
                                relationship.setW_user(BmobChatUser.getCurrentUser(context, User.class));
                                User user = new User();
                                user.setObjectId(msg.getBelongId());
                                relationship.setM_user(user);
                                relationship.save(context, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(context, "绑定成功", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Intent intent1 = new Intent(context, MainActivity.class);
                                        intent1.putExtra("tag", 2);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent1);
                                        editor.putString("reObjectId", relationship.getObjectId());
                                        editor.commit();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(context, "绑定失败", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                BmobMsg msg = BmobMsg.createTextSendMsg(context, user.getObjectId(), BmobChatUser.getCurrentUser(context, User.class).getUsername());
                                msg.setExtra("bindSuccess");
                                BmobChatManager.getInstance(context).sendTextMessage(user, msg);

                            }
                        });

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        alertDialog.show();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            } else if (ex.equals("bindSuccess")) {
                ActivityCollector.finishAll();
                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);

            } else if (ex.equals("unbind")) {
                Toast.makeText(context, json, Toast.LENGTH_SHORT).show();
                BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {
                    @Override
                    public void onSuccess(final BmobMsg msg) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("情侣解除");

                        builder.setMessage(msg.getContent() + "解除和您的情侣关系");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCollector.finishAll();
                                Intent intent = new Intent(context, LoverManagerActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        alertDialog.show();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            } else if (ex.equals("SLEEP") || ex.equals("GETUP") || ex.equals("EAT") || ex.equals("GAME") || ex.equals("SHOP")||ex.equals("IGETUP")) {
                Intent reminder;
                int extra = 0;
                if (ex.equals("SLEEP")) {
                    tag = Config.SLEEP_ACTION;
                    extra = 1;
                    message = context.getResources().getString(R.string.sleep_msg);
                    reminder = new Intent(tag);
                    reminder.putExtra("key", extra);
                    context.sendBroadcast(reminder);
                    editor.putInt("sleep", 1);
                    editor.commit();
                } else if (ex.equals("IGETUP")) {
                    tag = Config.SLEEP_ACTION;
                    extra = 3;
                    message = context.getResources().getString(R.string.getup_send);
                    reminder = new Intent(tag);
                    reminder.putExtra("key", extra);
                    context.sendBroadcast(reminder);
                    editor.putInt("sleep", 2);
                    editor.commit();
                } else if (ex.equals("GETUP")) {
                    tag = Config.SLEEP_ACTION;
                    extra = 2;
                    message = context.getResources().getString(R.string.getup_mas);
                    reminder = new Intent(tag);
                    reminder.putExtra("key", extra);
                    context.sendBroadcast(reminder);
                } else if (ex.equals("EAT")) {
                    tag = Config.SLEEP_ACTION;
                    message = context.getResources().getString(R.string.eat_msg);
                } else if (ex.equals("GAME")) {
                    tag = Config.SLEEP_ACTION;
                    message = context.getResources().getString(R.string.game_msg);
                } else if (ex.equals("SHOP")) {
                    tag = Config.SLEEP_ACTION;
                    message = context.getResources().getString(R.string.shop_msg);
                }
                // 发送通知
                createNotification(context, 2);
               /* NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification n = new Notification();
                n.icon = R.mipmap.ic_launcher;
                n.tickerText = "Clover收到消息";
                n.when = System.currentTimeMillis();
                Intent i = new Intent();
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                n.setLatestEventInfo(context, "消息", message, pi);
                n.defaults |= Notification.DEFAULT_SOUND;
                n.flags = Notification.FLAG_AUTO_CANCEL;
                nm.notify(1, n);*/
            } else if (ex.equals("MISS") || ex.equals("APOLOGIZE") || ex.equals("BORING") || ex.equals("DO")) {
                if (ex.equals("MISS")) {
                    final AlertDialog missDialog = CreatMyDialog(context, R.layout.dialog_miss_layout);
                    missDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            missDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                } else if (ex.equals("APOLOGIZE")) {
                    final AlertDialog apologizeDialog = CreatMyDialog(context, R.layout.dialog_apologize_layout);
                    apologizeDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            apologizeDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                } else if (ex.equals("BORING")) {
                    final AlertDialog apologizeDialog = CreatMyDialog(context, R.layout.dialog_boring_layout);
                    apologizeDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            apologizeDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                } else if (ex.equals("DO")) {
                    final AlertDialog doeDialog = CreatMyDialog(context, R.layout.dialog_do_layout);
                    doeDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            doeDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                }
            } else if (ex.equals("KISS") || ex.equals("HUG") || ex.equals("MIAO") || ex.equals("WANG")) {
                if (ex.equals("KISS")) {
                    final AlertDialog doeDialog = CreatMyDialog(context, R.layout.dialog_kiss_layout);
                    doeDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            doeDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                } else if (ex.equals("HUG")) {
                    final AlertDialog doeDialog = CreatMyDialog(context, R.layout.dialog_hug_layout);
                    doeDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            doeDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                } else if (ex.equals("MIAO")) {
                    final AlertDialog doeDialog = CreatMyDialog(context, R.layout.dialog_miao_layout);
                    doeDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            doeDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                } else if (ex.equals("WANG")) {
                    final AlertDialog doeDialog = CreatMyDialog(context, R.layout.dialog_wang_layout);
                    doeDialog.show();
                    TimerTask missTask = new TimerTask() {
                        @Override
                        public void run() {
                            doeDialog.dismiss();
                        }
                    };
                    new Timer().schedule(missTask, 2000);
                }

            } else if (ex.equals("MENSES_COME") || ex.equals("MENSES_GONE") || ex.equals("DISEASE_COME") || ex.equals("DISEASE_GONE") || ex.equals("DRUG")||ex.equals("GAME_INVITE")
                    ||ex.equals("GAME_INVITED")||ex.equals("BTN1_CLICKED")||ex.equals("BTN2_CLICKED")||ex.equals("BTN3_CLICKED")||ex.equals("BTN4_CLICKED")||ex.equals("BTN5_CLICKED")||ex.equals("BTN6_CLICKED")
                    ||ex.equals("BTN7_CLICKED")||ex.equals("BTN8_CLICKED")||ex.equals("BTN9_CLICKED")||ex.equals("YOU_LOSE")||ex.equals("TIE_SCORE")||ex.equals("RESTART_REQUEST")
                    ||ex.equals("I_AM_O")||ex.equals("I_AM_X")
                    ) {
                int extra = 0;
                if (ex.equals("MENSES_COME")) {
                    tag = Config.MENSES_COME_ACTION;
                    extra = 1;
                    message = context.getResources().getString(R.string.menses_coming_msg);
                    editor.putInt("menses", 1);
                    editor.commit();
                } else if (ex.equals("MENSES_GONE")) {
                    tag = Config.MENSES_COME_ACTION;
                    extra = 2;
                    message = context.getResources().getString(R.string.menses_going_msg);
                    editor.putInt("menses", 2);
                    editor.commit();
                } else if (ex.equals("DISEASE_COME")) {
                    tag = Config.DISEASE_COME_ACTION;
                    extra = 1;
                    message = context.getResources().getString(R.string.disease_come_msg);
                    editor.putInt("disease", 1);
                    editor.commit();
                } else if (ex.equals("DISEASE_GONE")) {
                    tag = Config.DISEASE_COME_ACTION;
                    extra = 2;
                    message = context.getResources().getString(R.string.disease_gone_msg);
                    editor.putInt("disease", 2);
                    editor.commit();
                } else if (ex.equals("DRUG")) {
                    tag = Config.DISEASE_COME_ACTION;
                    extra = 3;
                    message = context.getResources().getString(R.string.eatdrug);
                } else if (ex.equals("GAME_INVITE")) {
                    tag = Config.GAME_START_ACTION;
                    extra = 1;
                    message = context.getResources().getString(R.string.TTTA_Game_Invite);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.TTTActivity_Game_AlertTitle);
                    builder.setMessage(R.string.TTTActivity_Game_AlertContent);
                    builder.setNegativeButton(R.string.TTTActivity_Game_AlertRefuse, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton(R.string.TTTActivity_Game_AlertSure,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {

                                    Intent intent = new Intent(context, TTTATPActivity.class);
                                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {

                                        @Override
                                        public void onSuccess(BmobMsg bmobMsg) {
                                            User user = new User();
                                            user.setObjectId(bmobMsg.getBelongId());
                                            BmobChatManager chatManager = BmobChatManager.getInstance(context);
                                            msg = "GAME_INVITED";
                                            BmobRequest.pushMessageToLover(msg, "GAME_INVITED", context, bmobMsg.getBelongId(), user, chatManager);
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {

                                        }
                                    });

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//指定会全局,可以在后台弹出
                    dialog.show();
                    message = context.getResources().getString(R.string.TTTA_Game_Invite);
                } else if (ex.equals("GAME_INVITED")) {
                    tag = Config.GAME_START_ACTION;
                    extra = 2;
                    Toast.makeText(
                            context, R.string.TTTA_Game_Invited,
                            Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(context, TTTATPActivity.class);
                    intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);

                }else if (ex.equals("BTN1_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 1;
                } else if (ex.equals("BTN2_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 2;
                } else if (ex.equals("BTN3_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 3;
                } else if (ex.equals("BTN4_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 4;
                } else if (ex.equals("BTN5_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 5;
                } else if (ex.equals("BTN6_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 6;
                } else if (ex.equals("BTN7_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 7;
                } else if (ex.equals("BTN8_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 8;
                } else if (ex.equals("BTN9_CLICKED")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 9;
                }
                else if (ex.equals("YOU_LOSE")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 10;
                }
                else if (ex.equals("TIE_SCORE")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 11;
                }
                else if (ex.equals("RESTART_REQUEST")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 12;
                }
                else if (ex.equals("I_AM_O")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 13;
                }
                else if (ex.equals("I_AM_X")) {
                    tag = Config.GAME_BUTTON_ACTION;
                    extra = 14;
                }

                Intent reminder = new Intent(tag);
                reminder.putExtra("key", extra);
                context.sendBroadcast(reminder);
                createNotification(context, 2);
                /*// 发送通知
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification n = new Notification();
                n.icon = R.mipmap.ic_launcher;
                n.tickerText = "Clover收到消息";
                n.when = System.currentTimeMillis();
                Intent i = new Intent();
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
                n.setLatestEventInfo(context, "消息", message, pi);
                n.defaults |= Notification.DEFAULT_SOUND;
                n.flags = Notification.FLAG_AUTO_CANCEL;
                nm.notify(1, n);*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AlertDialog CreatMyDialog(Context context, int ResId) {
        AlertDialog.Builder builer = new AlertDialog.Builder(context);
        AlertDialog dlg = builer.create();
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(ResId, null);
        dlg.setCanceledOnTouchOutside(true);
        Window dialogWindow = dlg.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        dlg.onWindowAttributesChanged(lp);
        dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dlg.setView(view);
        return dlg;
    }

    public void createNotification(Context context, int tag) {

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification n = new Notification();
        n.icon = R.mipmap.logo;
        n.tickerText = "Clover收到消息";
        n.when = System.currentTimeMillis();
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("tag", tag);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        n.setLatestEventInfo(context, preferences.getString("nick","lover"), message, pi);
        SharedPreferences sharedPreferences = context.getSharedPreferences("set", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("voice", 0) == 1) {
            n.defaults |= Notification.DEFAULT_SOUND;
        }
        if (sharedPreferences.getInt("shake", 0) == 1) {
            n.defaults |= Notification.DEFAULT_VIBRATE;
            long[] vibrate = {0,100,200,300};
            n.vibrate = vibrate;
        }
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, n);
    }


}