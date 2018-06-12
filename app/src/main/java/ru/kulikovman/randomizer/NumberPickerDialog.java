package ru.kulikovman.randomizer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Objects;

public class NumberPickerDialog extends DialogFragment {
    private TextView mStartLimitField, mEndLimitField;
    private int mStartLimit, mEndLimit;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("log", "NumberPickerDialog запущен");

        // Получаем поля лимитов и их значения
        mStartLimitField = Objects.requireNonNull(getActivity()).findViewById(R.id.start_limit_field);
        mEndLimitField = Objects.requireNonNull(getActivity()).findViewById(R.id.end_limit_field);

        mStartLimit = Integer.parseInt(mStartLimitField.getText().toString());
        mEndLimit = Integer.parseInt(mEndLimitField.getText().toString());

        // Привязка к диалогу кастомного макета
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View numberPicker = inflater.inflate(R.layout.number_picker, null);

        // Настраиваем NumberPicker 1
        final NumberPicker picker1 = numberPicker.findViewById(R.id.picker_1);
        picker1.setMinValue(0);
        picker1.setMaxValue(9);
        picker1.setWrapSelectorWheel(true);

        // Настраиваем NumberPicker 2
        final NumberPicker picker2 = numberPicker.findViewById(R.id.picker_2);
        picker2.setMinValue(0);
        picker2.setMaxValue(9);
        picker2.setWrapSelectorWheel(true);

        // Настраиваем NumberPicker 3
        final NumberPicker picker3 = numberPicker.findViewById(R.id.picker_3);
        picker3.setMinValue(0);
        picker3.setMaxValue(9);
        picker3.setWrapSelectorWheel(true);

        // Создаем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_max_message)
                .setView(numberPicker)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num1 = picker1.getValue();
                        int num2 = picker2.getValue();
                        int num3 = picker3.getValue();

                        // Получаем выбранное число
                        int combinedValue = getCombinedValue(num1, num2, num3);

                        // Проверка числа на корректность
                        if (combinedValue > mStartLimit) {
                            mEndLimitField.setText(String.valueOf(combinedValue));
                        }
                    }
                });

        return builder.create();
    }

    private int getCombinedValue(int num1, int num2, int num3) {
        String number = "" + num1 + num2 + num3;
        return Integer.parseInt(number);
    }
}
