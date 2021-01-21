import it.unisa.dia.gas.jpbc.Element;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class TestSS {
    @Test
    public void testCalculate() {
        SecretSharing ss = new SecretSharing();
        Element e = SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(2));
        Element b = SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(5));
        Element n = SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(3));

        Assert.assertEquals(ss.mod(e, n), SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(2)));
        Assert.assertEquals(ss.pow(e, n), SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(8)));
        Assert.assertEquals(ss.add(e, n), SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(5)));
        Assert.assertEquals(ss.mul(e, n), SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(6)));
        Assert.assertEquals(ss.sub(e, b, n), SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(0)));
        Assert.assertEquals(ss.div(e, b, n), SecretSharing.pairing.getZr().newElement(BigInteger.valueOf(1)));
    }

    @Test
    public void testAll() {
        SecretSharing ss = new SecretSharing();

        Element[] f = ss.chooseFx(3, 5);
        System.out.println("s" + ss.recover(f, 3));
    }
}
