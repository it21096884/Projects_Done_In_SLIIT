package EventManagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parithyaga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.CharityEvent;
import ui.AllCharityRecyclerViewAdapter;


public class All_EventsFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int increment;
    private CollectionReference collectionReference = db.collection("Event");
    private TextView noallEvent;
    private TextView allparticipant_text;
    private List<CharityEvent> allcharityeventlist;
    private RecyclerView allrecyclerView;
    private AllCharityRecyclerViewAdapter allCharityRecyclerViewAdapter;
    private Button join_btn;

    private CharityEvent charityEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all__events, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allcharityeventlist = new ArrayList<>();
        allrecyclerView = view.findViewById(R.id.all_events_recycler);
        allrecyclerView.setHasFixedSize(true);
        allrecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getEveryEvent();


    }

    private void getEveryEvent() {
        super.onStart();
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot allevents: queryDocumentSnapshots){
                                CharityEvent charityEvent = allevents.toObject(CharityEvent.class);
                                charityEvent.setEventId(allevents.getId());
                                allcharityeventlist.add(charityEvent);
                            }

                            //invoking all recyclerview
                            allCharityRecyclerViewAdapter= new AllCharityRecyclerViewAdapter(All_EventsFragment.this,allcharityeventlist);
                            allrecyclerView.setAdapter(allCharityRecyclerViewAdapter);
                            allCharityRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("all", "onFailure: "+e.toString());
                    }
                });
    }
    }
