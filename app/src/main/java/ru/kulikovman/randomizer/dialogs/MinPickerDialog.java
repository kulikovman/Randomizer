package ru.kulikovman.randomizer.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import ru.kulikovman.randomizer.R;

public class MinPickerDialog extends DialogFragment {
    private TextView mStartLimitField, mEndLimitField;
    private int mStartLimit, mEndLimit;

    private View mNumberPicker;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Получаем поля лимитов и их значения
        mStartLimitField = Objects.requireNonNull(getActivity()).findViewById(R.id.start_limit_field);
        mEndLimitField = Objects.requireNonNull(getActivity()).findViewById(R.id.end_limit_field);
        mStartLimit = Integer.parseInt(mStartLimitField.getText().toString());
        mEndLimit = Integer.parseInt(mEndLimitField.getText().toString());

        // Привязка к диалогу кастомного макета
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mNumberPicker = inflater.inflate(R.layout.number_picker, null);

        // Массив значений для пикеров
        ArrayList<Integer> numberList = createListNumber();

        // Настраиваем пикеры
        final NumberPicker picker1 = createPicker(R.id.picker_1, numberList.get(0));
        final NumberPicker picker2 = createPicker(R.id.picker_2, numberList.get(1));
        final NumberPicker picker3 = createPicker(R.id.picker_3, numberList.get(2));

        // Создаем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_min_message)
                .setView(mNumberPicker)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num1 = picker1.getValue();
                        int num2 = picker2.getValue();
                        int num3 = picker3.getValue();

                        // Получаем выбранное число
                        int combinedValue = getCombinedValue(num1, num2, num3);

                        // Проверка числа на корректность
                        if (combinedValue < mEndLimit) {
                            mStartLimitField.setText(String.valueOf(combinedValue));
                        }
                    }
                });

        return builder.create();
    }

    private NumberPicker createPicker(int resId, Integer defaultValue) {
        NumberPicker picker = mNumberPicker.findViewById(resId);
        picker.setMinValue(0);
        picker.setMaxValue(9);
        picker.setValue(defaultValue);

        return picker;
    }

    private ArrayList<Integer> createListNumber() {
        // Делаем число трехзначным
        StringBuilder number = new StringBuilder(String.valueOf(mStartLimit));
        while (number.length() < 3) {
            number.insert(0, "0");
        }

        // Извлекаем отдельные цифры
        ArrayList<Integer> list = new ArrayList<>();
        list.add(Integer.parseInt(number.substring(0, 1)));
        list.add(Integer.parseInt(number.substring(1, 2)));
        list.add(Integer.parseInt(number.substring(2, 3)));

        return list;
    }

    private int getCombinedValue(int num1, int num2, int num3) {
        String number = "" + num1 + num2 + num3;
        return Integer.parseInt(number);
    }
}
