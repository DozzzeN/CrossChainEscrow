import org.junit.Test;

import javax.xml.ws.Holder;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestVE {
    @Test
    public void Test() {
        VerifiableEncryption ve = new VerifiableEncryption();
        for (int i = 0; i < 100; i++) {
            ve.SetUp();
            int m = 123; //1111011
            BigInteger[] c = ve.Com(m);
            assertTrue(ve.Open(m, c));

            int x = 1234;
            BigInteger y1 = VerifiableEncryption.KGen(x);
            VerifiableEncryption.Encryption[] c1c2 = ve.Enc(m, y1);
            assertEquals(ve.Dec(c1c2, x), m);

            Holder<BigInteger> e = new Holder<>();
            Holder<BigInteger[][]> z = new Holder<>();
            ve.VE(m, y1, e, z);
            if (!ve.VEV(e.value, z.value, c1c2, c, y1)) {
                throw new RuntimeException();
            }
        }
    }
}
