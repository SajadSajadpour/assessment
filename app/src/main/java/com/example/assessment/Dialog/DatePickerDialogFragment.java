package com.example.assessment.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.assessment.Activity.SearchActivity;
import com.example.assessment.Util.DataUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final String LOG_TAG = DatePickerDialogFragment.class.getSimpleName();

    static final String TAG_BEGIN_DATE = "DatePickerBeginDate";
    static final String TAG_END_DATE = "DatePickerEndDate";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Set date picker to today's date, except if there's already a date set in EditText
        TextView et = getEditText();
        Calendar c = new GregorianCalendar();
        if (et.getTag() != null) c.setTime(((DataUtil) et.getTag()).getDate());
        return new DatePickerDialog(getActivity(), this,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }

    // Called when the user selected a date from the date picker dialog
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Write date into EditText and associate a DataUtil object with the same EditText
        DataUtil date = new DataUtil(year, month, dayOfMonth);
        TextView et = getEditText();
        et.setText(date.format3());
        et.setTag(date);
        CheckBox cb = getCheckBox();
        if (!cb.isChecked()) cb.setChecked(true);
    }

    // Called when the user cancelled the date picker dialog
    @Override
    public void onCancel(DialogInterface dialog) {
    }

    private TextView getEditText() {
        FilterDialogFragment f = (FilterDialogFragment) getActivity().getFragmentManager().findFragmentByTag(SearchActivity.TAG_FILTER_DIALOG);
        switch (getTag()) {
            case TAG_BEGIN_DATE:
                return f.b.etBeginDate;
            case TAG_END_DATE:
                return f.b.etEndDate;
            default:
                new Exception().printStackTrace();
                return null;
        }
    }

    private CheckBox getCheckBox() {
        FilterDialogFragment f = (FilterDialogFragment) getActivity().getFragmentManager().findFragmentByTag(SearchActivity.TAG_FILTER_DIALOG);
        switch (getTag()) {
            case TAG_BEGIN_DATE:
                return f.b.cbBeginDate;
            case TAG_END_DATE:
                return f.b.cbEndDate;
            default:
                new Exception().printStackTrace();
                return null;
        }
    }

}
