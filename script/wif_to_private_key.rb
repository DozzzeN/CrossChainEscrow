require 'digest' 
require 'base58'

# Convert Private Key to WIF
wif = "cNGWirkSagQexhXMYdFpsdWAy1xuvrYzcgDCkerenet7Rr9aeMTF"
extendedchecksum = Base58.base58_to_binary(wif, :bitcoin).unpack("H*")[0]
puts extendedchecksum # mainnet and testnet are different
privatekey = extendedchecksum[2...66]
puts privatekey