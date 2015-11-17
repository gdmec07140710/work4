package com.example.administrator.work4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyContactsActivity extends AppCompatActivity {
    private ListView lv;
    private BaseAdapter lvAdapter;
    private User users[];
    private int slectItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lv = (ListView) findViewById(R.id.listView);
        loadContacts();
    }

    private void loadContacts(){
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        lvAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return users.length;
            }

            @Override
            public Object getItem(int i) {
                return users[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view==null){
                    TextView tv = new TextView(MyContactsActivity.this);
                    tv.setTextSize(22);
                    view  = tv;
                }
                String mobile = users[i].getMobile()==null?"":users[i].getMobile();
                TextView tv = (TextView)view;
                tv.setText(users[i].getName()+"---"+mobile);
                if(i==slectItem){
                    view.setBackgroundColor(Color.YELLOW);
                }else{
                    view.setBackgroundColor(0);
                }
                return view;
            }
        };
        lv.setAdapter(lvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slectItem = i;
                lvAdapter.notifyDataSetChanged();
            }
        });
    }

    public void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("系统信息");
        alert.setMessage("是否要删除联系人？");
        alert.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ContactsTable ct = new ContactsTable(MyContactsActivity.this);
                        if (ct.deleteByUser(users[slectItem])) {
                            users = ct.getAllUser();
                            lvAdapter.notifyDataSetChanged();
                            slectItem = 0;
                            Toast.makeText(MyContactsActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyContactsActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        alert.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
        alert.show();
    }

    public void importPhone(String name,String phone){
        Uri phoneURl = android.provider.ContactsContract.Data.CONTENT_URI;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, 1, 1, "添加");
        menu.add(0,2,2,"编辑");
        menu.add(0,3,3,"查看信息");
        menu.add(0,4,4,"删除");
        menu.add(0,5,5,"查询");
        menu.add(0, 6, 6, "导入到手机电话簿");
        menu.add(0, 7, 7, "退出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case 1:
                Intent intent = new Intent(MyContactsActivity.this,AddContactsActivity.class);
                startActivity(intent);
                break;
            case 2:
                if(users[slectItem].getId_DB()>0){
                    intent = new Intent(MyContactsActivity.this,UpdateContactsActivity.class);
                    intent.putExtra("user_ID",users[slectItem].getId_DB());
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"无结果记录,无法操作！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if(users[slectItem].getId_DB()>0){
                    intent = new Intent(MyContactsActivity.this,ContactsMessageActivity.class);
                    intent.putExtra("user_ID",users[slectItem].getId_DB());
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"无结果记录，无法操作！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if(users[slectItem].getId_DB()>0){
                    delete();
                }else{
                    Toast.makeText(this,"无结果记录，无法操作！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                new FindDialog(MyContactsActivity.this).show();
                break;
            case 6:
                if(users[slectItem].getId_DB()>0){
                    importPhone(users[slectItem].getName(),users[slectItem].getMobile());
                    Toast.makeText(this,"已经成功导入"+users[slectItem].getName()+"到手机电话簿！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"无结果记录，无法操作！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume(){
        super.onResume();
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        lvAdapter.notifyDataSetChanged();
    }
}
