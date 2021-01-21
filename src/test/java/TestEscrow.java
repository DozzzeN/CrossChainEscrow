import org.junit.Test;

public class TestEscrow {
    @Test
    public void test() {
        Escrow e = new Escrow();

        e.SetUp();
        e.LockA();
        e.LockB();
        e.UnlockA();
        e.UnlockB();

//        e.ArbitrateA(Escrow.c1c2B);
//        e.ArbitrateB(Escrow.c1c2A);
    }
}
