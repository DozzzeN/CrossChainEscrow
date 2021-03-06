require 'digest' # Hash Functions Library
require 'base58'

publickey = '630338961db1751600146e14ca4aaba8d4f8911897d426bba1c774074900ad67160014bfc9911b767254d537004192178f4e96e749a33cada91405205208b63bb77d5dc7401d8e623a3ae4ad8f908868'

binary = [publickey].pack("H*") # Convert to binary first before hashing
sha256 = Digest::SHA256.digest(binary)
ripemd160 = Digest::RMD160.digest(sha256)
hash160 = ripemd160.unpack("H*")[0] # Convert back to hex

#hash160 = '05' + hash160 # mainnet p2sh address
hash160 = 'c4' + hash160 # testnet p2sh address
puts 'hash160 ' + hash160
payload = [hash160].pack("H*")

sha256 = Digest::SHA256.digest(payload)
sha256sha256 = Digest::SHA256.digest(sha256).unpack("H*")[0]
puts 'sha256sha256 ' + sha256sha256

suffix = sha256sha256[0,8]
puts 'suffix ' + suffix

data = hash160 + suffix
data = data.upcase 
puts 'data ' + data

puts 'address ' + Base58.binary_to_base58([data].pack('H*'), :bitcoin, true)