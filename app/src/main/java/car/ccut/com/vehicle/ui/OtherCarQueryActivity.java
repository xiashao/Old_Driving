package car.ccut.com.vehicle.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cheshouye.api.client.WeizhangClient;
import com.cheshouye.api.client.WeizhangIntentService;
import com.cheshouye.api.client.json.CarInfo;
import com.cheshouye.api.client.json.CityInfoJson;
import com.cheshouye.api.client.json.InputConfigJson;

import butterknife.Bind;
import butterknife.OnClick;
import car.ccut.com.vehicle.MyApplication;
import car.ccut.com.vehicle.R;
import car.ccut.com.vehicle.base.BaseActivity;
import car.ccut.com.vehicle.interf.ConstantValue;


/**
 * Created by lenovo on 2016/4/17.
 */
public class OtherCarQueryActivity extends BaseActivity{
    private String defaultFrameNum="";
    private String defaultEngineNum;
    @Bind(R.id.chepai_sz)
    TextView short_name;
    @Bind(R.id.cx_city)
    TextView query_city;
    @Bind(R.id.chepai_number)
    EditText chepai_number;
    @Bind(R.id.chejia_number)
    EditText chejia_number;
    @Bind(R.id.engine_number)
    EditText engine_number;
    @Bind(R.id.popXSZ)
    View popXSZ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_others_query;
    }

    @Override
    public void initView() {
        setTitle("违章查询");
        // 根据默认查询地城市id, 初始化查询项目--显示默认值
        // setQueryItem(defaultCityId, defaultCityName);
        String carNum = MyApplication.getCurrentServerCar().getCarNumber();
        short_name.setText(carNum.substring(0,1));
//        query_city.setText(defaultCity);
        chepai_number.setText(carNum.substring(1,carNum.length()));
        engine_number.setText(defaultEngineNum);
        // 显示隐藏行驶证图示
        popXSZ.setOnTouchListener(new popOnTouchListener());
        hideShowXSZ();
    }

    @Override
    public void initData() {
        Intent weizhangIntent = new Intent(OtherCarQueryActivity.this, WeizhangIntentService.class);
        weizhangIntent.putExtra("appId", ConstantValue.VIOLATION_ID);
        weizhangIntent.putExtra("appKey", ConstantValue.VIOLATION_KEY);
        startService(weizhangIntent);
        defaultEngineNum=MyApplication.getCurrentServerCar().getCarMachineNo();
    }

    @Override
    @OnClick({R.id.btn_cpsz,R.id.linearLayout1,R.id.iv_title_back,R.id.btn_query})
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id){
            case R.id.btn_cpsz:
                intent.setClass(OtherCarQueryActivity.this, ProvinceList.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.linearLayout1:
                intent.setClass(OtherCarQueryActivity.this, ShortNameList.class);
                intent.putExtra("select_short_name", short_name.getText());
                startActivityForResult(intent, 0);
                break;
            case R.id.iv_title_back:
                onBackPressed();
                break;
            case R.id.btn_query:
                query();
                break;
        }

    }


    public void query(){
        // 获取违章信息
        CarInfo car = new CarInfo();
        String quertCityStr = null;
        String quertCityIdStr = null;

        final String shortNameStr = short_name.getText().toString()
                .trim();
        final String chepaiNumberStr = chepai_number.getText()
                .toString().trim();
        if (query_city.getText() != null
                && !query_city.getText().equals("")) {
            quertCityStr = query_city.getText().toString().trim();

        }

        if (query_city.getTag() != null
                && !query_city.getTag().equals("")) {
            quertCityIdStr = query_city.getTag().toString().trim();
            car.setCity_id(Integer.parseInt(quertCityIdStr));
        }
        final String chejiaNumberStr = chejia_number.getText()
                .toString().trim();
        final String engineNumberStr = engine_number.getText()
                .toString().trim();


        car.setChejia_no(chejiaNumberStr);
        car.setChepai_no(shortNameStr + chepaiNumberStr);
        car.setEngine_no(engineNumberStr);
        car.setCity_id(car.getCity_id());
        Intent intent = new Intent();
        intent.putExtra("classno", chejiaNumberStr);
        intent.putExtra("hphm", shortNameStr + chepaiNumberStr);
        intent.putExtra("engineno", engineNumberStr);
        intent.putExtra("city_id", car.getCity_id());
        boolean result = checkQueryItem(car);
        if (result) {
            intent.setClass(OtherCarQueryActivity.this, WeizhangResult.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (requestCode) {
            case 0:
                Bundle bundle = data.getExtras();
                String ShortName = bundle.getString("short_name");
                short_name.setText(ShortName);
                break;
            case 1:
                Bundle bundle1 = data.getExtras();
                String cityName = bundle1.getString("city_name");
                String cityId = bundle1.getString("city_id");
                //车架号后三位
//                Resources res7 =getResources();
//                String[] frame3 = res7.getStringArray(R.array.frame3);
//                for (int i=0;i<frame3.length;i++){
//                    if (frame3[i].equals(cityName)){
//                        chejia_number.setText(defaultFrameNum.substring(defaultFrameNum.length()-3));
//                    }
//                }
//                //车架号后四位
//                Resources res1 =getResources();
//                String[] frame4 = res1.getStringArray(R.array.frame4);
//                for (int i=0;i<frame4.length;i++){
//                    if (frame4[i].equals(cityName)){
//                        chejia_number.setText(defaultFrameNum.substring(defaultFrameNum.length()-4));
//                    }
//                }
                //车架号后六位
//                Resources res2 =getResources();
//                String[] frame6 = res2.getStringArray(R.array.frame6);
//                for (int i=0;i<frame6.length;i++){
//                    if (frame6[i].equals(cityName)){
//                        chejia_number.setText(defaultFrameNum.substring(defaultFrameNum.length()-6));
//                    }
//                }
                //车架号后7位
//                Resources res8 =getResources();
//                String[] frame7 = res8.getStringArray(R.array.frame7);
//                for (int i=0;i<frame7.length;i++){
//                    if (frame7[i].equals(cityName)){
//                        chejia_number.setText(defaultFrameNum.substring(defaultFrameNum.length()-7));
//                    }
//                }
                //完整车架号
//                Resources res3 =getResources();
//                String[] completeFrame = res3.getStringArray(R.array.frame_complete);
//                for (int i=0;i<completeFrame.length;i++){
//                    if (completeFrame[i].equals(cityName)){
//                        chejia_number.setText(defaultFrameNum);
//                    }
//                }
                //发动机号后四位
                Resources res4 =getResources();
                String[] engine4 = res4.getStringArray(R.array.engine4);
                for (int i=0;i<engine4.length;i++){
                    if (engine4[i].equals(cityName)){
                        engine_number.setText(defaultEngineNum.substring(defaultEngineNum.length() - 4));
                    }
                }
                //发动机号后六位
                Resources res5 =getResources();
                String[] engine6 = res5.getStringArray(R.array.engine6);
                for (int i=0;i<engine6.length;i++){
                    if (engine6[i].equals(cityName)){
                        engine_number.setText(defaultEngineNum.substring(defaultEngineNum.length()-6));
                    }
                }
                //完整发动机号
                Resources res6 =getResources();
                String[] fullEngine = res6.getStringArray(R.array.engine_complete);
                for (int i=0;i<fullEngine.length;i++){
                    if (fullEngine[i].equals(cityName)){
                        engine_number.setText(defaultEngineNum);
                    }
                }
                // query_city.setText(cityName);
                // query_city.setTag(cityId);
                // InputConfigJson inputConfig =
                // WeizhangClient.getInputConfig(Integer.parseInt(cityId));
                // System.out.println(inputConfig.toJson());
                setQueryItem(Integer.parseInt(cityId));


                break;
        }
    }

    // 根据城市的配置设置查询项目
    private void setQueryItem(int cityId) {

        InputConfigJson cityConfig = WeizhangClient.getInputConfig(cityId);

        // 没有初始化完成的时候;
        if (cityConfig != null) {
            CityInfoJson city = WeizhangClient.getCity(cityId);

            query_city.setText(city.getCity_name());
            query_city.setTag(cityId);

            int len_chejia = cityConfig.getClassno();
            int len_engine = cityConfig.getEngineno();

            View row_chejia = (View) findViewById(R.id.row_chejia);
            View row_engine = (View) findViewById(R.id.row_engine);

            // 车架号
            if (len_chejia == 0) {
                row_chejia.setVisibility(View.GONE);
            } else {
                row_chejia.setVisibility(View.VISIBLE);
                setMaxlength(chejia_number, len_chejia);
                if (len_chejia == -1) {
                    chejia_number.setHint("请输入完整车架号");
                } else if (len_chejia > 0) {
                    chejia_number.setHint("请输入车架号后" + len_chejia + "位");
                }
            }

            // 发动机号
            if (len_engine == 0) {
                row_engine.setVisibility(View.GONE);
            } else {
                row_engine.setVisibility(View.VISIBLE);
                setMaxlength(engine_number, len_engine);
                if (len_engine == -1) {
                    engine_number.setHint("请输入完整车发动机号");
                } else if (len_engine > 0) {
                    engine_number.setHint("请输入发动机后" + len_engine + "位");
                }
            }
        }
    }

    // 提交表单检测
    private boolean checkQueryItem(CarInfo car) {
        if (car.getCity_id() == 0) {
            Toast.makeText(OtherCarQueryActivity.this, "请选择查询地",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (car.getChepai_no().length() != 7) {
            Toast.makeText(OtherCarQueryActivity.this, "您输入的车牌号有误", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (car.getCity_id() > 0) {
            InputConfigJson inputConfig = WeizhangClient.getInputConfig(car
                    .getCity_id());
            int engineno = inputConfig.getEngineno();
            int registno = inputConfig.getRegistno();
            int classno = inputConfig.getClassno();

            // 车架号
            if (classno > 0) {
                if (car.getChejia_no().equals("")) {
                    Toast.makeText(OtherCarQueryActivity.this, "输入车架号不为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (car.getChejia_no().length() != classno) {
                    Toast.makeText(OtherCarQueryActivity.this, "输入车架号后" + classno + "位",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (classno < 0) {
                if (car.getChejia_no().length() == 0) {
                    Toast.makeText(OtherCarQueryActivity.this, "输入全部车架号", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            //发动机
            if (engineno > 0) {
                if (car.getEngine_no().equals("")) {
                    Toast.makeText(OtherCarQueryActivity.this, "输入发动机号不为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (car.getEngine_no().length() != engineno) {
                    Toast.makeText(OtherCarQueryActivity.this,
                            "输入发动机号后" + engineno + "位", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (engineno < 0) {
                if (car.getEngine_no().length() == 0) {
                    Toast.makeText(OtherCarQueryActivity.this, "输入全部发动机号", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            // 注册证书编号
            if (registno > 0) {
                if (car.getRegister_no().equals("")) {
                    Toast.makeText(OtherCarQueryActivity.this, "输入证书编号不为空", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (car.getRegister_no().length() != registno) {
                    Toast.makeText(OtherCarQueryActivity.this,
                            "输入证书编号后" + registno + "位", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (registno < 0) {
                if (car.getRegister_no().length() == 0) {
                    Toast.makeText(OtherCarQueryActivity.this, "输入全部证书编号", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    // 设置/取消最大长度限制
    private void setMaxlength(EditText et, int maxLength) {
        if (maxLength > 0) {
            et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
                    maxLength) });
        } else { // 不限制
            et.setFilters(new InputFilter[] {});
        }
    }

    // 显示隐藏行驶证图示
    private void hideShowXSZ() {
        View btn_help1 = (View) findViewById(R.id.ico_chejia);
        View btn_help2 = (View) findViewById(R.id.ico_engine);
        Button btn_closeXSZ = (Button) findViewById(R.id.btn_closeXSZ);

        btn_help1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.VISIBLE);
            }
        });
        btn_help2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.VISIBLE);
            }
        });
        btn_closeXSZ.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popXSZ.setVisibility(View.GONE);
            }
        });
    }



    // 避免穿透导致表单元素取得焦点
    private class popOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            popXSZ.setVisibility(View.GONE);
            return true;
        }
    }

}