package com.pan.cookbookproject.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pan.cookbookproject.R;
import com.pan.cookbookproject.bean.CookBookBean;

/**
 * Created by pan on 16/7/19.
 */
public class DetailActivity extends Activity {

    private TextView tvName;
    private TextView tvSeasoning;
    private TextView tvMethod;
    private TextView tvRemark;
    private TextView tvTitleRemark;

    private ImageView back;


    private CookBookBean cookBookBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initData();
    }

    private void initView(){
        tvName = (TextView) findViewById(R.id.cook_book_name);
        tvSeasoning = (TextView) findViewById(R.id.cook_book_seasoning);
        tvMethod = (TextView) findViewById(R.id.cook_book_method);
        tvRemark = (TextView) findViewById(R.id.cook_book_remark);
        tvTitleRemark = (TextView) findViewById(R.id.title_remark);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData(){
        cookBookBean = (CookBookBean) getIntent().getBundleExtra("bundle").getSerializable("cookBookInfo");
        tvName.setText(cookBookBean.getName());
        tvSeasoning.setText(cookBookBean.getSeasoning());
        tvMethod.setText(cookBookBean.getMethod());
        if (TextUtils.isEmpty(cookBookBean.getRemark())) {
            tvTitleRemark.setVisibility(View.GONE);
        } else {
            tvTitleRemark.setVisibility(View.VISIBLE);
            tvRemark.setText(cookBookBean.getRemark());
        }
    }
}
