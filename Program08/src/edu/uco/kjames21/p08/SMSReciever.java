package edu.uco.kjames21.p08;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsMessage;

public class SMSReciever extends BroadcastReceiver {

	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null && intent.getAction() != null && ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
			Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] messages = new SmsMessage[pduArray.length];
			for (int i = 0; i < pduArray.length; i++) {
				messages[i] = SmsMessage.createFromPdu((byte[])pduArray[i]);
			}
			String message = messages[0].getMessageBody();
			Intent newIntent = null;
			if (message.toLowerCase().startsWith("call")) {
				String telNumber = message.split(" ")[1];
				
				if(telNumber != null && !telNumber.equals("")) {
					newIntent = new Intent(Intent.ACTION_DIAL);
					newIntent.setData(Uri.parse("tel:" + telNumber));
				}
			} 
			else if (message.equals("uco")) {
				newIntent = new Intent(Intent.ACTION_VIEW);
				newIntent.setData(Uri.parse("http://www.uco.edu"));
			} 
			else if(message.equals("contact")) {
				newIntent = new Intent(context, ContactsActivity.class);
			}
			else if(message.equals("logo")){
				newIntent = new Intent(context, Logo.class);
			}
			
			if(newIntent != null) {
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(newIntent);
			}
		}
	}
}
