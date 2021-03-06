require 'digest' # Hash Functions Library
require 'base58'

publickey = '010000000135bd668f37633a5e56b59fbc3a16387969eac2e5b1863a24bfc5305346475cbc0000000017a914ebcd71b02690345813f6db469a2436c672961e8387fdffffff01401f000000000000160014bfc9911b767254d537004192178f4e96e749a33c38961d00'

binary = [publickey].pack("H*") # Convert to binary first before hashing
sha256 = Digest::SHA256.digest(binary)
sha256sha256 = Digest::SHA256.digest(sha256).unpack("H*")[0]

puts sha256sha256
