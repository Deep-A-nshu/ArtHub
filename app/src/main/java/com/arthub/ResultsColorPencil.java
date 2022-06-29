package com.firstapp.arthub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firstapp.arthub.models.Prizewinners;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ResultsColorPencil extends BottomSheetDialogFragment {

    TextView Compid;
    TextView firstp,secondp,thirdp;
    DatabaseReference databaseReference;
    FirebaseFirestore database;
    LinearLayout linearresultFR;
    Button showwinnersbtn;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ResultsColorPencil() {
        // Required empty public constructor
    }

    public static ResultsColorPencil newInstance(String param1, String param2) {
        ResultsColorPencil fragment = new ResultsColorPencil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results_color_pencil, container, false);
        Compid = view.findViewById(R.id.CompID_CPTF);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Results").child("colorPencil").child(uid);
        firstp = view.findViewById(R.id.FirstWinnerCPTF);
        secondp = view.findViewById(R.id.SecondWinnerCPTF);
        linearresultFR = view.findViewById(R.id.linearresultCPTF);
        showwinnersbtn = view.findViewById(R.id.showwinnersBTNCP);
        thirdp = view.findViewById(R.id.ThirdWinnerCPTF);

        showwinnersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearresultFR.setVisibility(View.VISIBLE);
                database = FirebaseFirestore.getInstance();
                String CID = Compid.getText().toString();
                database.collection("Results")
                        .document(CID)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {

                        Prizewinners useer = documentSnapshot.toObject(Prizewinners.class);
                        firstp.setText(String.valueOf(useer.getFirstprize()));
                        secondp.setText(String.valueOf(useer.getSecondprize()));
                        thirdp.setText(String.valueOf(useer.getThirdprize()));
                    }
                });
            }
        });


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        Bundle mArgs = getArguments();
        String myValue = mArgs.getString("KEY");
        databaseReference.child(myValue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String cid = snapshot.child("comp_id").getValue(String.class);
                    Compid.setText(cid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}