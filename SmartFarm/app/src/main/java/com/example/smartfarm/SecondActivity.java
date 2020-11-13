package com.example.smartfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.smartfarm.ui.alarm.AlarmFragment;
import com.example.smartfarm.ui.modify.ModifyFragment;
import com.example.smartfarm.ui.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.msg.Msg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class SecondActivity extends AppCompatActivity {
    Fragment1 fragment1; // 홈
    Fragment2 fragment2; // 센서
    AlarmFragment alarmFragment;
    ModifyFragment modifyFragment;
    SettingFragment settingFragment;
    FragmentManager fragmentManager;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    NavigationView navigationView; // 왼쪽 상단 메뉴 네비게이션
    BottomNavigationView bottomNavigationView; // 하단 네비게이션

    // TCP/IP 접속
    int port;
    String address;
    String id;
    Socket socket;
    Sender sender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String login_result = bundle.getString("login", "");
        Toast.makeText(this, login_result, Toast.LENGTH_SHORT).show();

        // TCP/IP
        port = 5555;
        address = "192.168.0.3";
        id = "[Phone]";
        new Thread(con).start();


        //Fragment
        fragment1 = new Fragment1(this);
        fragment2 = new Fragment2(this);
        alarmFragment = new AlarmFragment();
        modifyFragment = new ModifyFragment();
        settingFragment = new SettingFragment();

        fragmentManager = getSupportFragmentManager();

        // 상단 메뉴
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(SecondActivity.this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState(); // 삼선 메뉴 만들기
        drawerLayout.addDrawerListener(drawerToggle);

        // 왼쪽 상단 메뉴 네비게이션
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, fragment1).commit();
                        Toast.makeText(SecondActivity.this, "홈", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.alarm_list:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, alarmFragment).commit();
                        Toast.makeText(SecondActivity.this, "알림 목록", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.modify_farm_info:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, modifyFragment).commit();
                        Toast.makeText(SecondActivity.this, "농장 정보 수정", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting:
                        fragmentManager.beginTransaction().replace(R.id.framelayout, settingFragment).commit();
                        Toast.makeText(SecondActivity.this, "설정", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        Toast.makeText(SecondActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                        finish();
                }

                // Drawer Nav 닫기
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });


        // 하단 네비게이션
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ProgressDialog progressDialog = null;
                if (item.getItemId() == R.id.tab1) {
                    fragmentManager.beginTransaction().replace(R.id.framelayout, fragment1).commit();
                    Toast.makeText(SecondActivity.this, "홈", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.tab2) {
                    fragmentManager.beginTransaction().replace(R.id.framelayout, fragment2).commit();
                    Toast.makeText(SecondActivity.this, "센서 제어", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.tab3) {
                    Toast.makeText(SecondActivity.this, "QR코드", Toast.LENGTH_SHORT).show();

                }

                return false;
            }
        });
        fragmentManager.beginTransaction().replace(R.id.framelayout, fragment1).commit();
        Toast.makeText(SecondActivity.this, "홈", Toast.LENGTH_SHORT).show();
    }

    // 왼쪽 상단 네비게이션 항목 선택 시
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // 뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try{
            Msg msg = new Msg(null,"q");
            sender.setMsg(msg);
            new Thread(sender).start();
            if(socket != null){
                socket.close();
            }
            finish();
            onDestroy();
        }catch (Exception e){

        }
    }

    Runnable con = new Runnable() {
        @Override
        public void run() {
            try {
                connect();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    };

    public void connect() throws IOException {
        try {
            socket = new Socket(address, port);
        } catch (Exception e) {
            while (true) {
                try {
                    Thread.sleep(2000);
                    socket = new Socket(address, port);
                    break;
                } catch (Exception e1) {
                    System.out.println("Retry ...");
                }
            }
        }
        System.out.println("Connected Server:" + address);

        sender = new Sender(socket);
        new Receiver(socket).start();
        getList();
    }

    private void getList() {
        // 사용자의 정보를 주시오~
        Msg msg = new Msg(null,"[Phone]","1");
        sender.setMsg(msg);
        new Thread(sender).start();

    }

    class Receiver extends Thread {
        Socket socket;
        ObjectInputStream oi;

        public Receiver(){}

        public Receiver(Socket socket) throws IOException {
            this.socket = socket;
            oi = new ObjectInputStream(socket.getInputStream());
        }
        @Override
        public void run() {
            while(oi != null) {
                Msg msg  = null;
                try {
                    msg = (Msg) oi.readObject();
                    if(msg.getMaps() != null) {
                        HashMap<String,Msg> hm = msg.getMaps();
                        Set<String> keys = hm.keySet();
                        for(final String k : keys) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    String tx = tx_list.getText().toString();
//                                    tx_list.setText(tx+k+"\n");
                                }
                            });
                            System.out.println(k);
                        }
                        continue;
                    }
                    final Msg finalMsg = msg;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            String tx = tx_msg.getText().toString();
//                            tx_msg.setText(finalMsg.getId()+ finalMsg.getMsg()+"\n"+tx);
                        }
                    });
                    System.out.println(msg.getId()+msg.getMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.print("서버 연결이 끊어졌습니다.");
                    break;
                }
            }
            try {
                if(oi != null) {
                    oi.close();
                }
                if(socket != null) {
                    socket.close();
                }
            }catch(Exception e) {

            }
        }

    }
    class Sender implements Runnable {
        Socket socket;
        ObjectOutputStream oo;
        Msg msg;

        public Sender(Socket socket) throws IOException {
            this.socket = socket;
            oo = new ObjectOutputStream(socket.getOutputStream());
        }

        public void setMsg(Msg msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (oo != null) {
                try {
                    oo.writeObject(msg);
                } catch (IOException e) {
//					e.printStackTrace();
                    try {
                        if (socket != null) {
                            socket.close();
                        }


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    try {
                        // 재접속
                        Thread.sleep(2000);
                        connect();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

    }

}