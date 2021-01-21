import util.ArraysUtil;
import util.CryptoUtil;

import javax.xml.ws.Holder;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class VerifiableEncryption {
    //Pederson
    public static BigInteger q;
    public static BigInteger p;
    public static BigInteger g;
    public static BigInteger h;
    //ElGamal
    public static BigInteger g1;
    public static BigInteger g2;
    public int l;
    public BigInteger[] r;
    public BigInteger[] k;

    public static BigInteger randomFromZn(BigInteger n, Random rand) {
        BigInteger result;
        do {
            result = new BigInteger(n.bitLength(), rand);
            // 验证result小于n，即在群Zn中
        } while (result.compareTo(n) != -1 && result.compareTo(BigInteger.ZERO) > 0);
        return result;
    }

    public static BigInteger KGen(int x) {
        return g1.modPow(BigInteger.valueOf(x), q);
    }

    public void SetUp() {
        //q is Sophie Germain Prime, such as 2, 3, 5, 11, 23, 29, 41, 53, 83, 89, 113, 131, 173, 179, 191, 233, 239,
        //251, 281, 293, 359, 419, 431, 443, 491, 509, 593, 641, 653, 659, 683, 719, 743, 761, 809, 911, 953, 1013,
        //1019, 1031, 1049, 1103, 1223, 1229, 1289, 1409, 1439, 1451, 1481, 1499, 1511, 1559
        q = new BigInteger("254360121685046047230972737627095456373");
        p = new BigInteger("508720243370092094461945475254190912747");
        g = new BigInteger("6241954061487357500630752602811797628281211078835258007995614065175462432164");
        h = new BigInteger("222558795700244843773680524510954879013963336280030783717240433143752288480625");
        g1 = new BigInteger("45598057705151672169360740558625890332");
        g2 = new BigInteger("68854564261539979992154011358882143411");
//        while (!q.add(q).add(BigInteger.ONE).isProbablePrime(100)) {
//            q = BigInteger.probablePrime(7, new Random());
//        }
//        p = q.add(q).add(BigInteger.ONE);
//        g = randomFromZn(p, new Random()).pow(2);
//        h = randomFromZn(p, new Random()).pow(2);
//
//        g1 = randomFromZn(q, new Random());
//        while (g1.compareTo(BigInteger.TWO) < 0) {
//            g1 = randomFromZn(q, new Random());
//        }
//
//        g2 = randomFromZn(q, new Random());
//        while (g2.compareTo(BigInteger.TWO) < 0) {
//            g2 = randomFromZn(q, new Random());
//        }
    }

    public BigInteger[] Com(int m) {
        String mi = Integer.toBinaryString(m);
        this.l = mi.length();
        p = q.add(q).add(BigInteger.ONE);
        this.r = new BigInteger[this.l];
        BigInteger[] c = new BigInteger[this.l];
        for (int i = 0; i < this.l; i++) {
            int _mi = mi.charAt(i) - '0';
            this.r[i] = randomFromZn(q, new Random());
            c[i] = g.modPow(BigInteger.valueOf(_mi), p).multiply(h.modPow(this.r[i], p)).mod(p);
        }
        System.out.println("r:" + Arrays.toString(this.r));
        return c;
    }

    public boolean Open(int m, BigInteger[] c) {
        String mi = Integer.toBinaryString(m);
        boolean isSuccess = true;
        for (int i = 0; i < this.l; i++) {
            int _mi = mi.charAt(i) - '0';
            if (!g.modPow(BigInteger.valueOf(_mi), p).multiply(h.modPow(this.r[i], p)).mod(p).equals(c[i])) {
                isSuccess = false;
                break;
            }
        }
        return isSuccess;
    }

    public Encryption[] Enc(int m, BigInteger y1) {
        String mi = Integer.toBinaryString(m);
        this.k = new BigInteger[this.l];
        for (int i = 0; i < this.l; i++) {
            this.k[i] = randomFromZn(q, new Random());
        }
        System.out.println("k:" + Arrays.toString(this.k));

        Encryption[] c1c2 = new Encryption[this.l];
        for (int i = 0; i < this.l; i++) {
            int _mi = mi.charAt(i) - '0';
            c1c2[i] = new Encryption(
                    g1.modPow(this.k[i], q),
                    y1.modPow(this.k[i], q).multiply(g2.modPow(BigInteger.valueOf(_mi), q)));
        }
        return c1c2;
    }

    public int Dec(Encryption[] c1c2, int x) {
        BigInteger[] m_prime = new BigInteger[this.l];
        StringBuilder m = new StringBuilder();
        for (int i = 0; i < this.l; i++) {
            m_prime[i] = c1c2[i].getC2().multiply((c1c2[i].getC1().modPow(BigInteger.valueOf(x), q)).modInverse(q)).mod(q);
            if (m_prime[i].compareTo(g2) == 0) {
                m.append('1');
            }
            if (m_prime[i].compareTo(BigInteger.ONE) == 0) {
                m.append('0');
            }
        }
        return Integer.parseInt(m.toString(), 2);
    }

    public void VE(int m, BigInteger y1, Holder<BigInteger> e, Holder<BigInteger[][]> z) {
        if (e == null) {
            e = new Holder<>();
        }
        if (z == null) {
            z = new Holder<>();
        }
        z.value = new BigInteger[5][this.l];

        String mi = Integer.toBinaryString(m);

//        BigInteger[] ak = new BigInteger[this.l];
//        BigInteger[] am = new BigInteger[this.l];
//        BigInteger[] a = new BigInteger[this.l];
//        BigInteger[] ar = new BigInteger[this.l];
//
//        for (int i = 0; i < this.l; i++) {
//            ak[i] = randomFromZn(q, new Random());
//            am[i] = randomFromZn(q, new Random());
//            a[i] = randomFromZn(q, new Random());
//            ar[i] = randomFromZn(q, new Random());
//        }

        BigInteger[] ak = new BigInteger[]{
                new BigInteger("174423945970789179179967234322171302908"),
                new BigInteger("10568559441743620097552385236747960769"),
                new BigInteger("162858031504999031912126240165664381177"),
                new BigInteger("251895346406853436495753729357328407466"),
                new BigInteger("60390344022449072617013771914746365407"),
                new BigInteger("242228740582429699931227355738072105992"),
                new BigInteger("92228507778158672538237300945897786434")};
        BigInteger[] am = new BigInteger[]{
                new BigInteger("209915095021953647873141204296809788421"),
                new BigInteger("60967490728307028935040468312758254246"),
                new BigInteger("13072434689593245509014654079463946826"),
                new BigInteger("117954820124335413041056894578037396693"),
                new BigInteger("110193586332837066443579809811483259456"),
                new BigInteger("208871652834202167254225865355131213506"),
                new BigInteger("232853185391741026727397550899381357003")};
        BigInteger[] a = new BigInteger[]{
                new BigInteger("61836245865919958629230025907014379835"),
                new BigInteger("74758940982467916426826404385060965824"),
                new BigInteger("208044248448133882196498089191296567887"),
                new BigInteger("16612476548050582458813656237866579497"),
                new BigInteger("217827495615740623130350659218521003846"),
                new BigInteger("126176848148022641130410349606545656306"),
                new BigInteger("193600687786935625334845946912618283129")};
        BigInteger[] ar = new BigInteger[]{
                new BigInteger("85958440518351075511884820086873244948"),
                new BigInteger("94456847706381983109220074246779777286"),
                new BigInteger("198458377717368842574405363110775341601"),
                new BigInteger("125438419384021096502604305393463253741"),
                new BigInteger("218235259822952981060147071861165445955"),
                new BigInteger("94859922298142963393557116326401302688"),
                new BigInteger("42633142665470634930823770415873080549")};
        BigInteger[] Y1 = new BigInteger[this.l];
        BigInteger[] Y2 = new BigInteger[this.l];
        BigInteger[] Y3 = new BigInteger[this.l];
        BigInteger[] Y4 = new BigInteger[this.l];
        byte[] merge = new byte[0];

        for (int i = 0; i < this.l; i++) {
            int _mi = mi.charAt(i) - '0';
            Y1[i] = g1.modPow(ak[i], q);
            Y2[i] = y1.modPow(ak[i], q).multiply(g2.modPow(am[i], q)).mod(q);
            Y3[i] = y1.modPow(a[i], q).multiply(g2.modPow(BigInteger.valueOf(_mi).multiply(am[i]), q)).mod(q);
            Y4[i] = g.modPow(am[i], p).multiply(h.modPow(ar[i], p)).mod(p);
            merge = ArraysUtil.mergeByte(merge, Y1[i].toByteArray(), Y2[i].toByteArray(), Y3[i].toByteArray(), Y4[i].toByteArray());
        }

        e.value = new BigInteger(CryptoUtil.getHash("SHA-256", merge));
        e.value = e.value.compareTo(BigInteger.ZERO) < 0 ? e.value.negate() : e.value;

        for (int i = 0; i < this.l; i++) {
            int _mi = mi.charAt(i) - '0';
            z.value[1][i] = e.value.multiply(this.k[i]).add(ak[i]);
            z.value[2][i] = e.value.multiply(BigInteger.valueOf(_mi)).add(am[i]);
            z.value[3][i] = this.k[i].multiply(e.value.subtract(z.value[2][i])).add(a[i]);
            z.value[4][i] = e.value.multiply(this.r[i]).add(ar[i]);
        }
    }

    public boolean VEV(BigInteger e, BigInteger[][] z, Encryption[] c1c2, BigInteger[] c, BigInteger y1) {
        BigInteger[] Y1 = new BigInteger[this.l];
        BigInteger[] Y2 = new BigInteger[this.l];
        BigInteger[] Y3 = new BigInteger[this.l];
        BigInteger[] Y4 = new BigInteger[this.l];
        byte[] merge = new byte[0];

        for (int i = 0; i < this.l; i++) {
            Y1[i] = c1c2[i].getC1().modPow(e, q).modInverse(q)
                    .multiply(g1.modPow(z[1][i], q)).mod(q);

            Y2[i] = c1c2[i].getC2().modPow(e, q).modInverse(q)
                    .multiply(y1.modPow(z[1][i], q))
                    .multiply(g2.modPow(z[2][i], q)).mod(q);

            Y3[i] = c1c2[i].getC2().modPow(z[2][i], q)
                    .multiply(c1c2[i].getC2().modPow(e, q).modInverse(q))
                    .multiply(y1.modPow(z[3][i], q)).mod(q);

            Y4[i] = c[i].modPow(e, p).modInverse(p)
                    .multiply(g.modPow(z[2][i], p))
                    .multiply(h.modPow(z[4][i], p)).mod(p);

            merge = ArraysUtil.mergeByte(merge, Y1[i].toByteArray(), Y2[i].toByteArray(), Y3[i].toByteArray(), Y4[i].toByteArray());
        }

        return e.compareTo(new BigInteger(CryptoUtil.getHash("SHA-256", merge))) == 0;
    }

    public static class Encryption {
        public BigInteger c1;
        public BigInteger c2;

        public Encryption(BigInteger c1, BigInteger c2) {
            this.c1 = c1;
            this.c2 = c2;
        }

        public BigInteger getC1() {
            return c1;
        }

        public BigInteger getC2() {
            return c2;
        }

        @Override
        public String toString() {
            return "Encryption{" +
                    "c1=" + c1 +
                    ", c2=" + c2 +
                    '}';
        }
    }
}