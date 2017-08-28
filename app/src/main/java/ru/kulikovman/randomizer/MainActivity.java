package ru.kulikovman.randomizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mNumberField;
    private Button mBigButton;

    private SharedPreferences mSharedPref;

    private int mLimit;
    private int mRandomResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем необходимые вью элементы
        mNumberField = (EditText) findViewById(R.id.number_field);
        mBigButton = (Button) findViewById(R.id.big_button);

        // Получаем SharedPreferences
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);

        // Востанавливаем последние значения лимита и кнопки
        mLimit = mSharedPref.getInt(getString(R.string.number_field), 10);
        mRandomResult = mSharedPref.getInt(getString(R.string.big_button), 1);

        mNumberField.setText(String.valueOf(mLimit));
        mBigButton.setText(String.valueOf(mRandomResult));

        Log.d("myLog", "Восстановили значение лимита и кнопки: " + mLimit + " | " + mRandomResult);
    }

    public void getRandomNumber(View view) {

    }
}
