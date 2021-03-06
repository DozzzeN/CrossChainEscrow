require 'ecdsa'
require 'securerandom'
require 'digest/sha2'

group = ECDSA::Group::Secp256k1
# private_key = 1 + SecureRandom.random_number(group.order - 1)
# puts 'private key: %#x' % private_key 
private_key = 0x147b08e7b1cbc4c3201022acea92956c2783fc6da7256511393b3a0b1fc070a3

public_key = group.generator.multiply_by_scalar(private_key)
puts 'public key: '
puts '  x: %#x' % public_key.x
puts '  y: %#x' % public_key.y

#04 2daa93315eebbe2cb9b5c3505df4c6fb6caca8b756786098567550d4820c09db988fe9997d049d687292f815ccd6e7fb5c1b1a91137999818d17c73d0f80aef9
#2daa93315eebbe2cb9b5c3505df4c6fb6caca8b756786098567550d4820c09db 988fe9997d049d687292f815ccd6e7fb5c1b1a91137999818d17c73d0f80aef9

digest = 0x2f7c73271eca825ee0b34369041180566d8ba4a06dad7d7a6c6673ad0d2925da

signature = nil
while signature.nil?
  temp_key = 1 + SecureRandom.random_number(group.order - 1)
  signature = ECDSA.sign(group, private_key, digest, temp_key)
end
puts 'signature: '
puts '  r: %#x' % signature.r
puts '  s: %#x' % signature.s

signature_der_string = ECDSA::Format::SignatureDerString.encode(signature).unpack("H*")[0]
puts signature_der_string
# 3046022100cf4d7571dd47a4d47f5cb767d54d6702530a3555726b27b6ac56117f5e7808fe0221008cbb42233bb04d7f28a715cf7c938e238afde90207e9d103dd9018e12cb7180e 

signature_der_string = [signature_der_string].pack("H*")
signature = ECDSA::Format::SignatureDerString.decode(signature_der_string)

valid = ECDSA.valid_signature?(public_key, digest, signature)
puts "valid: #{valid}"

  