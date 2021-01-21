import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class SecretSharing {
    public static TypeACurveGenerator pg = new TypeACurveGenerator(512, 512);
    public static PairingParameters typeAParams = pg.generate();
    public static Pairing pairing = PairingFactory.getPairing(typeAParams);
    public static Element p = pairing.getZr().newElement(
            BigInteger.probablePrime(7, new Random()));

    public Element[] chooseFx(int t, int n) {
        System.out.println("p" + p);
        Element[] a = new Element[t];
        for (int i = 0; i < t; i++) {
            Element temp = pairing.getZr().newRandomElement().getImmutable();
            while (temp.toBigInteger().compareTo(BigInteger.valueOf(0)) == 0) {
                temp = pairing.getZr().newRandomElement().getImmutable();
            }
            a[i] = mod(temp.duplicate(), p.duplicate());
        }
        System.out.println("a" + Arrays.toString(a));

        Element[] f = new Element[n + 1];
        Arrays.fill(f, pairing.getZr().newElement(0).getImmutable());

        for (int i = 1; i < n + 1; i++) {
            for (int j = 0; j < t; j++) {
                Element xPow = pow(pairing.getZr().newElement(i).getImmutable(),
                        pairing.getZr().newElement(j).getImmutable());
                Element aMulXPow = mul(a[j].duplicate(), xPow.duplicate());
                f[i] = mod(add(f[i], aMulXPow.duplicate()), p.duplicate());
            }
        }
        System.out.println("shares" + Arrays.toString(f));
        return f;
    }

    public Element recover(Element[] shares, int t) {
        Element s = pairing.getZr().newElement(0).getImmutable();
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
            mul = mul(mul.duplicate(), shares[i].duplicate());
            s = add(s.duplicate(), mul.duplicate());
        }
        return mod(s.duplicate(), p.duplicate());
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
