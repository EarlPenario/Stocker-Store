package org.example;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreConnection {
    Firestore db;

    public FireStoreConnection(){
        db=null;
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/java/org/example/stock-store-a277c-firebase-adminsdk-fbsvc-107493cabb.json");
            FirebaseOptions options = new FirebaseOptions.Builder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount)).
                    setDatabaseUrl("https://stock-store-a277c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addEmployee(String name, String brand, String type, String expiry,
                            String quantity, String price) {
        Map<String, Object> product = new HashMap<>();
        product.put("Name", name);
        product.put("Brand",brand);
        product.put("Type", type);
        product.put("Expiration Date", expiry);
        product.put("Quantity", quantity);
        product.put("Price",price);

        ApiFuture<DocumentReference> result = db.collection("products").add(product);

        try {
            System.out.println("Added Documentation with Id: " + result.get().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateProduct(Product product,int index){
        try {
            ApiFuture<QuerySnapshot>query=db.collection(("products")).get();
            List<QueryDocumentSnapshot>documents=query.get().getDocuments();

            if(index>=0&&index<documents.size()){
                String documentId=documents.get(index).getId();
                DocumentReference docRef=db.collection("products").document(documentId);
                Map<String,Object>updates=new HashMap<>();
                updates.put("Name",product.getName());
                updates.put("Brand",product.getBrand());
                updates.put("Type",product.getType());
                updates.put("Expiration Date",product.getExpiry());
                updates.put("Quantity",product.getQuantity());
                updates.put("Price",product.getPrice());
                docRef.update(updates);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteProduct(int index){
        try {
            ApiFuture<QuerySnapshot>query=db.collection("products").get();
            List<QueryDocumentSnapshot>documents=query.get().getDocuments();
            if(index>=0&&index<documents.size()){
                String documentId=documents.get(index).getId();
                db.collection("products").document(documentId).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Product> getAllProducts() {
        try {
            ApiFuture<QuerySnapshot> query=db.collection("products").get();
            List<QueryDocumentSnapshot> documents=query.get().getDocuments();
            ArrayList<Product>products=new ArrayList<>();
            for(QueryDocumentSnapshot document:documents){
                String name=document.getString("Name");
                String brand=document.getString("Brand");
                String type=document.getString("Type");
                String expiry=document.getString("Expiration Date");
                String quantity=document.getString("Quantity");
                String price=document.getString("Price");
                products.add(new Product(name,brand,type,expiry,quantity,price));
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
