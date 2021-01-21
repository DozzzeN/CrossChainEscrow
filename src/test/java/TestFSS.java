import it.unisa.dia.gas.jpbc.Element;
import org.junit.Test;

public class TestFSS {
    @Test
    public void testAll() {
        FairSecretSharing fss = new FairSecretSharing();
        Element[][] f = fss.chooseFx(7, 3, 5);
        System.out.println("s:" + fss.recover(f, 7, 3));
    }
}
