package com.prolificinteractive.materialcalendarview.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.threeten.bp.Clock;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;


/**
 * Shows off the most basic usage
 */
public class DialogsActivity extends AppCompatActivity {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dialogs);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.button_normal_dialog)
  void onNormalDialogClick() {
    new SimpleDialogFragment().show(getSupportFragmentManager(), "test-normal");
  }

  @OnClick(R.id.button_simple_dialog)
  void onSimpleCalendarDialogClick() {
    new SimpleCalendarDialogFragment().show(getSupportFragmentManager(), "test-simple-calendar");
  }

  public static abstract class TitleFormatters implements TitleFormatter {
    String DEFAULT_FORMAT = "LLLL";

  }

  public static class SimpleDialogFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      return new AlertDialog.Builder(getActivity())
          .setTitle(R.string.title_activity_dialogs)
          .setMessage("Test Dialog")
          .setPositiveButton(android.R.string.ok, null)
          .create();
    }
  }

  public static class SimpleCalendarDialogFragment extends AppCompatDialogFragment
      implements OnDateSelectedListener {

    private TextView textView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      LayoutInflater inflater = getActivity().getLayoutInflater();
      LocalDate l = LocalDate.ofYearDay(2000, 1);
      //inflate custom layout and get views
      //pass null as parent view because will be in dialog layout
      View view = inflater.inflate(R.layout.dialog_basic, null);

      textView = view.findViewById(R.id.textView);

      MaterialCalendarView widget = view.findViewById(R.id.calendarView);
      DateFormatTitleFormatter dateFormatTitleFormatter = new DateFormatTitleFormatter();
      DateTimeFormatter.ofPattern("LLLL");
      widget.setTitleFormatter(dateFormatTitleFormatter);
      widget.state().edit()
              .setMinimumDate(CalendarDay.from(2000, 1, 1))  //设置可以显示的最早时间
              .setMaximumDate(CalendarDay.from(2000, 12, 31))//设置可以显示的最晚时间
              .setCalendarDisplayMode(CalendarMode.MONTHS)//设置显示模式，可以显示月的模式，也可以显示周的模式
              .commit();// 返回对象并保存
      widget.setCurrentDate(l);
      widget.setSelectionColor(getContext().getResources().getColor(R.color.sample_red));
      widget.setOnDateChangedListener(this);

      return new AlertDialog.Builder(getActivity())
          .setTitle(R.string.title_activity_dialogs)
          .setView(view)
          .create();
    }

    @Override
    public void onDateSelected(
        @NonNull MaterialCalendarView widget,
        @NonNull CalendarDay date,
        boolean selected) {
      textView.setText(FORMATTER.format(date.getDate()));
    }
  }
}
