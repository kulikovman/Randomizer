package ru.kulikovman.randomizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

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

        // Востанавливаем последние значения лимита и результата
        mLimit = mSharedPref.getInt(getString(R.string.limit), 10);
        mRandomResult = mSharedPref.getInt(getString(R.string.random_result), 1);

        mNumberField.setText(String.valueOf(mLimit));
        mBigButton.setText(String.valueOf(mRandomResult));

        Log.d("myLog", "Восстановили значения лимита и результата: " + mLimit + "|" + mRandomResult);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Сохраняем значения лимита и результата
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(getString(R.string.limit), mLimit);
        editor.putInt(getString(R.string.random_result), mRandomResult);
        editor.apply();

        Log.d("myLog", "Сохранили значения лимита и результата: " + mLimit + "|" + mRandomResult);
    }

    public void getRandomNumber(View view) {
        // Воспроизводим звук короткого щелчка при нажатии
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.roll_dice);
        //mediaPlayer.start();

        // Генерируем случайное число в заданном пределе
        Random random = new Random();
        mRandomResult = 1 + random.nextInt(mLimit);

        // Устанавливаем полученный результат на кнопку
        mBigButton.setText(String.valueOf(mRandomResult));

        Log.d("myLog", "Сгенерировали и установили рандомное число: " + mRandomResult);
    }
}
