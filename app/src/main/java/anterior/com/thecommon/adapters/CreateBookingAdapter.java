package anterior.com.thecommon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import anterior.com.thecommon.CommonApplication;
import anterior.com.thecommon.CreateBookingActivity;
import anterior.com.thecommon.R;
import anterior.com.thecommon.utils.APIManager;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by admin on 27/09/2017.
 */

public class CreateBookingAdapter extends BaseAdapter{

    public DialogPlus dialog;
    int row_count;
    Date selectedDate;
    CreateBookingActivity addActivity;
    private static LayoutInflater inflater = null;
    public int nCurrentContact;

    public String strTitle;
    public String strDescription;

    public Date startDate;
    public Date endDate;

    EditText etTitle;
    EditText etDescription;

    public CreateBookingAdapter(CreateBookingActivity addEventActivity)
    {
        this.addActivity = addEventActivity;
        inflater = (LayoutInflater) addActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row_count = 2;
        nCurrentContact = 0;
        strTitle = "";
        strDescription = "";
    }


    @Override
    public int getCount() {
        if(addActivity.isEdit)
            return row_count+1;
        else
            return row_count;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View vi=view;
        if(vi == null) {
            if (i == 0) {
                vi = inflater.inflate(R.layout.invite_row0, null);

                Button btnDateTimeStart = vi.findViewById(R.id.btnDateStart);
                final TextView tvDateStart = vi.findViewById(R.id.tvDateStart);
                final TextView tvCredit = vi.findViewById(R.id.tvCredit);
                TextView tvCategory = vi.findViewById(R.id.tvCategory);
                if (!addActivity.isEdit) {
                    String[] arrayRoomType = addActivity.getResources().getStringArray(R.array.branches_type);
                    tvCategory.setText(arrayRoomType[addActivity.nBranchID]);
                } else {
                    tvCategory.setVisibility(View.INVISIBLE);
                }
                btnDateTimeStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new SlideDateTimePicker.Builder(addActivity.getSupportFragmentManager())
                                .setListener(new SlideDateTimeListener() {
                                    public String txtDate;

                                    @Override
                                    public void onDateTimeSet(Date date) {
                                        startDate = date;
                                        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy");
                                        txtDate = fmt.format(startDate);
                                        fmt = new SimpleDateFormat("HH:mm");
                                        tvDateStart.setText(String.format("%s\n%s", txtDate, fmt.format(startDate)));
                                        try {
                                            long diff = endDate.getTime() - startDate.getTime();
                                            long seconds = diff / 1000;
                                            long minutes = seconds / 60;
                                            int credit = (int) (minutes / 15);
                                            tvCredit.setText(String.format("%d credit required", credit));
                                        } catch (Exception e) {

                                        }
                                    }
                                })
                                .setInitialDate(new Date())
                                .build()
                                .show();
                    }
                });

                Button btnDateTimeEnd = (Button) vi.findViewById(R.id.btnDateEnd);
                final TextView tvDateEnd = (TextView) vi.findViewById(R.id.tvDateEnd);
                btnDateTimeEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new SlideDateTimePicker.Builder(addActivity.getSupportFragmentManager())
                                .setListener(new SlideDateTimeListener() {
                                    public String txtDate;

                                    @Override
                                    public void onDateTimeSet(Date date) {
                                        endDate = date;
                                        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy");
                                        txtDate = fmt.format(endDate);
                                        fmt = new SimpleDateFormat("HH:mm");
                                        tvDateEnd.setText(String.format("%s\n%s", txtDate, fmt.format(endDate)));

                                        try {
                                            long diff = endDate.getTime() - startDate.getTime();
                                            long seconds = diff / 1000;
                                            long minutes = seconds / 60;
                                            int credit = (int) (minutes / 15);
                                            tvCredit.setText(String.format("%d credit required", credit));
                                        } catch (Exception e) {

                                        }
                                    }
                                })
                                .setInitialDate(new Date())
                                .build()
                                .show();
                    }
                });


                if (addActivity.isEdit) {
                    startDate = addActivity.booking.starttime;

                    SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy");
                    String txtDate = fmt.format(startDate);
                    fmt = new SimpleDateFormat("HH:mm");
                    tvDateStart.setText(String.format("%s\n%s", txtDate, fmt.format(startDate)));

                    endDate = addActivity.booking.endtime;
                    ;
                    txtDate = fmt.format(endDate);
                    tvDateEnd.setText(String.format("%s\n%s", txtDate, fmt.format(endDate)));

                    EditText etTitle = vi.findViewById(R.id.etTitle);
                    etTitle.setText(addActivity.booking.title);

                    EditText etDescription = vi.findViewById(R.id.etDescription);
                    etDescription.setText(addActivity.booking.description);


                } else {
                    SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy");
                    if (startDate == null)
                        tvDateStart.setText("");
                    else {
                        String txtDate = fmt.format(startDate);
                        tvDateStart.setText(String.format("%s\n%s", txtDate, fmt.format(startDate)));
                    }
                    if (endDate == null)
                        tvDateEnd.setText("");
                    else {
                        String txtDate = fmt.format(endDate);
                        tvDateEnd.setText(String.format("%s\n%s", txtDate, fmt.format(endDate)));
                    }

                    etTitle = vi.findViewById(R.id.etTitle);
                    etTitle.setText(strTitle);

                    etDescription = vi.findViewById(R.id.etDescription);
                    etDescription.setText(strDescription);
                }

            } else if (i == 1) {
                vi = inflater.inflate(R.layout.invite_row, null);

                Button btn_add_email = vi.findViewById(R.id.btn_add_email);

                final View finalVi = vi;
                btn_add_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strTitle = etTitle.getText().toString();
                        strDescription = etDescription.getText().toString();
                        row_count = row_count + 1;
                        notifyDataSetInvalidated();


                    }
                });

            } else if (addActivity.isEdit && i == row_count) {
                vi = inflater.inflate(R.layout.delete_row1, null);


                Button btnDelete = vi.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(addActivity, SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setTitleText("Do you want delete booking?");
                        sweetAlertDialog.setConfirmText("Sure");
                        sweetAlertDialog.setCancelText("Cancel");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                APIManager.getInstance(addActivity).deletebooking(addActivity.booking.id, new APIManager.CommonCallBackInterface() {
                                    @Override
                                    public void onSuccess(Object result) {
                                        addActivity.finish();
                                    }

                                    @Override
                                    public void onFailure(String error, int nCode) {
                                        int i = 0;
                                    }
                                });

                            }
                        });
                        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                sweetAlertDialog.cancel();

                            }
                        });
                        sweetAlertDialog.show();

                    }
                });

            } else {

                vi = inflater.inflate(R.layout.delete_row, null);
                String strEmail = "";
                if (addActivity.arrayInviteEmails.size() >= i - 1) {
                    strEmail = addActivity.arrayInviteEmails.get(i - 2);
                }

                EditText etEmail = vi.findViewById(R.id.etEmail);
                etEmail.setText(strEmail);
                ImageButton btn_Delete = (ImageButton) vi.findViewById(R.id.btn_Delete);
                btn_Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        row_count = row_count - 1;
                        notifyDataSetInvalidated();
                        try {
                            addActivity.arrayInviteEmails.remove(i - 2);
                        } catch (Exception e) {

                        }
                    }
                });

                ImageButton btn_Contacts = (ImageButton) vi.findViewById(R.id.btn_Contacts);
                btn_Contacts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        nCurrentContact = i - 2;
                        DialogAdapter adapter = new DialogAdapter(addActivity);

                        dialog = DialogPlus.newDialog(addActivity)
                                .setAdapter(adapter)
                                .setExpanded(false, 750)
                                .setMargin(10, 10, 10, 10)
                                .setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                    }
                                })
//                                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                                .create();
                        dialog.show();

                    }
                });
            }


        }
        return vi;
    }
}
