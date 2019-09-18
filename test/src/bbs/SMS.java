package bbs;
/**
 * vi:set ts=4 sw=4 expandtab fileencoding=utf8:
 * @version 2.3
 * @author wiley
 * @copyright (C) 2008-2010 D&SOFT
 * @link http://open.coolsms.co.kr
 **/
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * setuser   : set user id and password
 * add      : add message
 * send      : send messages (IOException catch required)
 * getr      : get result
 * printr   : print result
 */
public class SMS
{
   static final String VERSION_STR = "TBSP/1.0";

   private String _charset;
    private String app_version;
   private String userid;
   private String password;
   private String crypt;
   private ArrayList<SmsMessagePdu> pduList;
   private ArrayList<HashMap<String,String>> result;
   private Socket sock;
   private BufferedOutputStream fos;
   private BufferedInputStream bis;

   public SMS() 
   {
      crypt = "MD5";
      pduList = new ArrayList<SmsMessagePdu>();
      result = new ArrayList<HashMap<String,String>>();

   }


   public String keygen()
   {
      SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      Date d = new Date();

      Random rd = new Random();
      long rval = rd.nextInt(999999999);

      DecimalFormat decfor = new DecimalFormat("000000000");

      return df.format(d) + decfor.format(rval);
   }

   public void connect() throws IOException
   {
      ServerLoadBalance slb = new ServerLoadBalance();

      sock = slb.connect();

      fos = new BufferedOutputStream(sock.getOutputStream());
      bis = new BufferedInputStream(sock.getInputStream());
   }

   public void disconnect() throws IOException
   {
      sock.close();
   }

   public void setuser(String userid, String password) 
   {
        setuser(userid, password, "MD5");
   }

   public void setuser(String userid, String password, String crypt) 
   {
      this.userid = userid;
        if (crypt.equals("MD5"))
         this.password = md5(password);
        else
          this.password = password;
   }

    public void setcrypt(String crypt)
    {
        this.crypt = crypt;
    }

   public void charset(String _charset)
   {
      this._charset = _charset;
   }

    public void appversion(String version)
    {
        this.app_version = version;
    }

    public void emptyall()
    {
        this.pduList.clear();
        this.result.clear();
    }

   /**
    * add PDU
    *
    * @param SmsMessagePdu
    */
   public void add(SmsMessagePdu pdu) 
   {
        pduList.add(pdu);
   }

   /**
    * add immediate message
    *
    * @param callno recipient number
    * @param callback sender number
    * @param message 80 bytes message content
    */
   public void add(String callno, String callback, String message) 
   {
      add(callno, callback, message, null, null, null, "SMS", null, null, 0);
   }

   /**
    * add immediate message with ref. name
    *
    * @param callno recipient number
    * @param callback sender number
    * @param message 80 bytes message content
    * @param callname reference name
    */

   public void add(String callno, String callback, String message, String callname) 
   {
      add(callno, callback, message, callname, null, null, "SMS", null, null, 0);
   }

   /**
    * add reservation message
    *
    * @param callno recipient number
    * @param callback sender number
    * @param message 80 bytes message content
    * @param callname reference name
    * @param date reservation date YYYYMMDDHHMISS
    */
   public void add(String callno, String callback, String message, String callname, String date) 
   {
      add(callno, callback, message, callname, date, null, "SMS", null, null, 0);
   }

   /**
    * add message with local key
    *
    * @param callno recipient number
    * @param callback sender number
    * @param message 80 bytes message content
    * @param callname reference name
    * @param date reservation date YYYYMMDDHHMISS
    */
   public void add(String callno, String callback, String message, String callname, String date, String localkey) 
   {
        add(callno, callback, message, callname, date, localkey, "SMS", null, null, 0); 
   }

   /**
    * add message with local key
    *
    * @param callno recipient number
    * @param callback sender number
    * @param message 80 bytes message content
    * @param callname reference name
    * @param date reservation date YYYYMMDDHHMISS
    */
   public void add(String callno, String callback, String message, String callname, String date, String localkey, String type, String subject, String groupid, int delay_count) 
   {
        SmsMessagePdu pdu = new SmsMessagePdu();
        pdu.type = type;
        pdu.destinationAddress = callno;
        pdu.scAddress = callback;
        pdu.subject = subject;
        pdu.text = message;
        pdu.refName = callname;
        pdu.messageId = localkey;
        pdu.groupId = groupid;
        pdu.delayCount = delay_count;
      pduList.add(pdu);
   }

   /**
    * send all of messages
    */
   public void send() throws IOException
   {
      if (this.userid == null || this.password == null)
      {
         System.out.println("setuser required");
         return;
      }

      for (int i = 0; i < pduList.size(); i++)
      {
         byte[] packet = assembleFields(pduList.get(i));
         fos.write(packet, 0, packet.length);
         fos.flush();

            CSCP cscp = new CSCP();
         cscp.read(bis);
            CSCPPARAM ack = cscp.getparam(CSCPPARAM.ID_MESSAGE);
            if (ack == null) {
                System.out.println("no ack returned");
                break;
            }
            String tbspstr = ack.getBody();

         HashMap<String,String> map = TBSP.parse(tbspstr);

         result.add(map);
      }
   }

   public HashMap<String,String> remain() throws IOException
   {
      if (this.userid == null || this.password == null)
      {
         System.out.println("setuser required");
         return null;
      }

      HashMap<String, String> map = new HashMap<String, String>();

      map.put("VERSION", VERSION_STR);
      map.put("COMMAND", "CHECK-REMAIN");
      if (this.crypt.equals("MD5")) map.put("CRYPT-METHOD", "MD5");
      map.put("AUTH-ID", this.userid);
      map.put("AUTH-PASS", this.password);
      map.put("LANGUAGE", "JAVA");

        CSCP cscp = new CSCP();
        cscp.addparam(CSCPPARAM.ID_MESSAGE, TBSP.build(map));
        cscp.build();
      String packet = cscp.getPacketString();
      fos.write(packet.getBytes(), 0, packet.getBytes().length);
      fos.flush();

        CSCP cscp2 = new CSCP();
        cscp2.read(bis);
      CSCPPARAM ack = cscp2.getparam(CSCPPARAM.ID_MESSAGE);
      return TBSP.parse(ack.getBody());
   }


   public HashMap credits() throws IOException
   {
      if (this.userid == null || this.password == null)
      {
         System.out.println("setuser required");
         return null;
      }

      HashMap<String, String> map = new HashMap<String, String>();

      map.put("VERSION", VERSION_STR);
      map.put("COMMAND", "CHECK-CREDITS");
      if (this.crypt.equals("MD5")) map.put("CRYPT-METHOD", "MD5");
      map.put("AUTH-ID", this.userid);
      map.put("AUTH-PASS", this.password);
      map.put("LANGUAGE", "JAVA");

        CSCP cscp = new CSCP();
        cscp.addparam(CSCPPARAM.ID_MESSAGE, TBSP.build(map));
        cscp.build();
      String packet = cscp.getPacketString();
      fos.write(packet.getBytes(), 0, packet.getBytes().length);
      fos.flush();

        CSCP cscp2 = new CSCP();
        cscp2.read(bis);
      CSCPPARAM ack = cscp2.getparam(CSCPPARAM.ID_MESSAGE);
      return TBSP.parse(ack.getBody());
   }

   public HashMap<String,String> rcheck(String msgid) throws IOException
   {
      if (this.userid == null || this.password == null)
      {
         System.out.println("setuser required");
         return null;
      }

      HashMap<String, String> map = new HashMap<String, String>();

      map.put("VERSION", VERSION_STR);
      map.put("COMMAND", "CHECK-RESULT");
      if (this.crypt.equals("MD5")) map.put("CRYPT-METHOD", "MD5");
      map.put("AUTH-ID", this.userid);
      map.put("AUTH-PASS", this.password);
      map.put("LANGUAGE", "JAVA");
      map.put("MESSAGE-ID", msgid);

        CSCP cscp = new CSCP();
        cscp.addparam(CSCPPARAM.ID_MESSAGE, TBSP.build(map));
        cscp.build();
      String packet = cscp.getPacketString();
      fos.write(packet.getBytes(), 0, packet.getBytes().length);
      fos.flush();

        CSCP cscp2 = new CSCP();
        cscp2.read(bis);
      CSCPPARAM ack = cscp2.getparam(CSCPPARAM.ID_MESSAGE);
      return TBSP.parse(ack.getBody());
   }

   public void printr()
   {
      int succ=0;
      int fail=0;

      for (int i = 0; i < result.size(); i++)
      {
         HashMap<String, String> map = result.get(i);

          Set set = map.keySet();
            Object[] keys = set.toArray();
            for (int j = 0; j < keys.length; j++)
            {
                String key = (String)keys[j];
                String value = (String)map.get(key);

                System.out.println(key + " : " + value);
            }
      }
   }

   public ArrayList getr() 
   {
      return result;
   }

   private byte[] spacing(byte[] byteArr, int length)
   {
      byte[] retArr = new byte[length];
      int byteArr_size = byteArr.length;

      if (byteArr.length > length)
         byteArr_size = length;

      System.arraycopy(byteArr, 0, retArr, 0, byteArr_size);

      for (int i = byteArr_size; i < (length - byteArr_size); i++)
      {
         retArr[i] = ' ';
      }

      return retArr;
   }

   private String md5(String str)
   {
      StringBuffer hexString = new StringBuffer();
      byte[] byteStr = str.getBytes();
      try{
         MessageDigest algorithm = MessageDigest.getInstance("MD5");
         algorithm.reset();
         algorithm.update(byteStr);
         byte messageDigest[] = algorithm.digest();
             
         for (int i = 0; i < messageDigest.length; i++) {
            hexString.append(String.format("%02x", 0xFF & messageDigest[i]));
         }
      } catch(NoSuchAlgorithmException e) {
         System.out.println(e.getMessage());
      }
      return hexString.toString();
   }

   private byte[] assembleFields(SmsMessagePdu pdu) throws IOException
   {
      HashMap<String, String> map = new HashMap<String, String>();

      map.put("VERSION", VERSION_STR);
      map.put("COMMAND", "SEND");
      if (pdu.type != null) map.put("TYPE", pdu.type);
      if (pdu.messageId != null) map.put("MESSAGE-ID", pdu.messageId);
      if (pdu.groupId != null) map.put("GROUP-ID", pdu.groupId);
        if (pdu.destinationAddress != null) map.put("CALLED-NUMBER", pdu.destinationAddress);
        if (pdu.scAddress != null) map.put("CALLING-NUMBER", pdu.scAddress);
        if (pdu.subject != null) map.put("SUBJECT", pdu.subject);
        if (pdu.text != null) map.put("MESSAGE", pdu.text);
      if (pdu.refName != null) map.put("CALLED-NAME", pdu.refName);
      if (pdu.reservDate != null) map.put("RESERVE-DATE", pdu.reservDate);
      if (this.crypt.equals("MD5")) map.put("CRYPT-METHOD", "MD5");
        if (this.userid != null) map.put("AUTH-ID", this.userid);
      if (this.password != null) map.put("AUTH-PASS", this.password);
      map.put("LANGUAGE", "JAVA");
      if (this._charset != null)
         map.put("CHARSET", this._charset);
      if (this.app_version != null)
         map.put("APP-VERSION", this.app_version);

        CSCP cscp = new CSCP();
        if (pdu.imageFile != null) {
            File file = new File(pdu.imageFile);
            String filename = file.getName();
            map.put("ATTACHMENT", filename);
            FileInputStream reader = new FileInputStream(file);
            byte[] bytearray = new byte[3 + filename.length() + (int)file.length()];
            System.arraycopy(String.format("%3d", filename.length()).getBytes(), 0, bytearray, 0, 3);
            System.arraycopy(filename.getBytes(), 0, bytearray, 3, filename.length());
            reader.read(bytearray, 3 + filename.length(), (int)file.length());
            cscp.addparam(CSCPPARAM.ID_ATTACHMENT, bytearray);
        }
      cscp.addparam(CSCPPARAM.ID_MESSAGE, TBSP.build(map));
        return cscp.build();
   }
}

class SmsMessagePdu
{
    public String type;
    public String destinationAddress;
    public String scAddress;
    public String subject;
    public String text;
    public String reservDate;
    public String refName;
    public String messageId;
    public String groupId;
    public String imageFile;
    public int delayCount;

    SmsMessagePdu()
    {
        type="SMS";
        delayCount=0;
    }
}

class TBSP
{
   public static HashMap<String,String> parse(String tbspstr)
   {
      HashMap<String,String> map = new HashMap<String,String>();

      String[] strarr = tbspstr.split("\n");

      for (int i = 0; i < strarr.length; i++)
      {
         String line = strarr[i];
         String keyval[] = line.split(":");
         if (keyval.length < 1)
            continue;
         String key = keyval[0];
         String value = keyval[1];

         if (key.equals("MESSAGE"))
         {
            if (map.containsKey(key))
            {
               String tmpval = map.get(key);
               map.put(key, tmpval + "\n" + value);
            } else {
               map.put(key, value);
            }
         } else {
            map.put(key, value);
         }
      }

      return map;
   }

   public static String build(HashMap map)
   {
      String tbspstr="";

      Set set = map.keySet();
      Object[] keys = set.toArray();
      for (int i = 0; i < keys.length; i++)
      {
         String key = (String)keys[i];
         String value = (String)map.get(key);
         if (key.equals("MESSAGE"))
         {
            String[] strarr = value.split("\n");
            for (int j = 0; j < strarr.length; j++)
            {
               tbspstr = tbspstr + key + ":" + strarr[j] + "\n";
            }
         } else {
            tbspstr = tbspstr + key + ":" + value + "\n";
         }
      }
      return tbspstr;
   }
}


class CSCP
{
   public static int CSCP_VERSION_SIZE = 7;
   public static int CSCP_BODYLEN_SIZE = 8;
   private ArrayList<CSCPPARAM> params;
    private int bodylen = 0;
    private byte[] packet;

    CSCP()
    {
        params = new ArrayList<CSCPPARAM>();
    }


   public String read(BufferedInputStream bis) throws IOException
   {
      byte[] version = new byte[CSCP_VERSION_SIZE];
      byte[] bodylen = new byte[CSCP_BODYLEN_SIZE];
      byte[] body = new byte[0];

      bis.read(version, 0, CSCP_VERSION_SIZE);
      bis.read(bodylen, 0, CSCP_BODYLEN_SIZE);
      int ibodylen = Integer.valueOf((new String(bodylen)).trim());
      if (ibodylen > 0)
      {
            int restlen = ibodylen;
            do {
                CSCPPARAM param = new CSCPPARAM();
                param.read(bis);
                restlen -= param.size();
                add(param);
            } while(restlen > 0);
         body = new byte[ibodylen];
         //bis.read(body, 0, ibodylen);
      }
      return new String(body);
   }

   public byte[] build()
   {
        this.packet = new byte[CSCP_VERSION_SIZE + CSCP_BODYLEN_SIZE + bodylen];
        int p=0;
        System.arraycopy("CSCP2.0".getBytes(), 0, this.packet, p, CSCP_VERSION_SIZE);
        p+=CSCP_VERSION_SIZE;
        System.arraycopy(String.format("%8d", this.bodylen).getBytes(), 0, this.packet, p, CSCP_BODYLEN_SIZE);
        p+=CSCP_BODYLEN_SIZE;
        for (int i = 0; i < params.size(); i++) {
            byte[] bytearray = params.get(i).getPacketBytes();
            System.arraycopy(bytearray, 0, this.packet, p, bytearray.length);
            p+=bytearray.length;
        }
      return this.packet;
   }

    public byte[] getPacketBytes()
    {
        return this.packet;
    }

    public String getPacketString()
    {
        return new String(this.packet);
    }

    public void add(CSCPPARAM param)
    {
        this.params.add(param);
        this.bodylen += param.size();
    }

    public void addparam(String id, String body)
    {
        CSCPPARAM param = new CSCPPARAM();
        param.setId(id);
        param.setBody(body);
        param.build();
        add(param);
    }

    public void addparam(String id, byte[] body)
    {
        CSCPPARAM param = new CSCPPARAM();
        param.setId(id);
        param.setBody(body);
        param.build();
        add(param);
    }

    public CSCPPARAM getparam(String id)
    {
        for (int i = 0; i < this.params.size(); i++) {
            CSCPPARAM param = this.params.get(i);
            if (param.getId().equals(id)) return param;
        }
        return null;
    }
}

class CSCPPARAM
{
    private String id;
    private int bodylen;
    private byte[] body;
    private byte[] buf;
    static final int ID_SIZE = 2;
    static final int BODYLEN_SIZE = 8;
    static final String ID_MESSAGE = "ME";
    static final String ID_ATTACHMENT = "AT";

    public void CSCPPARAM()
    {
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setBody(byte[] body)
    {
        this.body = body;
        this.bodylen =  body.length;
    }

    public void setBody(String body)
    {
        setBody(body.getBytes());
    }

    public void build()
    {
        this.buf = new byte[ID_SIZE + BODYLEN_SIZE + this.bodylen];
        
        int p=0;
        System.arraycopy(this.id.getBytes(), 0, this.buf, p, ID_SIZE);
        p+=ID_SIZE;
        System.arraycopy(String.format("%8d", this.bodylen).getBytes(), 0, this.buf, p, BODYLEN_SIZE);
        p+=BODYLEN_SIZE;
      System.arraycopy(this.body, 0, this.buf, p, this.bodylen);
    }

    public boolean read(BufferedInputStream bis) throws IOException
    {
        byte[] id = new byte[ID_SIZE];
        byte[] bodylen = new byte[BODYLEN_SIZE];

        // id
      bis.read(id, 0, ID_SIZE);
        this.id = new String(id);
        // bodylen
        bis.read(bodylen, 0, BODYLEN_SIZE);
        this.bodylen = Integer.valueOf((new String(bodylen)).trim());
        // body
        if (this.bodylen > 0) {
            this.body = new byte[this.bodylen];
            bis.read(this.body, 0, this.bodylen);
        }
        return true;
    }

    public int size()
    {
        return (ID_SIZE + BODYLEN_SIZE + this.bodylen);
    }

    public int bodySize()
    {
        return this.bodylen;
    }

    public String getId()
    {
        return this.id;
    }

    public String getBody()
    {
        return new String(this.body);
    }

    public String getPacketString()
    {
        return new String(this.buf);
    }

    public byte[] getPacketBytes()
    {
        return this.buf;
    }
}

class ServerLoadBalance
{
    static final String HOSTS[] = {
        "alpha.coolsms.co.kr"
        , "bravo.coolsms.co.kr"
        , "delta.coolsms.co.kr"
    };
    static final int port = 80;
    private int timeout = 5000; // five seconds

    ServerLoadBalance() throws IOException
    {
    }

    public Socket __connect__(String host, int port)
    {
        Socket sock = null;

        try
        {
            sock = new Socket();
            sock.connect(new InetSocketAddress(host, port), this.timeout);
            return sock;
        }
        catch(IOException e)
        {
            return null;
        }
    }

    public Socket connect() throws IOException
    {
        Socket sock = null;

        for (int i = 0; i < this.HOSTS.length; i++) {
            sock = __connect__(this.HOSTS[i], this.port);
            if (sock != null) break;
        }

        return sock;
    }
}