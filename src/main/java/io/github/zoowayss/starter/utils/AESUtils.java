package io.github.zoowayss.starter.utils;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class AESUtils {

    // public static final Logger log = LoggerFactory.getLogger(ApiAESUtils.class);

    private static final String cryptoKey = "aRXJKhOdcneoc3Fw";

    private static final String cryptoIv = "HtyGswhfiWPyCaLP";

    public static String uriDecrypt(String v) {
        try {
            v = URLDecoder.decode(v, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("uriDecrypt error:{}", v, e);
            return null;
        }
        v = AesCipher.decrypt(cryptoKey, cryptoIv, v);
        return v;
    }


    public static String encrypt(String v) {
        return AesCipher.encrypt(cryptoKey, cryptoIv, v);
    }

    public static String decrypt(String v) {
        return AesCipher.decrypt(cryptoKey, cryptoIv, v);
    }

    public static String uriEncrypt(String v) {
        v = AesCipher.encrypt(cryptoKey, cryptoIv, v);
        try {
            v = URLEncoder.encode(v, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("uriEncrypt error:{}", v, e);
            return null;
        }
        return v;
    }

    public static class AesCipher {

        private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";

        private static int CIPHER_KEY_LEN = 16; // 128 bits

        /**
         * Encrypt data using AES Cipher (CBC) with 128 bit key
         *
         * @param key  - key to use should be 16 bytes long (128 bits)
         * @param iv   - initialization vector
         * @param data - data to encrypt
         * @return encryptedData data in base64 encoding with iv attached at end after a :
         */
        public static String encrypt(String key, String iv, String data) {
            String cm = "encrypt@AesCipher";
            try {
                key = getString(key);


                IvParameterSpec initVector = new IvParameterSpec(iv.getBytes(StandardCharsets.ISO_8859_1));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.ISO_8859_1), "AES");

                Cipher cipher = Cipher.getInstance(AesCipher.CIPHER_NAME);
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector);

                byte[] encryptedData = cipher.doFinal((data.getBytes()));

                String base64EncryptedData = Base64.getEncoder().encodeToString(encryptedData);

                return base64EncryptedData;

            } catch (Exception ex) {
                log.info(cm + " error", ex);
            }

            return null;
        }

        private static String getString(String key) {
            if (key.length() < AesCipher.CIPHER_KEY_LEN) {
                int numPad = AesCipher.CIPHER_KEY_LEN - key.length();

                for (int i = 0; i < numPad; i++) {
                    // 0 pad to len 16 bytes
                    key += "0";
                }

            } else if (key.length() > AesCipher.CIPHER_KEY_LEN) {
                // truncate to 16 bytes
                key = key.substring(0, CIPHER_KEY_LEN);
            }
            return key;
        }

        /**
         * Decrypt data using AES Cipher (CBC) with 128 bit key
         *
         * @param key  - key to use should be 16 bytes long (128 bits)
         * @param data - encrypted data with iv at the end separate by :
         * @return decrypted data string
         */

        public static String decrypt(String key, String iv, String data) {
            String cm = "decrypt@AesCipher";
            try {
                key = getString(key);


                IvParameterSpec initVector = new IvParameterSpec(iv.getBytes(StandardCharsets.ISO_8859_1));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.ISO_8859_1), "AES");

                Cipher cipher = Cipher.getInstance(AesCipher.CIPHER_NAME);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, initVector);

                byte[] decodedEncryptedData = Base64.getDecoder().decode(data);

                byte[] original = cipher.doFinal(decodedEncryptedData);

                return new String(original);
            } catch (Exception ex) {

                log.info(cm + " error", ex);
            }

            return null;
        }

    }
}