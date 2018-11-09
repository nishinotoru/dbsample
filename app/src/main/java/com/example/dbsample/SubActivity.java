package com.example.dbsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * メイン画面に関連するクラス
 * MainActivity
 */
public class SubActivity extends AppCompatActivity
        //implements
        //RadioGroup.OnCheckedChangeListener
{

    private EditText mEditText01xProduct;        // 品名
    private EditText mEditText01xMadeIn;         // 品名
    private EditText mEditText01xNumber;         // 個数
    private EditText mEditText01xPrice;          // 単価

    private TextView mText01xKome01;             // 品名の※印
    private TextView mText01xKome02;             // 産地の※印
    private TextView mText01xKome03;             // 個数の※印
    private TextView mText01xKome04;             // 単価の※印

    private Button mButton01xRegist;             // 登録ボタン
    //private Button mButton01xShow;               // 表示ボタン

    //private RadioGroup mRadioGroup01xShow;       // 選択用ラジオボタングループ

    //private Intent intent;                      // インテント

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        findViews();        // 各部品の結びつけ処理

        init();             //初期値設定

        // ラジオボタン選択時
        //mRadioGroup01xShow.setOnCheckedChangeListener(this);

        // 登録ボタン押下時処理
        mButton01xRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // キーボードを非表示
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                EditText textInput = (EditText)findViewById(R.id.editText01xProduct);
                Intent intent = new Intent();
                intent.putExtra("SUB_INPUT_STRING", textInput.getText().toString());
                setResult(RESULT_OK,intent);

                finish();

                // DBに登録
                //saveList();

            }
        });

        // 表示ボタン押下時処理
        //mButton01xShow.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {
              //  if (intent != null) {
                //    startActivity(intent);      // 各画面へ遷移
                //} else {
                 //   Toast.makeText(SubActivity.this, "ラジオボタンが選択されていません。", Toast.LENGTH_SHORT).show();
               // }

            //}
       // });
    }


    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {

        mEditText01xProduct = (EditText) findViewById(R.id.editText01xProduct);   // 品名
        mEditText01xMadeIn = (EditText) findViewById(R.id.editText01xMadeIn);     // 産地
        mEditText01xNumber = (EditText) findViewById(R.id.editText01xNumber);     // 個数
        mEditText01xPrice = (EditText) findViewById(R.id.editText01xPrice);       // 単価

        mText01xKome01 = (TextView) findViewById(R.id.text01xKome01);             // 品名の※印
        mText01xKome02 = (TextView) findViewById(R.id.text01xKome02);             // 産地※印
        mText01xKome03 = (TextView) findViewById(R.id.text01xKome03);             // 個数の※印
        mText01xKome04 = (TextView) findViewById(R.id.text01xKome04);             // 単価の※印

        mButton01xRegist = (Button) findViewById(R.id.button01xRegist);           // 登録ボタン
        //mButton01xShow = (Button) findViewById(R.id.button01xShow);               // 表示ボタン

        //mRadioGroup01xShow = (RadioGroup) findViewById(R.id.radioGroup01x);       // 選択用ラジオボタングループ

    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {
        mEditText01xProduct.setText("");
        mEditText01xMadeIn.setText("");
        mEditText01xNumber.setText("");
        mEditText01xPrice.setText("");

        mText01xKome01.setText("");
        mText01xKome02.setText("");
        mText01xKome03.setText("");
        mText01xKome04.setText("");
        mEditText01xProduct.requestFocus();      // フォーカスを品名のEditTextに指定
    }

    /**
     * ラジオボタン選択処理
     * onCheckedChanged()
     */
    //@Override
    //public void onCheckedChanged(RadioGroup group, int checkedId) {
 //       switch (checkedId) {
 //           case R.id.radioButton01Product:         // 品名一覧(ListView×ArrayAdapter)を選択した場合
 //               intent = new Intent(SubActivity.this, SelectSheetProduct.class);
 //               break;
 //           case R.id.radioButton01ListView:        // ListView表示を選択した場合
 //               intent = new Intent(SubActivity.this, SelectSheetListView.class);
 //               break;
 //           case R.id.radioButton01TableLayout:     // TableLayout表示を選択した場合
 //               intent = new Intent(SubActivity.this, SelectSheetTable.class);
 //               break;
 //       }
    //}

    /**
     * EditTextに入力したテキストをDBに登録
     * saveDB()
     */
    private void saveList() {

        // 各EditTextで入力されたテキストを取得
        String strProduct = mEditText01xProduct.getText().toString();
        String strMadeIn = mEditText01xMadeIn.getText().toString();
        String strNumber = mEditText01xNumber.getText().toString();
        String strPrice = mEditText01xPrice.getText().toString();

        // EditTextが空白の場合
        if (strProduct.equals("") || strMadeIn.equals("") || strNumber.equals("") || strPrice.equals("")) {

            if (strProduct.equals("")) {
                mText01xKome01.setText("※");     // 品名が空白の場合、※印を表示
            } else {
                mText01xKome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strMadeIn.equals("")) {
                mText01xKome02.setText("※");     // 産地が空白の場合、※印を表示
            } else {
                mText01xKome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strNumber.equals("")) {
                mText01xKome03.setText("※");     // 個数が空白の場合、※印を表示
            } else {
                mText01xKome03.setText("");      // 空白でない場合は※印を消す
            }

            if (strPrice.equals("")) {
                mText01xKome04.setText("※");     // 単価が空白の場合、※印を表示
            } else {
                mText01xKome04.setText("");      // 空白でない場合は※印を消す
            }

            Toast.makeText(SubActivity.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された単価と個数は文字列からint型へ変換
            int iNumber = Integer.parseInt(strNumber);
            int iPrice = Integer.parseInt(strPrice);

            // DBへの登録処理
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();                                         // DBの読み書き
            dbAdapter.saveDB(strProduct, strMadeIn, iNumber, iPrice);   // DBに登録
            dbAdapter.closeDB();                                        // DBを閉じる

            init();     // 初期値設定

        }

    }

    // メニュー未使用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // メニュー未使用
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}