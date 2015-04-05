# crypto
Danyhacks 2015

[![Build Status](https://travis-ci.org/benohalloran/crypto.svg?branch=Encryption)](https://travis-ci.org/benohalloran/crypto)

Images
======
Refresh icon is from [Material design icons](http://materialdesignicons.com/).

Backend is [Parse.com](parse.com)

Encryption is handled with help from [Mobistego](https://github.com/paspao/MobiStego).

Currently a techdemo of sorts.
Only can communicate with a certain number of preset pictures, but the fun part is that an arbitrary message can be encoded inside the picture that can be extracted by others with this app
We used a LSB steganography algothim to hide data, and significant work has been done to allow for RSA encrypted messages to be transmitted and decoded, but it is currently not working.
