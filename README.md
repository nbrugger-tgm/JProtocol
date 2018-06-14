# Save Network

## Description
Save Network is an **high level Java API**
It provides everything you need to create java Applicationes which are interacting with Networks.
It has many and hight implemented Classes to build Server-Client Applicationes.

## Overview

 1. Functions
 2. Where to use
 3. Examples
 4. Rights and Usage
## Funktionen
 1. Cryptography
 2. Server Client Modell
	 1. Basic Use
	 2. Package Use
	 3. Full Stack Use

### Cryptography
the package `com.niton.tele.crypto` is responsable for all Cryptographic tasks.
The Package consists of two parts.
The first part are the standardized Encryption Methods **RSA** and **AES**.  The classes
for them are called `SimpleAES` and `SimpleRSA`which contain hight level en/decrypt
methods.
The seccond part is my custom Encryption called **CLUSTER**. The technice is a bit like a
rubics Cube, but you can see the source. The advantage of Clustering is that the key has **only
one rule**: it needs at least 1  Byte and should / can not exceed the size of `Integer.MAX_VALUE`.
Obviously the top Level class is called `SimpleCluster`

> **The use of the encryptions is at your own risk! 
> *I am NOT responsible for any loss of data or a loss of security***

But i can tell you they are all well tested. For RSA and AES I use the default JavaSE API which is
incredible difficult! So if RSA or AES has an error its not my fault.
All the methods in the class are well documented or self describing.
