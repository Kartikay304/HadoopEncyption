package org.hadoopEncryption;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    public static void main(String[] args) throws Exception {
        generateKeyPair();
    }

    //This function generates RSA key pairs, public and private keys.
    public static void generateKeyPair()throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PrivateKey aPrivate = kp.getPrivate();  // private key
        PublicKey aPublic = kp.getPublic();     // public key
        try (FileOutputStream outPrivate = new FileOutputStream("private.key")) {
            outPrivate.write(aPrivate.getEncoded());   //privateKey is stored in file system.
        }
        try (FileOutputStream outPublic = new FileOutputStream("public.key")) {
            outPublic.write(aPublic.getEncoded());    //publicKey is stored in file system.
        }
    }

    //This function encrypt the message using RSA.
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    //This function encrypt the message using RSA.
    public static String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    //This function return the public key by loading public key file from file system.
    public static PublicKey loadPublicKey() throws Exception {
        File publicKeyFile = new File("public");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return publicKeyFactory.generatePublic(publicKeySpec);
    }

    //This function return the private key by loading private key file from file system.
    public static PrivateKey loadPrivateKey() throws Exception{
        File privateKeyFile = new File("private");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());

        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return privateKeyFactory.generatePrivate(privateKeySpec);
    }
}
