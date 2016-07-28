package car.ccut.com.vehicle.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import car.ccut.com.vehicle.R;

/**
 * Created by lenovo on 2016/4/17.
 */
public class ShortNameList extends Activity {
    private GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取传过来的城市
        Bundle bundle = getIntent().getExtras();
        final String short_name = bundle.getString("select_short_name");

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.csy_activity_shortname);

        //标题
        TextView txtTitle = (TextView) findViewById(R.id.tv_title);
        txtTitle.setText("选择车牌所在地");

        //返回按钮
        ImageView btnBack = (ImageView) findViewById(R.id.iv_title_back);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //省份简称列表
        gv = (GridView) findViewById(R.id.gv_1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.csy_listitem_shortname, getDate());
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String txt = adapter.getItem(position);
                if(txt.length()>0){
                    Toast.makeText(ShortNameList.this, txt, Toast.LENGTH_SHORT).show();
                    // 选择之后再打开一个 显示城市的 Activity；
                    Intent intent = new Intent();
                    intent.putExtra("short_name", txt);
                    setResult(0, intent);
                    finish();
                }
            }
        });

    }

    private String[] getDate() {
        return new String[] { "京", "津", "沪", "川", "鄂", "甘", "赣", "桂", "贵", "黑",
                "吉", "翼", "晋", "辽", "鲁", "蒙", "闽", "宁", "青", "琼", "陕", "苏",
                "皖", "湘", "新", "渝", "豫", "粤", "云", "藏", "浙", ""};
    }

}
