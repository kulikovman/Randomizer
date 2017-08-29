package ru.kulikovman.randomizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSharedPref;
    private EditText mNumberField;
    private Button mBigButton;

    private SoundPool mSoundPool;
    private int mBigButtonSound;

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

        // Создаем SoundPool в зависимости от используемой версии Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }

        Log.d("myLog", "Создали SoundPool");

        // Сохраняем id звукового файла
        mBigButtonSound = mSoundPool.load(this, R.raw.tap_button, 1);

        Log.d("myLog", "Сохранили id звукового файла");
    }

    // Создаем SoundPool для Android API 21 и выше
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    // Создаем SoundPool для старых версий Android
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool() {
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
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

        // Очищаем SoundPool
        mSoundPool.release();
        mSoundPool = null;

        Log.d("myLog", "SoundPool очищен");
    }

    public void getRandomNumber(View view) {
        // Воспроизводим звук короткого щелчка при нажатии
        mSoundPool.play(mBigButtonSound, 1, 1, 1, 0, 1);

        // Генерируем случайное число в заданном пределе
        Random random = new Random();
        mRandomResult = 1 + random.nextInt(mLimit);

        // Устанавливаем полученный результат на кнопку
        mBigButton.setText(String.valueOf(mRandomResult));

        Log.d("myLog", "Сгенерировали и установили рандомное число: " + mRandomResult);
    }
}
