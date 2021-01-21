import it.unisa.dia.gas.jpbc.Element;

import javax.xml.ws.Holder;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Escrow {
    public static FairSecretSharing fss = new FairSecretSharing();
    public static VerifiableEncryption veA = new VerifiableEncryption();
    public static VerifiableEncryption veB = new VerifiableEncryption();

    public static BigInteger pkA;
    public static BigInteger skA;
    public static BigInteger pkB;
    public static BigInteger skB;
    public static BigInteger pkM;
    public static BigInteger skM;

    public static BigInteger[] cA;
    public static VerifiableEncryption.Encryption[] c1c2A;
    public static Holder<BigInteger> eA;
    public static Holder<BigInteger[][]> zA;

    public static BigInteger[] cB;
    public static VerifiableEncryption.Encryption[] c1c2B;
    public static Holder<BigInteger> eB;
    public static Holder<BigInteger[][]> zB;

    public static int xA;
    public static BigInteger[] rA;

    public static int xB;
    public static BigInteger[] rB;

    public void SetUp() {
        Element[][] f = fss.chooseFx(7, 3, 5);
        skM = fss.recover(f, 7, 3).toBigInteger();

        veA.SetUp();
        veB.SetUp();

        skA = new BigInteger(128, new SecureRandom());
        skB = new BigInteger(128, new SecureRandom());

        pkA = VerifiableEncryption.KGen(skA.intValue());
        pkB = VerifiableEncryption.KGen(skB.intValue());
        pkM = VerifiableEncryption.KGen(skM.intValue());
    }

    public void LockA() {
        xA = new Random().nextInt(128);
        cA = veA.Com(xA);

        c1c2A = veA.Enc(xA, pkM);
        eA = new Holder<>();
        zA = new Holder<>();

        veA.VE(xA, pkM, eA, zA);
    }

    public void LockB() {
        xB = new Random().nextInt(128);
        cB = veB.Com(xB);

        c1c2B = veB.Enc(xB, pkM);
        eB = new Holder<>();
        zB = new Holder<>();

        veB.VE(xB, pkM, eB, zB);
    }

    public void UnlockA() {
        if (veB.VEV(eB.value, zB.value, c1c2B, cB, pkM)) {
            rB = veB.r;
        }
    }

    public void UnlockB() {
        if (veA.VEV(eA.value, zA.value, c1c2A, cA, pkM)) {
            rA = veA.r;
        }
    }

    public void ArbitrateA(VerifiableEncryption.Encryption[] c1c2B) {
        int xB = veB.Dec(c1c2B, skM.intValue());
    }

    public void ArbitrateB(VerifiableEncryption.Encryption[] c1c2A) {
        int xA = veA.Dec(c1c2A, skM.intValue());
    }
}