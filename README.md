# This API is not stable at the moment!!!
## if you notice issues you need to fix them yourself. The majority of the features work but i am aware of same bugs. The encryption is working fine only the network components are not stable. They are useable but some special cases are not covered
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
### Server Client Modell
The Server Client modell is the most important part of the API. It consist of many and big classes.
There are 3 Ways to use the given structure. The package is `com.niton.tele.network` and the
most interesting classes there are `client.NetworkClient` and `server.Server`

 - Basic Use
	 1. Basic uses only a minimal part of the API and **only use one Stream**/Socket. It is for sending data over an single Stream for only one time. Its not recommended as it doesn't supports encryption multithreading or data handling and connection seccurity
	 2. Examples are at the end of the document 
 - Package Use
	 1. Package Use allows you additional to the Basic use to pack the data to send in packages.
	 2. Because of this you have the advantage that you are able to **easy send full Objects** and big data constructs, the disadvantage you have in comparison to the Basic Use is that you are not able to stream the content so its not recommended for Big downloads as long as you dont write your own PackageSplitter
	 3. Encrypting also doesnt works here
 - Full stack
	 1. Here you have a very very hight amount of good services automisations and other cool stuff.
	 2. You can use this very easy as its very beautiful designed 
	 3. Some cool features
		 1. Automated Session Handling
		 2. Automated (controllable) Encryption
		 3. Automated Multitherading
		 4. ping
		 5. and much more
	 4. Additional we can use the Streaming feature from the Simple Use
## Where to use
You can use this API great for :
 - Data providing Services
 - Making an API for your Web Service
 - Live Data transmission
 - Online Based Games
 - Chat Services
 - Custom Servers
 - Something like Samba (sharing files/dirs in local networks) 
## Examples
Cryptography:
 - RSA or AES en/decrypt a byte array 

    `byte[] dataToEncrypt = "Some ****** very ****** bad text nobody is allowed to see".getBytes("UTF-8");
    SecretKey key        = SimpleAES.generateKey(128);
    byte[] encryptedData = SimpleAES.encrypt(key, dataToEncrypt);
    //Some Time in between
    byte[] decryptedData = SimpleAES.decrypt(key, encryptedData);`

 - Cluster en/decrypt a byte array 

    `byte[] dataToEncrypt = "Some ****** very ****** bad text nobody is allowed to see".getBytes("UTF-8");
    byte[] key           = SimpleAES.generateRandom(1024);
    byte[] encryptedData = SimpleCluster.encrypt(key, dataToEncrypt);
    //Some Time in between
    byte[] decryptedData = SimpleCluster.decrypt(key, encryptedData);`
    

 - Encrypt an `Serializeable` Object

    `Rectangle secretRectangle = new Rectangle(123, 448, 625, 326);
		SecretKey key = SimpleAES.generateKey(128);
		SealedObject encrypted = SimpleAES.encryptObject(secretRectangle, key);
		//You can do whatever you want here. I save it into an File
		File f = new File("C:/myfile.obj.enc");
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(encrypted);`

 


