package com.gits.smzone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.gits.smzone.db.Item;


public class AddZoneActivity extends Activity implements OnClickListener{
	
	
	
	
	private EditText title;
	private EditText message;
	private EditText latitude;
	private EditText longitude;
	private EditText address;
	private EditText phoneNumber;
	private EditText radius;

	private final int RC_PICK_MAP=1;
	private final int RC_PICK_CONTACT=2;
	static final int PICK_CONTACT = 0;

	private long id;

	/** Called when the activity is first created. */
	
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		title= (EditText) findViewById(R.id.title);
		message= (EditText) findViewById(R.id.message);
		latitude = (EditText) findViewById(R.id.latitude);
		longitude= (EditText) findViewById(R.id.longitude);
		address= (EditText) findViewById(R.id.address);
		phoneNumber= (EditText) findViewById(R.id.phone_number);
		radius= (EditText) findViewById(R.id.radius);

		findViewById(R.id.btn_pick_map).setOnClickListener(this);
		findViewById(R.id.btn_pick_contact).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);

		Bundle extras = getIntent().getExtras(); 
		if(extras!=null){
			if(extras.getLong("id")!=0L){
				id =  extras.getLong("id");
				findViewById(R.id.ic_title_ok).setVisibility(View.VISIBLE);
				fillUI();
			}
		}
	}

	private void fillUI(){
		Cursor cur = getContentResolver().query(Item.CONTENT_URI, null, Item._ID +"="+ id, null, null);
		if(cur.moveToFirst()){
			title.setText(cur.getString(cur.getColumnIndex(Item.TITLE)));
			message.setText(cur.getString(cur.getColumnIndex(Item.MESSAGE)));
			latitude.setText(cur.getString(cur.getColumnIndex(Item.LATITUDE)));
			longitude.setText(cur.getString(cur.getColumnIndex(Item.LONGITUDE)));
			address.setText(cur.getString(cur.getColumnIndex(Item.ADDRESS)));
			phoneNumber.setText(cur.getString(cur.getColumnIndex(Item.PHONE_NUMBER)));
			radius.setText(cur.getString(cur.getColumnIndex(Item.RADIUS)));
		}
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_pick_map:
			intent = new Intent(this, PickMapActivity.class);
			intent.putExtra("a", "a");
			if(!latitude.getText().toString().equals("")){
				intent.putExtra("latitude", Double.parseDouble(latitude.getText().toString()));
				intent.putExtra("longitude", Double.parseDouble(longitude.getText().toString()));
			}
			startActivityForResult(intent, RC_PICK_MAP);
			break;
		case R.id.btn_pick_contact:
			Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);  
			startActivityForResult(contactPickerIntent,RC_PICK_CONTACT ); 
			break;
		case R.id.btn_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btn_save:
			saveZone();
			break;
		default:
			break;
		}

	}

	private void saveZone(){
		//validasi
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true)
		.setTitle(getString(R.string.validation_title))
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		if(title.getText().toString().trim().equals("")){
			builder.setMessage(R.string.validation_error_title);
			AlertDialog alert = builder.create();
			alert.show();
		}else if(message.getText().toString().equals("")){
			builder.setMessage(R.string.validation_error_message);
			AlertDialog alert = builder.create();
			alert.show();
		}else if(phoneNumber.getText().toString().equals("")){
			builder.setMessage(R.string.validation_error_phone);
			AlertDialog alert = builder.create();
			alert.show();
		}else if(latitude.getText().toString().equals("")){
			builder.setMessage(R.string.validation_error_destination);
			AlertDialog alert = builder.create();
			alert.show();
		}else if(radius.getText().toString().equals("")){
			builder.setMessage(R.string.validation_error_radius);
			AlertDialog alert = builder.create();
			
			alert.show();
		}else{
			
			//save ke sqlite
			ContentValues cv = new ContentValues();
			cv.put(Item.TITLE, title.getText().toString().trim());
			cv.put(Item.MESSAGE, message.getText().toString().trim());
			cv.put(Item.PHONE_NUMBER, phoneNumber.getText().toString().trim());
			cv.put(Item.LATITUDE, latitude.getText().toString().trim());
			cv.put(Item.LONGITUDE, longitude.getText().toString().trim());
			cv.put(Item.ADDRESS, address.getText().toString().trim());
			cv.put(Item.RADIUS, radius.getText().toString().trim());
			cv.put(Item.DISTANCE, "");
			cv.put(Item.IS_ACTIVE, true);
			try {
				if(id!=0L)
					getContentResolver().update(Item.CONTENT_URI, cv, Item._ID +"="+id, null);
				else
					getContentResolver().insert(Item.CONTENT_URI, cv);
				setResult(RESULT_OK);
				finish();
			} catch (Exception e) {
				//error save
				Toast.makeText(this, getString(R.string.error_save), Toast.LENGTH_SHORT).show();
			}

		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RC_PICK_MAP:
			if(resultCode==RESULT_OK){
				Bundle extras = data.getExtras();
				latitude.setText(extras.getString("latitude"));
				longitude.setText(extras.getString("longitude"));
				address.setText(extras.getString("address"));
			}
			break;
		case RC_PICK_CONTACT:
			if(resultCode==RESULT_OK){
            	
                
				
			String [] proj= {Phone.NUMBER};
			Cursor cursor = managedQuery(data.getData(),proj,null,null,null); 
			if (cursor.moveToFirst()){
				String phn = cursor.getString(cursor.getColumnIndexOrThrow(Phone.NUMBER));
				
				phoneNumber.setText(phn);
			
				
			}
		}

			break;

		default:
			break;
		}
	}
	
	public void onDeleteClick(View v){
		if(getContentResolver().delete(Item.CONTENT_URI, Item._ID +"="+id, null)>0){
			setResult(RESULT_OK);
			finish();
		}
	}
}