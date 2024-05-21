package org.hadoopEncryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class AES {
    public static void main(String[] args) throws Exception {
        generateKey();
    }
    // This function generates IV. IV is a pseudo-random value and has the same size as the block that is encrypted.
    public static byte[] generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return  iv;
    }
     // This function generates AES symmetric key and encrypt it using RSA public key before storing it in file system.
     public static void generateKey() throws Exception {
         KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
         keyGenerator.init(128);
         SecretKey key = keyGenerator.generateKey();        // 128-bit secrete key is generated and stored in key.

         byte[] symmetric_key =key.getEncoded(); //<--------------------
         Cipher piper = Cipher.getInstance("RSA");
         piper.init(Cipher.ENCRYPT_MODE, loadPublicKey());
         byte[] encrypted_key= piper.doFinal(symmetric_key);  // 128-bit symmetric key is encrypted using RSA

         byte[] iv = generateIv();  // IV values generated.
         try (FileOutputStream outFour_key = new FileOutputStream("four.key")) {
             outFour_key.write(iv);           //Four_key is stored in system.
         }
         try (FileOutputStream outSecrete_Key = new FileOutputStream("secrete.key")) {
             outSecrete_Key.write(encrypted_key);    //Encrypted secrete key is stored in system.
         }
     }

     // This functions takes input as Plain-Text and return Cipher-Text using AES encryption algorithm.
    public static String encrypt(String input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        File secereteKeyFile = new File("secrete");    //secrete Key is retrieved from file.
        byte[] publicKeyBytes = Files.readAllBytes(secereteKeyFile.toPath());

        Cipher piper = Cipher.getInstance("RSA");//<-----------------------
        piper.init(Cipher.DECRYPT_MODE, RSA.loadPrivateKey());
        byte[] decrypt = piper.doFinal(publicKeyBytes);    // secrete key is decrypted using RSA
        SecretKey originalKey = new SecretKeySpec(decrypt, 0, decrypt.length, "AES");

        File four = new File("four");     //four Key is retrieved from file.
        byte[] four_Key = Files.readAllBytes(four.toPath());
        IvParameterSpec ivspec = new IvParameterSpec(four_Key);
        cipher.init(Cipher.ENCRYPT_MODE, originalKey, ivspec);

        byte[] cipherText = cipher.doFinal(input.getBytes());    // file contents gets encrypted using AES.
        return Base64.getEncoder().encodeToString(cipherText);
    }
    // This functions takes input as Cipher-Text and return Plain-Text using AES encryption algorithm.
    public static String decrypt(String cipherText) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        File secereteKeyFile = new File("secrete");  //secrete Key is retrieved from file.
        byte[] publicKeyBytes = Files.readAllBytes(secereteKeyFile.toPath());

        Cipher pipher = Cipher.getInstance("RSA");//<-----------------------
        pipher.init(Cipher.DECRYPT_MODE, RSA.loadPrivateKey());
        byte[] decrypt = pipher.doFinal(publicKeyBytes);  // secrete key is decrypted using RSA
        SecretKey originalKey = new SecretKeySpec(decrypt, 0, decrypt.length, "AES");

        File four = new File("four");  //four Key is retrieved from file.
        byte[] fourt = Files.readAllBytes(four.toPath());
        IvParameterSpec ivspec = new IvParameterSpec(fourt);
        cipher.init(Cipher.DECRYPT_MODE, originalKey, ivspec);

        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText)); // file contents gets encrypted using AES.
        return new String(plainText);
    }
    public static PublicKey loadPublicKey() throws Exception {
        File publicKeyFile = new File(new URI("file:///home/ubuntu/hadoopEncryption/public.key"));    // RSA public key is retrieved, to encrypt AES secrete key.
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return publicKeyFactory.generatePublic(publicKeySpec);                   //public key is returned
    }
}

