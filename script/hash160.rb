require 'digest' # Hash Functions Library

publickey = '1230'

binary = [publickey].pack("H*") # Convert to binary first before hashing
sha256 = Digest::SHA256.digest(binary)
ripemd160 = Digest::RMD160.digest(sha256)
hash160 = ripemd160.unpack("H*")[0] # Convert back to hex

puts hash160 # 05205208b63bb77d5dc7401d8e623a3ae4ad8f90
