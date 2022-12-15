package EventManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import model.CharityEvent;
import util.UserApi;

public class UpdateEvent extends AppCompatActivity implements View.OnClickListener {

    private EditText eventNameupdate;
    private EditText eventDescupdate;
    private EditText eventlocupdate;
    private EditText eventcountupdate;
    private EditText eventcontactupdate;
    private ProgressBar update_eventprogress;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Event");
    private CharityEvent charityEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        charityEvent = (CharityEvent) getIntent().getSerializableExtra("chairtyevent");

        eventNameupdate = findViewById(R.id.update_event_name);
        eventDescupdate = findViewById(R.id.update_event_desc);
        eventlocupdate = findViewById(R.id.update_event_location);
        eventcountupdate = findViewById(R.id.update_event_prcount);
        eventcountupdate.setFocusable(false);
        eventcontactupdate = findViewById(R.id.update_event_phn);
      // update_eventprogress = findViewById(R.id.update_progressbar);

        eventNameupdate.setText(charityEvent.getEventName());
        eventDescupdate.setText(charityEvent.getEventDesc());
        eventlocupdate.setText(charityEvent.getLocation());
        eventcountupdate.setText(String.valueOf(charityEvent.getParCount()));
        eventcontactupdate.setText(String.valueOf(charityEvent.getPhoneNumber()));

        if(UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserID();
            currentUserName = UserApi.getInstance().getUserName();

        }
        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if(user != null){
                Log.d("userins", "onCreate: "+user.getEmail());
            }else{

            }
        };

        //update_eventprogress.setVisibility(View.INVISIBLE);
        findViewById(R.id.update_event_btn).setOnClickListener(UpdateEvent.this);
        findViewById(R.id.delete_eventbtn).setOnClickListener(UpdateEvent.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_event_btn:
                updateEvent();
                break;
            case R.id.delete_eventbtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to delete this event?");
                builder.setMessage("This event will be deleted from your events");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteEvent();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog ad = builder.create();
                ad.show();

                break;
        }
    }

    private void updateEvent() {
        String name = eventNameupdate.getText().toString().trim();
        String desc = eventDescupdate.getText().toString().trim();
        String location = eventlocupdate.getText().toString().trim();
        String count_update = eventcountupdate.getText().toString().trim();
        int eprcount_update = Integer.parseInt(count_update);
        String contacts = eventcontactupdate.getText().toString().trim();
        long ephone_update = Long.parseLong(contacts);

//        update_eventprogress.setVisibility(View.VISIBLE);
        //update the details
        CharityEvent charityEvent1 = new CharityEvent();
        charityEvent1.setEventName(name);
        charityEvent1.setEventDesc(desc);
        charityEvent1.setTimeadded(new Timestamp(new Date()));
        charityEvent1.setLocation(location);
        charityEvent1.setParCount(eprcount_update);
        charityEvent1.setPhoneNumber(ephone_update);
        charityEvent1.setUserName(currentUserName);
        charityEvent1.setUserId(currentUserId);
        Log.d("userID", "updateEvent: "+currentUserId);

        collectionReference.document(charityEvent.getEventId())
                .update(
                        "eventName",charityEvent1.getEventName(),
                        "eventDesc",charityEvent1.getEventDesc(),
                        "location",charityEvent1.getLocation(),
                        "parCount",charityEvent1.getParCount(),
                        "timeadded", charityEvent1.getTimeadded(),
                        "phoneNumber",charityEvent1.getPhoneNumber(),
                        "userId",charityEvent1.getUserId(),
                        "userName",charityEvent1.getUserName()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //update_eventprogress.setVisibility(View.INVISIBLE);
                        Toast.makeText(UpdateEvent.this, "Event Updated", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UpdateEvent.this, UserAccount.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // update_eventprogress.setVisibility(View.INVISIBLE);
                        Log.d("eventup", "onFailure: "+e.toString());
                    }
                });
    }
    private void deleteEvent() {
        collectionReference.document(charityEvent.getEventId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UpdateEvent.this, "Event Deleted", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(UpdateEvent.this, UserAccount.class));

                        }
                    }
                });
    }
}