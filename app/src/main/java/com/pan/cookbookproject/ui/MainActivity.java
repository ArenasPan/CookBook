package com.pan.cookbookproject.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liyu.sqlitetoexcel.SqliteToExcel;
import com.pan.cookbookproject.R;
import com.pan.cookbookproject.adapter.CookBookAdapter;
import com.pan.cookbookproject.bean.CookBookBean;
import com.pan.cookbookproject.database.CookBookDatabase;
import com.pan.cookbookproject.database.DatabaseHelper;
import com.pz.actionsheetlibrary.NewActionSheet;

import java.util.Arrays;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, NewActionSheet.MenuItemClickListener {

    private static final int REQUEST_TO_ADD = 1;
    private static final int REQUEST_TO_CHANGE = 2;

    private static final List<String> CLASSIFY = Arrays.asList("名称", "原料", "做法", "备注");

    private ImageView ivAdd;
    private ImageView ivSearch;
    private TextView tvNoData;
    private RecyclerView recyclerView;
    private EditText etSearch;
    private TextView tvExport;

    private RelativeLayout rlSearch;
    private LinearLayout llSearchClassify;
    private TextView tvSearchClassify;

    private List<CookBookBean> cookBookBeen;
    private CookBookAdapter adapter;
    private CookBookDatabase cookBookDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView(){
        ivAdd = (ImageView) findViewById(R.id.add);
        ivSearch = (ImageView) findViewById(R.id.search_button);
        tvNoData = (TextView) findViewById(R.id.text_no_data);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        etSearch = (EditText) findViewById(R.id.et_search);
        tvExport = (TextView) findViewById(R.id.tv_export);

        rlSearch = (RelativeLayout) findViewById(R.id.layout_search);
        llSearchClassify = (LinearLayout) findViewById(R.id.layout_search_classify);
        tvSearchClassify = (TextView) findViewById(R.id.tv_search_classify);

        rlSearch.setVisibility(View.GONE);
        llSearchClassify.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvExport.setOnClickListener(this);
    }

    private void initData() {
        cookBookDatabase = new CookBookDatabase(this);
        cookBookBeen = cookBookDatabase.query();
        if (cookBookBeen == null || cookBookBeen.size() == 0) {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        adapter = new CookBookAdapter(this, cookBookBeen);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CookBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                showCustomDialog(position);
            }
        });
    }

    private void showCustomDialog(final int position) {
//        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_choose, null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView tvToDetail = (TextView) dialogView.findViewById(R.id.to_detail);
        tvToDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDetailActivity(position);
                dialog.dismiss();
            }
        });

        TextView tvChange = (TextView) dialogView.findViewById(R.id.to_change_info);
        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toChangeActivity(position);
                dialog.dismiss();
            }
        });

        TextView tvDelete = (TextView) dialogView.findViewById(R.id.to_delete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCookbook(position);
                dialog.dismiss();
            }
        });
    }

    private void toDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cookBookInfo",cookBookBeen.get(position));
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    private void toChangeActivity(int position){
        Intent intent = new Intent(this, ChangeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("cookBookInfo",cookBookBeen.get(position));
        intent.putExtra("bundle", bundle);
        startActivityForResult(intent, REQUEST_TO_CHANGE);
    }

    private void deleteCookbook(int position) {
        cookBookDatabase.delete(cookBookBeen.get(position));
        cookBookBeen.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                startActivityForResult(new Intent(this, AddActivity.class), REQUEST_TO_ADD);
                break;
            case R.id.search_button:
                if (rlSearch.getVisibility() == View.GONE) {
                    rlSearch.setVisibility(View.VISIBLE);
                } else {
                    searchCookBook(etSearch.getText().toString(),tvSearchClassify.getText().toString());
                }
                break;
            case R.id.tv_export:
                if (cookBookBeen == null || cookBookBeen.size()==0) {
                    Toast.makeText(MainActivity.this,"当前无数据",Toast.LENGTH_SHORT).show();
                }
                saveAsExcel();
                break;
            case R.id.layout_search_classify:
                chooseSearchClassify();
                break;
        }
    }

    private void saveAsExcel() {
        SqliteToExcel sqliteToExcel = new SqliteToExcel(this, DatabaseHelper.DATABASE_NAME);
        sqliteToExcel.startExportAllTables("tupperware_cookbook.xls", new SqliteToExcel.ExportListener() {

            @Override
            public void onStart() {
                Log.i("export_to_excel", "onStart");
                Toast.makeText(MainActivity.this,"正在导出excel数据",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Log.i("export_to_excel", "onError");
                Toast.makeText(MainActivity.this,"导出数据错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Log.i("export_to_excel", "onComplete");
                Toast.makeText(MainActivity.this,"导出成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void searchCookBook(String word,String classify) {
        rlSearch.setVisibility(View.GONE);
        int classifyId = 0;
        switch (classify) {
            case "名称":
                classifyId = 0;
                break;
            case "原料":
                classifyId = 1;
                break;
            case "做法":
                classifyId = 2;
                break;
            case "备注":
                classifyId = 3;
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(word)) {
            cookBookBeen = cookBookDatabase.query();
            adapter.cookBookBeen = cookBookBeen;
            adapter.notifyDataSetChanged();
        } else {
            cookBookBeen = cookBookDatabase.fuzzyQuery(word, classifyId);
            if (cookBookBeen == null || cookBookBeen.size() == 0) {
                Toast.makeText(this, "未找到搜索的菜谱", Toast.LENGTH_SHORT).show();
            } else {
                adapter.cookBookBeen = cookBookBeen;
                adapter.notifyDataSetChanged();
            }
        }
        etSearch.setText("");
    }

    private void chooseSearchClassify() {
        NewActionSheet as = new NewActionSheet(this);
        as.addItems(CLASSIFY);
        as.setItemClickListener(this);
        as.setCancelableOnTouchMenuOutside(true);
        as.showMenu();
    }

    private void updateRecyclerList() {
        if (cookBookBeen == null || cookBookBeen.size() == 0) {
            if (recyclerView.getVisibility() == View.GONE) {
                recyclerView.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
            }
            cookBookBeen = cookBookDatabase.query();
            adapter = new CookBookAdapter(this, cookBookBeen);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new CookBookAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(View view, int position) {
                    showCustomDialog(position);
                }
            });
        } else {
            cookBookBeen = null;
            cookBookBeen = cookBookDatabase.query();
            adapter.cookBookBeen = cookBookBeen;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TO_ADD:
            case REQUEST_TO_CHANGE:
                boolean refresh = data.getBooleanExtra("add", false);
                if (refresh) {
                    updateRecyclerList();
                }
                break;
        }
    }


    @Override
    public void onItemClick(String tag, int itemPosition) {
        tvSearchClassify.setText(CLASSIFY.get(itemPosition));
    }

    @Override
    public void onCancelClick(String tag) {

    }

    @Override
    public void onItemClickAfterAnimation(String tag, int itemPosition) {

    }

    @Override
    public void onCancelClickAfterAnimation(String tag) {

    }
}
