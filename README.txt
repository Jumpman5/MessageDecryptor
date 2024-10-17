#MessageEncryption

This program takes a string from user input and either encrypts or decrypts it
based on the user's choice. The encryption algorithm is based off of the
solitaire cipher cryptographic algorithm. The algorithm uses a text file
with integers that range from 1-28. All integers in the range 1-28 must be
included in the text file and no duplicates are allowed. The integers are
followed immediately by a newline character. 27 and 28 represent the jokers. 
In order for two users to communicate to each other, both users must possess
the same text file with the same ordering of numbers in order to successfully
encrypt or decrypt the messages to each other. Changing the ordering of the
numbers will change the ciphertext.

USAGE:

java Program6 e cards.txt // this will encrypt a message

java Program6 d cards.txt // this will decrypt a message

Note: When encrypting a message, the algorithm will append extra X's if the 
user input is not a multiple of 5. This is intended as part of the algorithm.
