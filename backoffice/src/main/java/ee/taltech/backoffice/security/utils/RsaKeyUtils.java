package ee.taltech.backoffice.security.utils;

import ee.taltech.backoffice.game.controller.errorHandling.SystemError;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static ee.taltech.backoffice.game.controller.errorHandling.SystemError.Code.SERVER_ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
// NB! Do not remove static because 'optimize imports' removes .* from imports in some cases
public class RsaKeyUtils {

    public static RSAPublicKey publicKey(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            // read resource
            String keyString = new String(inputStream.readAllBytes(), UTF_8);
            byte[] keyBytes = Base64.getMimeDecoder().decode(getPublicKeyData(keyString));

            // create pubic key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SystemError(SERVER_ERROR, "Cannot instantiate a public key");
        }
    }

    public static RSAPrivateKey privateKey(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            // read resource
            String keyString = new String(inputStream.readAllBytes(), UTF_8);
            byte[] keyBytes = Base64.getMimeDecoder().decode(getPrivateKeyData(keyString));

            // create private key
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SystemError(SERVER_ERROR, "Cannot instantiate a private key");
        }
    }

    private static String getPublicKeyData(String publicKey) {
        return publicKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n?|-+END PUBLIC KEY-+\\r?\\n?)", "");
    }

    private static String getPrivateKeyData(String publicKey) {
        return publicKey.replaceAll("(-+BEGIN PRIVATE KEY-+\\r?\\n?|-+END PRIVATE KEY-+\\r?\\n?)", "");
    }

}
