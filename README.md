# Save Network lib

> This library undergoes a rework in the moment.
> * Unit tests -> CI will be added soon
> * Conventional Commits
> * Bugfixes
> * Documentation
> Therefore i don't recommend using any version bevore `2.0.0rc0` is released

## Description
Save Network is an high level Java API
It provides everything you need to create Java applications which are interacting with Networks.
It can be used to easily build Server-Client applications.

#### Pros

+ Many Features
+ Very easy to use - Fast to use
+ Moderate to low traffic due binary protocoll

#### Cons

- Not professional coded (spare time/hobby project)
- Overhead compared to plain/native binary communication
  - (More traffic)
- Not performance optimized
- *NOT* compatible with other languages than java (Clients need java)

> This lib is focused on high level package communication. While you can use low level/native sockets there is a separate lib called `JNet` Which is focused on managed low level / binary sockets 
## Overview
 1. Functions
 2. Where to use
 3. Examples
 4. Rights and Usage
## Funktionen
 1. Cryptography
  2. Server Client Modell

### Cryptography
the package `com.niton.tele.crypto` is responsable for all Cryptographic tasks.
The Package consists of two parts.
The first part are the standardized Encryption Methods **RSA** and **AES**.  The classes
for them are called `SimpleAES` and `SimpleRSA`which contain hight level en/decrypt
methods.
The second part is my custom Encryption called **CLUSTER**. The technice is a bit like a
rubic-cubes, but you can see the source. The advantage of Clustering is that the key has **only
one rule**: it needs at least 1  Byte (key and data) and should / can not exceed the size of `Integer.MAX_VALUE`.
Obviously the top Level class is called `SimpleCluster`

> **The use of the encryption (CLUSTER) is at your own risk! 
> *I am NOT responsible for any loss of data or a loss of security***

But I can tell you they are all well tested. For RSA and AES I use the default JavaSE API which is
incredible difficult!
All the methods in the class are well documented or self describing.

### Server Client Modell
The Server Client model is the most important part of the API. It consist of many and big classes.
There are 3 Ways to use the given structure. The package is `com.niton.net.pack` and the
most interesting classes there are `client.NetworkClient` and `server.Server`

1. Here you have a very very high amount of good services automations and other cool stuff.
 2. You can use this very easy as its very beautiful designed 
 3. Some cool features
	 1. Automated Session Handling (counting traffic -> send/received bytes)
	 2. Automated (controllable) Encryption
	 3. Automated Multithreading 
	 4. ping
	 5. and much more
 4. Additional you can directly use plain Sockets if you need to
      1. This feature is also Thread save

## Where to use
You can use this API great for :
 - Data providing Services
 - Live Data transmission
 - Online Based Games
 - Chat Services
 - Custom Servers
 - Something like Samba (sharing files/dirs in local networks)
## Examples
Cryptography:
 - RSA or AES en/decrypt a byte array 

```java
byte[] dataToEncrypt = "Some ****** very ****** bad text nobody is allowed to see".getBytes("UTF-8");
SecretKey key        = SimpleAES.generateKey(128);
byte[] encryptedData = SimpleAES.encrypt(key, dataToEncrypt);
//Some Time in between
byte[] decryptedData = SimpleAES.decrypt(key, encryptedData);`
```
 - Cluster en/decrypt a byte array 

```java
byte[] dataToEncrypt = "Some ****** very ****** bad text nobody is allowed to see".getBytes("UTF-8");
byte[] key           = SimpleAES.generateRandom(1024);
byte[] encryptedData = SimpleCluster.encrypt(key, dataToEncrypt);
//Some Time in between
byte[] decryptedData = SimpleCluster.decrypt(key, encryptedData);`
```


 - Encrypt an `Serializeable` Object

```java
Rectangle secretRectangle = new Rectangle(123, 448, 625, 326);
SecretKey key = SimpleAES.generateKey(128);
SealedObject encrypted = SimpleAES.encryptObject(secretRectangle, key);
//You can do whatever you want here. I save it into an File
File f = new File("C:/myfile.obj.enc");
FileOutputStream fos = new FileOutputStream(f);
ObjectOutputStream oos = new ObjectOutputStream(fos);
oos.writeObject(encrypted);
```

More examples are found in the `examples` package
