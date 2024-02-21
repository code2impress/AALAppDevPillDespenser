package com.example.aal_appdev_pilldespenser;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TutorialStepOneDialogFragment extends DialogFragment {

    // Interface for communicating completion of tutorial step one
    public interface TutorialStepOneListener {
        void onTutorialStepOneCompleted();
    }

    private TutorialStepOneListener listener; // Listener reference

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                // Get display metrics
                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);

                // Determine the size of the dialog to be a square based on the smaller screen dimension
                int size = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
                window.setLayout(size, size); // Apply the square layout
                window.setGravity(Gravity.CENTER); // Center the dialog on screen
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Initialize listener
            listener = (TutorialStepOneListener) context;
        } catch (ClassCastException e) {
            // Throw exception if the context does not implement the listener interface
            throw new ClassCastException(context.toString() + " must implement TutorialStepOneListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create dialog using AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.tutorial_step_one_layout, null); // Use your layout

        // Set the view and positive button for the dialog
        builder.setView(view)
                .setPositiveButton("Next", (dialog, id) -> listener.onTutorialStepOneCompleted());

        return builder.create();
    }
}
