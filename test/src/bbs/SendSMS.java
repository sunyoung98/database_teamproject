package bbs;

import java.io.IOException;
import java.net.URLDecoder;

public class SendSMS {

	public SendSMS( String[] phoneN,String msg )

    {

        SMS sms = new SMS();

        

        sms.appversion("TEST/1.0");

        sms.charset("utf8");

        sms.setuser("dlwlgus9805", "dlwlgus3824");				// coolsms 怨꾩젙 �엯�젰�빐二쇱떆硫대릺�슂



        
        for(int i=0;i<phoneN.length;i++) {
        	 SmsMessagePdu pdu = new SmsMessagePdu();

             pdu.type = "SMS";

             pdu.destinationAddress = phoneN[i];

             pdu.scAddress = "01094128011";                   // 諛쒖떊�옄 踰덊샇          

           

             try {
            	  
                  pdu.text =msg;
                  //System.out.println(msg);
                  	// 蹂대궪 硫붿꽭吏� �궡�슜

                  sms.add(pdu);
                 sms.connect();

                 sms.send();

                 sms.disconnect();

             } catch (IOException e) {

                 System.out.println(e.toString());

             }

             sms.printr();

             sms.emptyall();	


        }
       
    }


}
