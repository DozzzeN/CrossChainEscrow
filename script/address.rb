require 'bitcoin'
 
Bitcoin::network = :testnet #使用测试网
pri_key, pub_key = Bitcoin.generate_key
address = Bitcoin::pubkey_to_address(pub_key)
puts pri_key
puts pub_key
puts address