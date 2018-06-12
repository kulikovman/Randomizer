package ru.kulikovman.randomizer;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class NumberPickerDialog extends DialogFragment {
    private TextView mStartLimitField, mEndLimitField;
    private int mStartLimit, mEndLimit;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("log", "NumberPickerDialog запущен");

        // Получаем поля лимитов и их значения
        mStartLimitField = getActivity().findViewById(R.id.start_limit_field);
        mEndLimitField = getActivity().findViewById(R.id.end_limit_field);

        mStartLimit = Integer.parseInt(mStartLimitField.getText().toString());
        mEndLimit = Integer.parseInt(mEndLimitField.getText().toString());

        // Привязка к диалогу кастомного макета
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View numberPickerLayout = inflater.inflate(R.layout.number_picker_layout, null);

        // Настраиваем NumberPicker 1
        final NumberPicker numberPicker1 = numberPickerLayout.findViewById(R.id.number_picker_1);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(9);
        numberPicker1.setWrapSelectorWheel(true);

        // Настраиваем NumberPicker 2
        final NumberPicker numberPicker2 = numberPickerLayout.findViewById(R.id.number_picker_2);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(9);
        numberPicker2.setWrapSelectorWheel(true);

        // Настраиваем NumberPicker 3
        final NumberPicker numberPicker3 = numberPickerLayout.findViewById(R.id.number_picker_3);
        numberPicker3.setMinValue(0);
        numberPicker3.setMaxValue(9);
        numberPicker3.setWrapSelectorWheel(true);

        // Создаем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выбор")
                .setMessage("Максимального значения")
                .setView(numberPickerLayout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num1 = numberPicker1.getValue();
                        int num2 = numberPicker2.getValue();
                        int num3 = numberPicker3.getValue();

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
