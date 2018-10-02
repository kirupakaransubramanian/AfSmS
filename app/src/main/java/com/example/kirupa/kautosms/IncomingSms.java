package com.example.kirupa.kautosms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.kirupa.kautosms.App.getAppContext;


public class IncomingSms extends BroadcastReceiver {

        // Get the object of SmsManager
        final SmsManager sms = SmsManager.getDefault();
    public static ArrayList<String> phoneNumbers=new ArrayList<>();

        public void onReceive(Context context, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    String senderNum = null;
                    String messageRead = null;
                    String messageCurrent = null;
                    Log.i("SmsReceiver", "pdusObj.length: "+ pdusObj.length);

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        senderNum = phoneNumber;
                        messageCurrent = currentMessage.getDisplayMessageBody();
                        if(messageRead!=null){
                            messageRead = messageRead+messageCurrent;
                        }else{
                            messageRead = messageCurrent;
                        }

                        Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + messageCurrent);

                    } // end for loop
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; messageRead: " + messageRead);
                    sendSMS(messageRead);
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }
    public void sendSMS(String msg) {
        // the phone numbers we want to send to
//        String numbers[] = {"9677158165", "8870890160","8606643250"};
        Log.i("SmsReceiver", "size()::>>"+ phoneNumbers.size()+getAppContext());
        String[] getPhoneNumbers = null;

        getPhoneNumbers = new SharedPreference(getAppContext()).getPhoneNumbers();
        if(getPhoneNumbers!=null){
            System.out.println("getPhoneNumbers length:"+getPhoneNumbers.length);
            phoneNumbers.clear();

            for (int i = 0; i < getPhoneNumbers.length; i++) {
                phoneNumbers.add(getPhoneNumbers[i]);

            }

        }else {

            System.out.println("getPhoneNumbers null:");

        }
        for(String number : phoneNumbers) {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(number, null, parts, null, null);
        }

    }
    }


