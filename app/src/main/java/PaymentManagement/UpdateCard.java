package PaymentManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parithyaga.R;
import com.example.parithyaga.UserAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Card;

public class UpdateCard extends AppCompatActivity {
    private EditText cardname;
    private EditText cvnNum;
    private EditText cardNumber;

    private Button updateCard;
    private Button deletebtn;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    // connection to firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("PaymentCards");
    private Card updtcrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_update_card);

        updtcrd = (Card) getIntent().getSerializableExtra("UpdateCardDet");

        cardname = findViewById(R.id.card_name_update);
        cardNumber = findViewById(R.id.card_number_update);
        cvnNum = findViewById(R.id.cvn_number_update);
        updateCard = findViewById(R.id.update_card_btn);
        deletebtn = findViewById(R.id.delete_card);
        cardname.setText(updtcrd.getCardName());
        cardNumber.setText(updtcrd.getCardNumber());
        cvnNum.setText(updtcrd.getCnvNumber());

        updateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateThisCard();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteCard();
            }
        });
    }

    private void DeleteCard() {
        collectionReference.document(updtcrd.getCardId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateCard.this, "This Card was Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(UpdateCard.this,UserAccount.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void UpdateThisCard() {

        String cardName = cardname.getText().toString().trim();
        String cvnNumber = cvnNum.getText().toString().trim();
        String cardNum = cardNumber.getText().toString().trim();

        Card updateTcard = new Card();
        updateTcard.setCardName(cardName);
        updateTcard.setCardNumber(cardNum);
        updateTcard.setCnvNumber(cvnNumber);

        collectionReference.document(updtcrd.getCardId())
                .update(
                        "cardName",updateTcard.getCardName(),
                        "cardNumber",updateTcard.getCardNumber(),
                        "cvnNumber",updateTcard.getCnvNumber()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateCard.this, "Card was Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateCard.this, UserAccount.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


}