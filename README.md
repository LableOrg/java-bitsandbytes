Bits and Bytes
==============

A modest collection of utility classes that make some bit and byte-wise
operations in Java a little bit more readable.

## Purpose

This tiny library can be used in projects where bit and byte manipulation is
performed to increase the readability of the code. In particular, it was
designed to supplement (and sometimes replace) Hadoop's `Bytes` class, which is
very useful, but part of a much larger library.

This tiny library has no dependencies, which makes it useful in (for example)
Hadoop's MapReduce tasks.

## Installation

This library is available on Maven Central:

```
<dependency>
    <groupId>org.lable.oss.bitsandbytes</groupId>
    <artifactId>bitsandbytes</artifactId>
    <version>2.5</version>
</dependency>
```

## Example usage

### Binary

In unit tests and debugging output the `Binary` class can aid in bit-wise operations more readable.

For example:

```java
// Instead of:
assertThat(someBitManipulatingMethodUnderTest(0),
           is(new byte[] {-1, -86, 15, 0})));

// Write:
assertThat(someBitManipulatingMethodUnderTest(0),
           is(Binary.decode("10000000 10101010 00001111 00000000")));
```

To encode a `byte[]` into a string of ones and zeroes:

```java
byte[] input = "a1".getBytes();

// Using the defaults.
//   0110000100110001
String out = Binary.encode(input);

// Or specify formatting.
//   01100001 10000100
String spaceSeparated = Binary.encode(input, false, true);

//   [01100001][10000100]
String delimited = Binary.encode(input, true, false);
```

### ByteMangler

The `ByteMangler` class contains a number of methods that aim to make working
with byte arrays a little more pleasant:

```java
// Concatenate any number of byte arrays in a single call.
byte[] cat = ByteMangler.add(part, anotherPart, andAnotherPart, yetAnotherPart);

// Shrink a byte[], discarding the rest. Output here is the same as "1234".getBytes().
byte[] fourBytes = ByteMangler.shrink("12345".getBytes(), 4);

// Remove a number of bytes from the start of a byte[]. Output here is the same as "2345".getBytes().
byte[] againFourBytes = ByteMangler.chomp("12345".getBytes(), 1);

// Flip all bits in a byte[].
byte[] normal = Binary.decode("11001111 00000001");
// Equal to Binary.decode("00110000 11111110").
byte[] flipped = ByteMangler.flip(unflipped);

// Reverse the order of bits in a byte[].
// Equal to Binary.decode("10000000 11110011").
byte[] reversed = ByteMangler.reverse(normal);
```

Analogous to String's `split` method, `ByteMangler` provides a `split` method as well:

```java
// Split a byte[] on 0x00 bytes:
byte[] input = Binary.decode("11110011 00000000 00000001 10000001");
// The output list will contain two byte[] equal to:
//   * Binary.decode("11110011")
//   * Binary.decode("00000001 10000001")
List<byte[]> parts = ByteMangler.split(input, new byte[]{0x00});
```

And a `replace` method:

```java
// Replace all occurrences of 0xFF with 0x00 0x00:
byte[] input = Binary.decode("11110011 11111111 00000000 00000001 11111111 10000001");

// 11110011 00000000 00000000 00000000 00000001 00000000 00000000 10000001.
byte[] output = ByteMangler.replace(input, Binary.decode("11111111"), new byte[]{0, 0});


```

### BitMask

HBase's
[FuzzyRowFilter](https://hbase.apache.org/apidocs/org/apache/hadoop/hbase/filter/FuzzyRowFilter.html)
expects a bitmask in the form of a byte array containing `0x00` or `0x01` for
each byte as one of its inputs. `BitMask` has a method that can compose this
byte array mask by specifying the consecutive number of ones and zeroes you
need:
 
```java
// 0011101111:
byte[] mask = BitMask.bitMask(2, 3, 1, 4);
 
// To start the mask with ones, pass 0 as the first argument.
// 11111111:
byte[] anotherMask = BitMask.bitMask(0, 8);
```

### BytePrinter

When you end up with `byte[]` that contain printable UTF-8 encoded text as well as 
some unprintable control characters (e.g., `null` byte separators), logging these
for debugging can be cumbersome. The `BytePrinter` class can help in these cases, 
by creating a string where valid UTF-8 sequences are left as-is, and unprintable
characters are replaced by printable escape sequences:
 
```java
byte[] unprintable = ByteMangler.add("abc".getBytes(), new byte[]{0x00}, "def".getBytes());
// "abc\x00def"
String printable = BytePrinter.utf8Escaped(unprintable);
```

### ByteConversion

Convert Java primitives to and from byte arrays.

```java
// Strings are encoded as UTF-8; 0xE2 0x82 0xAC:
byte[] result = ByteConversion.fromString("€");
String euro = ByteConversion.toString(result);
```

Primitives such as ints are converted to their conventional Java byte
representation. This usually means [two's
complement](https://en.wikipedia.org/wiki/Two's_complement).

```java
// 0x7F 0xFF 0xFF 0xFF:
byte[] result = ByteConversion.fromInt(Integer.MAX_VALUE);
```

In cases where you want the byte array of an int or long to be naturally
sortable (which is often what you want when they are used as part of a HBase
row-key), *two's complement* causes negative numbers to be sorted *after*
positive numbers.

To prevent this, use `ByteMangler` to flip the first bit when you read and 
write the byte representation of an `int` or `long`:

```java
// 0x7F 0xFF 0xFF 0xFF:
byte[] resultMinusOne = ByteMangler.flipTheFirstBit(ByteConversion.fromInt(-1));
int minusOne = ByteConversion.toInt(ByteMangler.flipTheFirstBit(resultMinusOne));
// 0x80 0x00 0x00 0x00:
byte[] resultZero = ByteMangler.flipTheFirstBit(ByteConversion.fromInt(0));
int zero = ByteConversion.toInt(ByteMangler.flipTheFirstBit(resultZero));
// 0x80 0x00 0x00 0x01:
byte[] resultOne = ByteMangler.flipTheFirstBit(ByteConversion.fromInt(1));
int one = ByteConversion.toInt(ByteMangler.flipTheFirstBit(resultOne));
```

Now `-1` sorts right before `0`.

Or, specify the `LEXICOGRAPHIC_SORT` *NumberRepresentation* parameter for the same effect:

```java
// 0x7F 0xFF 0xFF 0xFF:
byte[] resultMinusOne = ByteConversion.fromInt(-1, NumberRepresentation.LEXICOGRAPHIC_SORT);
int minusOne = ByteConversion.toInt(resultMinusOne, NumberRepresentation.LEXICOGRAPHIC_SORT);
```
