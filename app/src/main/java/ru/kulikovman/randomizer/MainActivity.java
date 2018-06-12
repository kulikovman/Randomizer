package ru.kulikovman.randomizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mSharedPref;
    
    private SoundPool mSoundPool;
    private int mBigButtonSound;

    private TextView mStartLimitField, mEndLimitField;
    private ImageView mNumber1, mNumber2, mNumber3;
    
    private int mStartLimit, mEndLimit;
    private int mResult;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);

        Log.d("log", "Запущен onCreate");

        // Инициализируем необходимые вью элементы
        mStartLimitField = findViewById(R.id.start_limit_field);
        mEndLimitField = findViewById(R.id.end_limit_field);
        mNumber1 = findViewById(R.id.image_number_1);
        mNumber2 = findViewById(R.id.image_number_2);
        mNumber3 = findViewById(R.id.image_number_3);

        // Получаем SharedPreferences
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);

        // Востанавливаем значения лимита и результата
        mStartLimit = mSharedPref.getInt(getString(R.string.start_limit), 1);
        mEndLimit = mSharedPref.getInt(getString(R.string.end_limit), 999);
        mResult = mSharedPref.getInt(getString(R.string.result), 279);

        Log.d("log", "Восстановили значения лимита и результата: " + mStartLimit + " - " + mEndLimit + " | " + mResult);

        // Выводим значения на экран
        mStartLimitField.setText(String.valueOf(mStartLimit));
        mEndLimitField.setText(String.valueOf(mEndLimit));
        setRandomNumber();

        // Создаем SoundPool
        //createSoundPool();
    }

    private void setRandomNumber() {
        // Сначала скрываем все картинки
        mNumber1.setVisibility(View.GONE);
        mNumber2.setVisibility(View.GONE);
        mNumber3.setVisibility(View.GONE);

        // Потом показываем только нужные картинки

        // Формируем массив чисел из которых состоит результат
        ArrayList<Integer> list = new ArrayList<>();

        int temp = mResult;
        while(temp > 0) {
            list.add(temp % 10);
            temp /= 10;
        }

        Collections.reverse(list);

        // Переносим результат на экран
        for (int i = 0; i < list.size(); i++) {
            int number = list.get(i);

            if (i == 0) {
                setImageNumber(mNumber1, number);
            } else if (i == 1) {
                setImageNumber(mNumber2, number);
            } else if (i == 2) {
                setImageNumber(mNumber3, number);
            }
        }


        /*for (int number : list) {


            Log.d("log", String.valueOf(number));
        }*/


    }

    private void setImageNumber(ImageView imageView, int number) {
        // Делаем вью видимым
        imageView.setVisibility(View.VISIBLE);

        // Формируем имя ресурса картинки - num_p_7
        String numberImage = "num_p_" + String.valueOf(number);

        // Получаем идентификатор и присваиваем картинку
        int numberImageId = getResources().getIdentifier(numberImage, "drawable", getPackageName());
        imageView.setImageResource(numberImageId);
    }



    /*@Override
    protected void onPause() {
        super.onPause();

        Log.d("log", "Запущен onPause");

        // Сохраняем значения лимита и результата
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(getString(R.string.start_limit), mLimit);
        editor.putInt(getString(R.string.result), mResult);
        editor.apply();

        Log.d("log", "Сохранили значения лимита и результата: " + mLimit + "|" + mResult);

        // Очищаем SoundPool
        clearSoundPool();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("log", "Запущен onResume");

        // Создаем SoundPool
        createSoundPool();
    }

    @SuppressWarnings("deprecation")
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

            Log.d("log", "Создали SoundPool");

            // Получаем id звуковых файлов
            loadIdSounds();
        }
    }

    private void loadIdSounds() {
        mBigButtonSound = mSoundPool.load(this, R.raw.tap_button, 1);

        Log.d("log", "Получили id звуковых файлов");
    }

    private void clearSoundPool() {
        mSoundPool.release();
        mSoundPool = null;

        Log.d("log", "SoundPool очищен");
    }

    public void getRandomNumber(View v) {
        // Обновляем лимит расчета
        updateLimit();

        // Перемещаем курсор в конец поля
        moveCursorToEnd();

        // Прячем клавиатуру
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        // Воспроизводим звук короткого щелчка при нажатии
        mSoundPool.play(mBigButtonSound, 1, 1, 1, 0, 1);

        // Генерируем случайное число в заданном пределе
        Random random = new Random();
        mResult = 1 + random.nextInt(mLimit);

        // Устанавливаем полученный результат на кнопку
        mBigButton.setText(String.valueOf(mResult));

        Log.d("log", "Сгенерировали рандомное число: " + mResult);
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
    }*/
}
