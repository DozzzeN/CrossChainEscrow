pragma solidity >=0.7.0 <0.8.0;

contract contract {
    int public g;
    int public h;
    int public rB;
    int public cB;
    int public p;
    uint public pB;
    
    uint tB;
    
    address payable public A = 0x5B38Da6a701c568545dCfcB03FcB875f56beddC4;
    address payable public B = 0xAb8483F64d9C6d1EcF9b849Ae677dD3315835cb2;
    
    constructor (uint _tB) payable {
        g = 62419540614873575006307526028117976282812114065175462432164;
        h = 22255879570024484377368052409548790117240433143752288480625;
        rB = 2109;
        p = 508720243370092094461945475254190912747;
        cB = 415022213053624546523813780278162293523;
        pB = msg.value;
        tB = _tB;
    }
    
    
    function unlock(int xB) public {
        if (msg.sender == A) {
            int _cB = pow(g, xB, p);
            _cB = (_cB * pow (h, rB, p)) % p;
            if (_cB == cB) {
                A.transfer(pB);    
            }   
        }
        if (msg.sender == B) {
            if (block.number > tB) {
                B.transfer(pB);
            }
        }
    }
    
    
    function pow(int a, int b, int n) public pure returns (int) {
        int c = 1;
        int t = a % n;
        while (b != 0) {
            if (b & 1 == 1) {
                c = (c * t) % n;
            }
            b >>= 1;
            t = (t * t) % n;
        }
        return c;
    }
}
