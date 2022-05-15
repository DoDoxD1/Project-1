package com.example.sendsync;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    private String name, email, userPass, profileUriString;
    static FirebaseAuth mAuth;
    static FirebaseUser mUser;
    static User user;
    static FirebaseFirestore db;
    static DocumentReference userRef;


    public User(){

    }

    public User(String email, String userPass){
        this.email = email;
        this.userPass = userPass;
    }

    public void setPhotoUri(String photoUri) {
        this.profileUriString = photoUri;
    }


    public static void getCurrentUserFromDB(){

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if(mUser!=null)
        userRef = db.collection("Users").document(mUser.getUid());
        if(userRef!=null)
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
            }
        });

    }

    public static User getUser() {
        return user;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPass(){return userPass;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.userPass = password;
    }

    public String getProfileUriString() {
        return profileUriString;
    }

    public void setProfileUriString(String profileUriString) {
        this.profileUriString = profileUriString;
    }

}
