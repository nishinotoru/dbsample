package com.example.dbsample;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dbsample.R;

import com.densowave.barcode.*;
import com.densowave.barcode.decoder.*;
import com.densowave.barcode.decoderparams.*;


/**
 * メイン画面に関連するクラス
 * MainActivity
 */
public class MainActivity extends AppCompatActivity implements
        RadioGroup.OnCheckedChangeListener {

    private EditText mEditText01Product;        // バー


    private EditText mEditText01User;         // ユーザ
    private EditText mEditText01Date;         // 日付
    private EditText mEditText01Memo;          // メモ

    private TextView mText01Kome01;             // ユーザの※印
    private TextView mText01Kome02;             // 日付の※印
    private TextView mText01Kome03;             // メモの※印

    private TextView mSUB;             // 単価の※印


    private Button mButton01Regist;             // 登録ボタン
    private Button mButton01Show;               // 表示ボタン

    private Button mButton01Fin;               // 終了ボタン


    private RadioGroup mRadioGroup01Show;       // 選択用ラジオボタングループ

    private Intent intent;                      // インテント


    private static final int SUBACTIVITY = 1;

    private IntentFilter filter;




    ReaderManager m_RM = null;

//デンソーバーコードスキャンイベント受信後処理
    private final BroadcastReceiver myDataReceiver = new
            BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(GeneralString.Intent_PASS_TO_APP))
                    {
// Fetch data from the intent
                        String sDataStr = intent.getStringExtra(GeneralString.BcReaderData);
                        //Toast.makeText(MainActivity.this, "Decoded data is " + sDataStr,
                        //        Toast.LENGTH_SHORT).show();
                        TextView mEditText01Product = (TextView)findViewById(R.id.editText01Product);
                        mEditText01Product.setText(intent.getStringExtra(GeneralString.BcReaderData));

                        // DBに登録
                        saveList();



                    }}
            };

//デンソーバーコードスキャンイベント読み取り処理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_RM = ReaderManager.InitInstance(this);
        filter = new IntentFilter();
        filter.addAction(GeneralString.Intent_PASS_TO_APP);
        registerReceiver(myDataReceiver, filter);






        intent = new Intent(MainActivity.this, SubActivity.class);
        //startActivity(intent);      // 各画面へ遷移

        startActivityForResult(intent, SUBACTIVITY);

        findViews();        // 各部品の結びつけ処理

        init();             //初期値設定


        // ラジオボタン選択時
        mRadioGroup01Show.setOnCheckedChangeListener(this);

        // 登録ボタン押下時処理
        mButton01Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // キーボードを非表示
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // DBに登録
                saveList();

            }
        });

        // 表示ボタン押下時処理
        mButton01Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent != null) {
                    startActivity(intent);      // 各画面へ遷移
                } else {
                    Toast.makeText(MainActivity.this, "ラジオボタンが選択されていません。", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 表示ボタン押下時処理
        mButton01Fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        TextView label1 = (TextView)findViewById(R.id.text01Kome01);
        TextView label2 = (TextView)findViewById(R.id.text01Kome02);
        TextView label3 = (TextView)findViewById(R.id.text01Kome03);



        if (requestCode == SUBACTIVITY){
            if (resultCode == RESULT_OK){
                Bundle extras = intent.getExtras();
                label1.setText(extras.getString("SUB_INPUT_STRING1"));
                mText01Kome01 = (TextView) findViewById(R.id.text01Kome01);

                label2.setText(extras.getString("SUB_INPUT_STRING2"));
                mText01Kome02 = (TextView) findViewById(R.id.text01Kome02);

                label3.setText(extras.getString("SUB_INPUT_STRING3"));
                mText01Kome03 = (TextView) findViewById(R.id.text01Kome03);


            }
        }
    }




    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {

        mEditText01Product = (EditText) findViewById(R.id.editText01Product);   // バー

        mButton01Regist = (Button) findViewById(R.id.button01Regist);           // 登録ボタン
        mButton01Show = (Button) findViewById(R.id.button01Show);               // 表示ボタン

        mButton01Fin = (Button) findViewById(R.id.button01Fin);               // 終了ボタン


        mRadioGroup01Show = (RadioGroup) findViewById(R.id.radioGroup01);       // 選択用ラジオボタングループ

    }

    /**
     * 初期値設定 (EditTextの入力欄は空白、※印は消す)
     * init()
     */
    private void init() {
        mEditText01Product.setText("");
        //mEditText01MadeIn.setText("");
        //mEditText01Number.setText("");
        //mEditText01Price.setText("");

        //mText01Kome01.setText("");
        //mText01Kome02.setText("");
        //mText01Kome03.setText("");
        //mText01Kome04.setText("");
        mEditText01Product.requestFocus();      // フォーカスを品名のEditTextに指定
    }

    /**
     * ラジオボタン選択処理
     * onCheckedChanged()
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            //case R.id.radioButton01Product:         // 品名一覧(ListView×ArrayAdapter)を選択した場合
            //    intent = new Intent(MainActivity.this, SelectSheetProduct.class);
            //    break;
            case R.id.radioButton01ListView:        // ListView表示を選択した場合
                intent = new Intent(MainActivity.this, SelectSheetListView.class);
                break;
            //case R.id.radioButton01TableLayout:     // TableLayout表示を選択した場合
            //    intent = new Intent(MainActivity.this, SelectSheetTable.class);
            //    break;
        }
    }

    /**
     * EditTextに入力したテキストをDBに登録
     * saveDB()
     */
    private void saveList() {

        // 各EditTextで入力されたテキストを取得


        String strProduct = mEditText01Product.getText().toString();
        String strUser = mText01Kome01.getText().toString();
        String strDate = mText01Kome02.getText().toString();
        String strMemo = mText01Kome03.getText().toString();



        //String strMadeIn = mEditText01MadeIn.getText().toString();
        //String strNumber = mEditText01Number.getText().toString();
        //String strPrice = mEditText01Price.getText().toString();

        // EditTextが空白の場合
        //if (strProduct.equals("") )) {

            //if (strProduct.equals("")) {
              //  mText01Kome01.setText("※");     // 品名が空白の場合、※印を表示
            //} else {
            //    mText01Kome01.setText("");      // 空白でない場合は※印を消す
            //}

            //if (strMadeIn.equals("")) {
            //    mText01Kome02.setText("※");     // 産地が空白の場合、※印を表示
            //} else {
            //    mText01Kome02.setText("");      // 空白でない場合は※印を消す
           // }

            //if (strNumber.equals("")) {
            //    mText01Kome03.setText("※");     // 個数が空白の場合、※印を表示
            //} else {
            //    mText01Kome03.setText("");      // 空白でない場合は※印を消す
            //}

            //
            //Toast.makeText(MainActivity.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        //} else {        // EditTextが全て入力されている場合

            // 入力された単価と個数は文字列からint型へ変換
            //int iNumber = Integer.parseInt(strNumber);
            //int iPrice = Integer.parseInt(strPrice);

            // DBへの登録処理
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.openDB();                                         // DBの読み書き
            dbAdapter.saveDB(strProduct, strUser, strDate, strMemo);   // DBに登録
            dbAdapter.closeDB();                                        // DBを閉じる

            init();     // 初期値設定

        //}

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



