package rpeck.loba_login_register;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Robert Peck on 11/4/2015.
 */
public class Encryption {
    //Class To Encrypt The Password Of The User

    //String To Return The Encrypted Password
    private String result;

    //Constructor That Accepts A String And Sends It To Be Encrypted, Then Is Stored Into The Class Variable result
    public Encryption(String s){
        result = runMD5(s);
    }

    //runMD5 Method Takes A String And Encrypts It Then Returns That Encrypted String.
    private String runMD5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")), 0, s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getResult() {
        return result;
    }
}
