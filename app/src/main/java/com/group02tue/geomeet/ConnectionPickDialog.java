package com.group02tue.geomeet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.group02tue.geomeet.backend.api.APIFailureReason;
import com.group02tue.geomeet.backend.api.profiles.QueryUsersAPIResponseListener;
import com.group02tue.geomeet.backend.authentication.AuthenticationManager;
import com.group02tue.geomeet.backend.social.ConnectionsManager;

import java.util.ArrayList;
import java.util.HashSet;

public class ConnectionPickDialog {
    private Context context;
    private ConnectionPickDialogEventListener eventListener;
    private final ArrayList<String> filteredUsers = new ArrayList<>();
    private AuthenticationManager authenticationManager;

    public ConnectionPickDialog(Context context, ConnectionPickDialogEventListener eventListener,
                                AuthenticationManager authenticationManager) {
        this.eventListener = eventListener;
        this.context = context;
        this.authenticationManager = authenticationManager;
    }


    public void show() {
        final Dialog dialog_data = new Dialog(context);
        dialog_data.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog_data.getWindow().setGravity(Gravity.CENTER);
        dialog_data.setContentView(R.layout.search_connection_alertdialog);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        TextView alertdialog_textview = (TextView) dialog_data.findViewById(R.id.alertdialog_textview);
        alertdialog_textview.setText("selected_string");
        alertdialog_textview.setHint("selected_string");

        Button btn_cancel = dialog_data.findViewById(R.id.dialog_cancel_btn);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog_data != null) {
                    dialog_data.dismiss();
                    dialog_data.cancel();
                }
            }
        });
        EditText filterText = dialog_data.findViewById(R.id.alertdialog_edittext);
        ListView alertdialog_listview = dialog_data.findViewById(R.id.alertdialog_Listview);
        alertdialog_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, filteredUsers);
        alertdialog_listview.setAdapter(adapter);
        alertdialog_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
                //Utility.hideKeyboard(this, v);
                if (dialog_data != null) {
                    synchronized (filteredUsers) {
                        if (position < filteredUsers.size()) {
                            String userSelected = filteredUsers.get(position);
                            eventListener.onPickedConnection(userSelected);
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    dialog_data.dismiss();
                    dialog_data.cancel();
                }
            }
        });
        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (String.valueOf(s).isEmpty()) {
                    filteredUsers.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }
                //adapter.getFilter().filter(s);
                ConnectionsManager.QueryUsernames(authenticationManager, new QueryUsersAPIResponseListener() {
                    @Override
                    public void onFoundUsernames(String requestedUsername, HashSet<String> usernames) {
                        if (String.valueOf(s).equals(requestedUsername)) {
                            synchronized (filteredUsers) {
                                // Remove old ones
                                for (int i = filteredUsers.size() - 1; i > -1; i--) {
                                    if (!usernames.contains(filteredUsers.get(i))) {
                                        filteredUsers.remove(i);
                                    }
                                }
                                // Add new ones
                                for (String username : usernames) {
                                    if (!filteredUsers.contains(username)) {
                                        filteredUsers.add(username);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(APIFailureReason response) {

                    }
                }, String.valueOf(s));
            }
        });
        dialog_data.show();
    }
}
