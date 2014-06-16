package test.Controllers;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import test.config.ApplicationConfig;
import test.config.DealerRepository;
import test.config.SpecialRepository;
import test.config.UserRepository;
import test.model.Special;
import test.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by maharb on 6/11/14.
 */

@Controller
@RequestMapping(value="/v1/specials")
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SpecialRepository specialRepository;
    @Autowired
    DealerRepository dealerRepository;

    private DateFormat dateFormat;

    public MainController(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        userRepository = (UserRepository) ctx.getBean("userRepository");
        specialRepository = (SpecialRepository) ctx.getBean("specialRepository");
        dealerRepository = (DealerRepository) ctx.getBean("dealerRepository");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @RequestMapping(value="/test")
    @ResponseBody
    public String test(){
        return "HELLO";
    }

    @RequestMapping(value="/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user){
        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " is trying to login.");


        User check = userRepository.findByUsername(user.getUsername());

        try {
            if(!validatePassword(user.getPassword(), check.getPassword())){
                System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " failed to login.");

                return new ResponseEntity<String>(jsonGen("Invalid Username or Password"),HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        System.out.println(dateFormat.format(new Date()) + "  INFO: " + user.getUsername() + " logged in successfully.");
        return new ResponseEntity<String>(jsonGen("Login Success") ,HttpStatus.OK);
    }

    @RequestMapping(value="/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody User user){
        try {
            String securePass = generateStorngPasswordHash(user.getPassword());
            user.setPassword(securePass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        userRepository.save(user);
        return new ResponseEntity<String>(jsonGen("Registered"), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> createSpecial(@RequestBody Special special){

        return new ResponseEntity<String>(jsonGen("Created Special"), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody Special special){

        return new ResponseEntity<String>(jsonGen("Updated") ,HttpStatus.OK);
    }

    @RequestMapping(value = "/getSpecial", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Special> getSpecial(@RequestBody String info){

        return new ResponseEntity<Special>(new Special() ,HttpStatus.OK);
    }

    private String jsonGen(String response){
        JSONObject returnObj = new JSONObject();
        try {
            returnObj.put("response", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnObj.toString();
    }

    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 10000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
