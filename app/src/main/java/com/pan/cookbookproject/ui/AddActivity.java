package com.pan.cookbookproject.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pan.cookbookproject.R;
import com.pan.cookbookproject.bean.CookBookBean;
import com.pan.cookbookproject.database.CookBookDatabase;

/**
 * Created by pan on 16/7/15.
 */
public class AddActivity extends Activity implements View.OnClickListener{

    private EditText etName;
    private EditText etSeasoning;
    private EditText etMethod;
    private EditText etRemark;
    private Button btnSave;
    private ImageView ivBack;

    private CookBookDatabase cookBookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cookbook);
        initView();
        cookBookDatabase = new CookBookDatabase(this);
    }

    private void initView(){
        etName = (EditText) findViewById(R.id.et_name);
        etSeasoning = (EditText) findViewById(R.id.et_seasoning);
        etMethod = (EditText) findViewById(R.id.et_method);
        etRemark = (EditText) findViewById(R.id.et_remark);
        btnSave = (Button) findViewById(R.id.btn_save);
        ivBack = (ImageView) findViewById(R.id.back);
        btnSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("add", false);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_save:
                saveCookBook();
                break;
        }
    }

    private void saveCookBook() {
        String name = etName.getText().toString();
        String seasoning = etSeasoning.getText().toString();
        String method = etMethod.getText().toString();
        String remark = etRemark.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this,"名称为空,请输入后保存",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(seasoning)) {
            Toast.makeText(this,"原料为空,请输入后保存",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(method)) {
            Toast.makeText(this,"方法为空,请输入后保存",Toast.LENGTH_SHORT).show();
            return;
        }

        CookBookBean cookBookBean = new CookBookBean();
        cookBookBean.setName(name);
        cookBookBean.setSeasoning(seasoning);
        cookBookBean.setMethod(method);
        cookBookBean.setRemark(remark);

        cookBookDatabase.insert(cookBookBean);

        Intent intent = new Intent();
        intent.putExtra("add", true);
        setResult(RESULT_OK, intent);
        finish();
    }
}
