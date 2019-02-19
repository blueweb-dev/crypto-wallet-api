package crypto.wallet.decoder;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.cipher.CryptoCipherFactory;
import org.apache.commons.crypto.cipher.CryptoCipherFactory.CipherProvider;
import org.apache.commons.crypto.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.util.CipherUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 기타 테스트
 */
@Slf4j
@SpringBootTest @RunWith(SpringRunner.class) public class CipherTest implements WalletConst {
  
  
    public void main(String[] args) {
        Double val = new BigInteger("b469471f80140000", 16).doubleValue();
        log.debug("val " + val);
    }
    
    @Test public void switchUnit() {
        BigInteger amount = new BigInteger("" + (1.333 * 10000000D), 16);
//        Double amt = WalletUtil.hexToEtherWith0x("1042351c984894000");
        log.info("amt " + amount);
    }

//    @Test
    public void testCipher() throws IOException, InvalidKeyException, InvalidAlgorithmParameterException
                    , ShortBufferException, IllegalBlockSizeException, BadPaddingException {
      
        final SecretKeySpec key  = new SecretKeySpec(CipherUtil.getUTF8Bytes("1234567890123456"),"AES");
        final IvParameterSpec iv = new IvParameterSpec(CipherUtil.getUTF8Bytes("1234567890123456"));
    
        Properties properties  = new Properties();
        properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CipherProvider.OPENSSL.getClassName());
        // Creates a CryptoCipher instance with the transformation and properties.
        final String transform = "AES/CBC/PKCS5Padding";
        CryptoCipher encipher  = Utils.getCipherInstance(transform, properties);
        log.debug("Cipher:  " + encipher.getClass().getCanonicalName());
    
        final String sampleInput = "hello world!";
    
        byte[] input = CipherUtil.getUTF8Bytes(sampleInput);
        byte[] output = new byte[32];
    
        // Initializes the cipher with ENCRYPT_MODE, key and iv.
        encipher.init(Cipher.ENCRYPT_MODE, key, iv);
        // Continues a multiple-part encryption/decryption operation for byte array.
        int updateBytes = encipher.update(input, 0, input.length, output, 0);
//        log.debug("updateBytes " + updateBytes);
        // We must call doFinal at the end of encryption/decryption.
        int finalBytes = encipher.doFinal(input, 0, 0, output, updateBytes);
//        log.debug("finalBytes " + finalBytes);
        // Closes the cipher.
        encipher.close();
    
        log.debug(Arrays.toString(Arrays.copyOf(output, updateBytes+finalBytes)));
    
        // Now reverse the process using a different implementation with the same settings
        properties.setProperty(CryptoCipherFactory.CLASSES_KEY, CipherProvider.JCE.getClassName());
        CryptoCipher decipher = Utils.getCipherInstance(transform, properties);
//        log.debug("Cipher:  " + encipher.getClass().getCanonicalName());
    
        decipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte [] decoded = new byte[32];
        decipher.doFinal(output, 0, updateBytes + finalBytes, decoded, 0);
        log.debug("input:  " + sampleInput + ", output: " + new String(decoded, StandardCharsets.UTF_8));
        
        assertThat(sampleInput).isEqualTo(new String(decoded, StandardCharsets.UTF_8));
    }

}
