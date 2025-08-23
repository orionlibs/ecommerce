package de.hybris.platform.couponservices.couponcodegeneration.impl;

import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeGenerationException;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodesGenerator;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CouponCodesInputStream extends InputStream
{
    private static final String STRING_SEPARATOR = "\n";
    private static final Logger LOG = LoggerFactory.getLogger(CouponCodesInputStream.class);
    private final CouponCodesGenerator couponCodesGenerator;
    private final MultiCodeCouponModel coupon;
    private final int batchSize;
    private int totalSize;
    private int currentCouponPos;
    private int pos;
    private int count;
    private byte[] buf;


    public CouponCodesInputStream(MultiCodeCouponModel coupon, CouponCodesGenerator couponCodesGenerator, int batchSize, int totalSize)
    {
        this.coupon = coupon;
        this.couponCodesGenerator = couponCodesGenerator;
        this.batchSize = batchSize;
        this.totalSize = totalSize;
    }


    public synchronized int read() throws IOException
    {
        if(this.pos == this.count)
        {
            readNextChunkOfCodes();
        }
        return (this.pos < this.count) ? (this.buf[this.pos++] & 0xFF) : -1;
    }


    public int available() throws IOException
    {
        return this.totalSize - this.currentCouponPos + this.count - this.pos;
    }


    protected void readNextChunkOfCodes()
    {
        int nextCodesChunkSize = (this.currentCouponPos + this.batchSize < this.totalSize) ? this.batchSize : (this.totalSize - this.currentCouponPos);
        StringJoiner stringJoiner = new StringJoiner("\n");
        try
        {
            for(int i = 0; i < nextCodesChunkSize; i++)
            {
                stringJoiner.add(this.couponCodesGenerator.generateNextCouponCode(this.coupon));
                this.currentCouponPos++;
            }
        }
        catch(CouponCodeGenerationException ex)
        {
            if(ex.getErrorCode() == 500)
            {
                this.totalSize = this.currentCouponPos;
                LOG.warn(ex.getMessage());
            }
            else
            {
                throw ex;
            }
        }
        String chunkString = stringJoiner.toString();
        this.count = stringJoiner.length();
        this.pos = 0;
        if(this.currentCouponPos < this.totalSize)
        {
            chunkString = chunkString + "\n";
            this.count++;
        }
        this.buf = chunkString.getBytes(Charset.forName("UTF-8"));
    }


    public int getGeneratedCouponsCount()
    {
        return this.currentCouponPos;
    }
}
