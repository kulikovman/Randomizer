package ru.kulikovman.randomizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSharedPref;
    private EditText mLimitField;
    private Button mBigButton;

    private SoundPool mSoundPool;
    private int mBigButtonSound;

    private int mLimit;
    private int mRandomResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("myLog", "Запущен onCreate");

        // Инициализируем необходимые вью элементы
        mLimitField = (EditText) findViewById(R.id.limit_field);
        mBigButton = (Button) findViewById(R.id.big_button);

        // Получаем SharedPreferences
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);

        // Востанавливаем последние значения лимита и результата
        mLimit = mSharedPref.getInt(getString(R.string.limit), 10);
        mRandomResult = mSharedPref.getInt(getString(R.string.random_result), 1);
        mLimitField.setText(String.valueOf(mLimit));
        mBigButton.setText(String.valueOf(mRandomResult));

        Log.d("myLog", "Восстановили значения лимита и результата: " + mLimit + "|" + mRandomResult);

        // Создаем SoundPool
        createSoundPool();

        // Перемещаем курсор в конец поля
        moveCursorToEnd();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("myLog", "Запущен onPause");

        // Сохраняем значения лимита и результата
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(getString(R.string.limit), mLimit);
        editor.putInt(getString(R.string.random_result), mRandomResult);
        editor.apply();

        Log.d("myLog", "Сохранили значения лимита и результата: " + mLimit + "|" + mRandomResult);

        // Очищаем SoundPool
        clearSoundPool();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("myLog", "Запущен onResume");

        // Создаем SoundPool
        createSoundPool();
    }

    private void createSoundPool() {
        if (mSoundPool == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Создаем SoundPool для Android API 21 и выше
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();

                mSoundPool = new SoundPool.Builder()
                        .setAudioAttributes(attributes)
                        .build();
            } else {
                // Создаем SoundPool для старых версий Android
                mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            }

            Log.d("myLog", "Создали SoundPool");

            // Получаем id звуковых файлов
            loadIdSounds();
        }
    }

    private void loadIdSounds() {
        mBigButtonSound = mSoundPool.load(this, R.raw.tap_button, 1);

        Log.d("myLog", "Получили id звуковых файлов");
    }

    private void clearSoundPool() {
        mSoundPool.release();
        mSoundPool = null;

        Log.d("myLog", "SoundPool очищен");
    }

    /*// Создаем SoundPool для Android API 21 и выше
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
    }*/

    public void getRandomNumber(View v) {
        // Обновляем лимит расчета
        updateLimit();

        // Перемещаем курсор в конец поля
        moveCursorToEnd();

        // Прячем клавиатуру
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // Воспроизводим звук короткого щелчка при нажатии
        mSoundPool.play(mBigButtonSound, 1, 1, 1, 0, 1);

        // Генерируем случайное число в заданном пределе
        Random random = new Random();
        mRandomResult = 1 + random.nextInt(mLimit);

        // Устанавливаем полученный результат на кнопку
        mBigButton.setText(String.valueOf(mRandomResult));

        Log.d("myLog", "Сгенерировали рандомное число: " + mRandomResult);
    }

    private void updateLimit() {
        String temp = mLimitField.getText().toString();

        while (temp.startsWith("0")) {
            temp = temp.substring(1);
        }

        if (temp.length() > 0) {
            mLimit = Integer.parseInt(temp);
        }

        mLimitField.setText(String.valueOf(mLimit));
    }

    private void moveCursorToEnd() {
        mLimitField.setSelection(mLimitField.getText().length());
    }
}
