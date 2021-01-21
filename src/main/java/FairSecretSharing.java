import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import util.ArraysUtil;
import util.CryptoUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class FairSecretSharing {
    public static TypeACurveGenerator pg = new TypeACurveGenerator(512, 512);
    public static PairingParameters typeAParams = pg.generate();
    public static Pairing pairing = PairingFactory.getPairing(typeAParams);
    public static Element p = pairing.getZr().newElement(
            BigInteger.probablePrime(7, new Random()));
    public static byte[] v;
    public static byte[][] vi;

    public Element[][] chooseFx(int d, int t, int n) {
        int ind = new Random().nextInt(d - 1) + 1; //1 <= ind <= d-1
        System.out.println("ind:" + ind);
        System.out.println("p:" + p);
        Element[][] a = new Element[d][t];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < t; j++) {
                Element temp = pairing.getZr().newRandomElement().getImmutable();
                while (temp.toBigInteger().compareTo(BigInteger.valueOf(0)) == 0) {
                    temp = pairing.getZr().newRandomElement().getImmutable();
                }
                a[i][j] = mod(temp.duplicate(), p.duplicate());
            }
        }

        vi = new byte[d][];
        for (int i = 0; i < d; i++) {
            System.out.println("a:" + Arrays.toString(a[i]));
            vi[i] = CryptoUtil.getHash("SHA-256", a[i][0]);
        }

        v = CryptoUtil.getHash("SHA-256", ArraysUtil.mergeByte(a[ind - 1][0].toBytes(), a[ind][0].toBytes()));

        Element[][] f = new Element[d][n + 1];
        for (int i = 0; i < d; i++) {
            Arrays.fill(f[i], pairing.getZr().newElement(0).getImmutable());
        }

        for (int r = 0; r < d; r++) {
            for (int i = 1; i < n + 1; i++) {
                for (int j = 0; j < t; j++) {
                    Element xPow = pow(pairing.getZr().newElement(i).getImmutable(),
                            pairing.getZr().newElement(j).getImmutable());
                    Element aMulXPow = mul(a[r][j].duplicate(), xPow.duplicate());
                    f[r][i] = mod(add(f[r][i], aMulXPow.duplicate()), p.duplicate());
                }
            }
        }

        for (int i = 0; i < d; i++) {
            System.out.println("shares:" + Arrays.toString(f[i]));
        }
        return f;
    }

    public Element recover(Element[][] shares, int d, int t) {
        Element[] s = new Element[d];
        Arrays.fill(s, pairing.getZr().newElement(0).getImmutable());
        for (int r = 0; r < d; r++) {
            for (int i = 1; i < t + 1; i++) {
                Element mul = pairing.getZr().newElement(1).getImmutable();
                for (int j = 1; j < t + 1; j++) {
                    Element iElement = pairing.getZr().newElement(i).getImmutable();
                    Element jElement = pairing.getZr().newElement(j).getImmutable();
                    if (i == j) continue;
                    Element iSubj = sub(iElement.duplicate(), jElement.duplicate(), p.duplicate());
                    Element iDiviSubj = div(jElement.duplicate(), iSubj.duplicate(), p.duplicate());
                    mul = mul(mul.duplicate(), iDiviSubj.duplicate());
                }
                mul = mul(mul.duplicate(), shares[r][i].duplicate());
                s[r] = add(s[r].duplicate(), mul.duplicate());
            }
            s[r] = mod((s[r].duplicate()), p.duplicate());
            if (r > 0) {
                for (int i = 0; i < t + 1; i++) {
                    if (!ArraysUtil.isEqual(CryptoUtil.getHash("SHA-256", s[r]), vi[r])) {
                        throw new RuntimeException();
                    }
                }
                if (ArraysUtil.isEqual(CryptoUtil.getHash("SHA-256", ArraysUtil.mergeByte(
                        s[r - 1].toBytes(), s[r].toBytes())), v)) {
                    return s[r - 1].duplicate();
                }
            }
        }
        return null;
    }

    public Element mod(Element e, Element n) {
        BigInteger result = e.duplicate().toBigInteger().mod(n.duplicate().toBigInteger());
        return pairing.getZr().newElement(result).getImmutable();
    }

    public Element pow(Element e, Element n) {
        if (e.toBigInteger().compareTo(BigInteger.valueOf(1)) == 0) {
            return pairing.getZr().newElement(1).getImmutable();
        } else if (n.toBigInteger().compareTo(BigInteger.valueOf(0)) == 0) {
            return pairing.getZr().newElement(1).getImmutable();
        }
        BigInteger result = e.duplicate().toBigInteger().pow(n.duplicate().toBigInteger().intValue());
        return pairing.getZr().newElement(result).getImmutable();
    }

    public Element mul(Element e, Element n) {
        BigInteger result = e.duplicate().toBigInteger().multiply(n.duplicate().toBigInteger());
        return pairing.getZr().newElement(result).getImmutable();
    }

    public Element add(Element e, Element n) {
        BigInteger result = e.duplicate().toBigInteger().add(n.duplicate().toBigInteger());
        return pairing.getZr().newElement(result).getImmutable();
    }

    public Element sub(Element e, Element b, Element n) {
        if (e.duplicate().toBigInteger().compareTo(b.duplicate().toBigInteger()) < 0) {
            BigInteger sub = b.duplicate().toBigInteger().subtract(e.duplicate().toBigInteger());
            sub = sub.mod(n.duplicate().toBigInteger());
            sub = n.duplicate().toBigInteger().subtract(sub);
            return mod(pairing.getZr().newElement(sub), n.duplicate());
        }
        return mod(pairing.getZr().newElement(
                e.duplicate().toBigInteger().subtract(b.duplicate().toBigInteger())).getImmutable(), n.duplicate());
    }

    public Element div(Element a, Element b, Element n) {
        return mod(mul(a.duplicate(),
                pairing.getZr().newElement(
                        b.duplicate().toBigInteger().modInverse(n.duplicate().toBigInteger())).getImmutable()).getImmutable(), n.duplicate());
    }
}
