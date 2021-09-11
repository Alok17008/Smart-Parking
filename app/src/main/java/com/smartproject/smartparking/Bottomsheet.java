package com.smartproject.smartparking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Bottomsheet extends BottomSheetDialogFragment {

    public Bottomsheet() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        View view =inflater.inflate( R.layout.fragment_bottomsheet,container,false );
        setStyle( STYLE_NORMAL,R.style.AppBottomSheetDialogTheme );


        return view;
    }
}
