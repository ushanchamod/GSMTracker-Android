package com.ousl.gsm.ui.immense;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.ousl.gsm.DatabaseHelper;
import com.ousl.gsm.R;
import com.ousl.gsm.databinding.FragmentImmenseBinding;


public class ImmenseFragment extends Fragment {
    Button btnOpenMapActivity;
    TextInputLayout title;
    EditText secondsForTrack;
    String text;
    String userId;
    private FragmentImmenseBinding binding;

    DatabaseHelper myDb;

    CustomBaseAdapterImmense customBaseAdapter;
    ListView listView;
    String[][] results;

    View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentImmenseBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        btnOpenMapActivity = root.findViewById(R.id.start_tracking_btn);

        title = root.findViewById(R.id.textInputLayout2);

        secondsForTrack = root.findViewById(R.id.second_for_track);

        launchMapActivity();

        // Database
        myDb = new DatabaseHelper(getActivity());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userId = sharedPreferences.getString("user_id", "default value");

        results = myDb.retrieveDataFromImmenseMain(Integer.parseInt(userId));

        listView = (ListView) root.findViewById(R.id.immense_list_view);
        customBaseAdapter = new CustomBaseAdapterImmense(getActivity(), results);
        listView.setAdapter(customBaseAdapter);

        return root;
    }

    public void launchMapActivity(){
        btnOpenMapActivity.setOnClickListener(view -> {
            // check input feldspar are empty
            text = title.getEditText().getText().toString();
            if(text.isEmpty()){
                Toast toast = Toast.makeText(getContext(), "Please enter a title for continue", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if (secondsForTrack.getText().toString().isEmpty()){
                Toast toast = Toast.makeText(getContext(), "Please enter seconds for continue", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("tracking_title", text);
            editor.putString("second_for_tracking", secondsForTrack.getText().toString());
            editor.apply();

            // open map activity
            Toast toast = Toast.makeText(getContext(), "Open Map Activity", Toast.LENGTH_SHORT);
            toast.show();

            // map activity
            Intent intent = new Intent(getActivity(), MapsActivityImmense.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}