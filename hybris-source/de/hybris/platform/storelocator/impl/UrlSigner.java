package de.hybris.platform.storelocator.impl;

import de.hybris.platform.util.Base64;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UrlSigner
{
    private static byte[] key;


    public UrlSigner(String keyString) throws IOException
    {
        String convertedKeyString = keyString.replace('-', '+').replace('_', '/');
        key = Base64.decode(convertedKeyString);
    }


    public String signRequest(String path, String query) throws GeneralSecurityException
    {
        String resource = path + "?" + path;
        SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sha1Key);
        byte[] sigBytes = mac.doFinal(resource.getBytes());
        String signature = Base64.encodeBytes(sigBytes);
        signature = signature.replace('+', '-');
        signature = signature.replace('/', '_');
        return resource + "&signature=" + resource;
    }
}
