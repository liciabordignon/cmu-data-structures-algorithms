// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 1 - Part 2

import edu.cmu.andrew.lbordign.SinglyLinkedList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;


public class MerkleHellman {

    private static final int MAX_CHARS = 80;
    private static final int N = MAX_CHARS * 8; // 640 bits
    private static final SecureRandom RNG = new SecureRandom();

    static class KeyPair {
        SinglyLinkedList w;  // private superincreasing
        SinglyLinkedList b;  // public key
        BigInteger q;        // modulus
        BigInteger r;        // multiplier (invertible mod q)
    }

    // Key generation
    // - w[i] = 7^i
    // - q > sum(w)
    // - b[i] = (r*w[i]) mod q
    // Postcondition: returns (w,b,q,r) with q > sum(w) and gcd(r,q)=1.
    static KeyPair generateKeys() {
        KeyPair kp = new KeyPair();
        kp.w = new SinglyLinkedList(); //superincreasing sequence of integers
        kp.b = new SinglyLinkedList();

        // Building w with 640 nodes: 7^0, 7^1, ..., 7^639
        // Note: key size does not depend on the size of the input
        for (int i = 0; i < N; i++) {
            kp.w.addAtEndNode(BigInteger.valueOf(7).pow(i));
        }

        // sumW
        BigInteger sumW = BigInteger.ZERO;
        kp.w.reset();
        while (kp.w.hasNext()) {
            BigInteger wi = (BigInteger) kp.w.next();
            sumW = sumW.add(wi);
        }

        // q > sumW
        kp.q = sumW.add(BigInteger.ONE); // q = sumW + 1

        // choose r with  1 < r < q and gcd(r,q)=1
        while (true) {
            BigInteger cand = new BigInteger(kp.q.bitLength(), RNG);
            cand = cand.mod(kp.q.subtract(BigInteger.TWO)).add(BigInteger.TWO);
            if (cand.gcd(kp.q).equals(BigInteger.ONE)) {
                kp.r = cand;
                break;
            }
        }

        // Build public key list b
        kp.w.reset();
        while (kp.w.hasNext()) {
            BigInteger wi = (BigInteger) kp.w.next();
            BigInteger bi = kp.r.multiply(wi).mod(kp.q);
            kp.b.addAtEndNode(bi);
        }

        return kp;
    }

    // Changing plaintext into bits (640)
    // Uses UTF-8 bytes; pads with zeros up to 640 bits.
    // Precondition: s.length() <= 80.
    // Postcondition: returns an int[640] containing the UTF-8 bits of s padded with zeros.
    static int[] textToBits(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

        int[] bits = new int[N];
        int pos = 0;

        for (byte by : bytes) {
            for (int k = 7; k >= 0; k--) {
                bits[pos++] = (by >> k) & 1;
            }
        }
        while (pos < N) bits[pos++] = 0;
        return bits;
    }

    // Changing bits into plaintext by also trimming trailing 0 bytes (padding).
    static String bitsToText(int[] bits) {
        int byteCount = bits.length / 8; // 80
        byte[] out = new byte[byteCount];

        for (int i = 0; i < byteCount; i++) {
            int v = 0;
            for (int k = 0; k < 8; k++) {
                v = (v << 1) | bits[i * 8 + k];
            }
            out[i] = (byte) v;
        }

        int end = out.length;
        while (end > 0 && out[end - 1] == 0) end--;

        return new String(out, 0, end, StandardCharsets.UTF_8);
    }

    // Encryption
    // C = sum(b[i] * x[i]) with x[i] in {0,1}. It is essentially the sum of b[i] where bit is 1.
    // Precondition: b has exactly 640 elements
    // Postcondition: returns ciphertext C
    static BigInteger encrypt(SinglyLinkedList b, int[] bits) {
        BigInteger C = BigInteger.ZERO;

        b.reset();
        for (int i = 0; i < bits.length; i++) {
            BigInteger bi = (BigInteger) b.next();
            if (bits[i] == 1) {
                C = C.add(bi);
            }
        }
        return C;
    }

    // Decryption
    // I compute the modular inverse rInv = r^{-1} (mod q) that is r * rInv ≡ 1 (mod q).
    //
     // Given:
    //   C  = Σ (b[i] * x[i])                 // encrypted value
    //   b[i] = (r * w[i]) mod q              // public key
    // Then:
    //   C ≡ r * Σ (w[i] * x[i]) (mod q)
    //
    // Multiplying by rInv modulo q cancels r:
    //   C' = (C * rInv) mod q
    //      ≡ Σ (w[i] * x[i]) (mod q)
    //
    // Since q > Σ w[i], the modulo operation does not change the value:
    //   C' = Σ (w[i] * x[i])
    //
    // Because w is a superincreasing sequence, the bits x[i] can be recovered using a greedy algorithm
    // = starting from the largest w[i] and proceeding backwards.

    // Preconditions: w has exactly 640 elements and is superincreasing, q > sum(w), gcd(r,q)=1 (so rInv exists)
    // Postcondition: returns the recovered bit array of length 640.
    // Runtime:Big Theta(N) to copy w + Big Theta(N) greedy recovery (overall Big Theta(N)).
    static int[] decrypt(SinglyLinkedList w, BigInteger q, BigInteger r, BigInteger C) {
        BigInteger rInv = r.modInverse(q); //rInv = r^{-1} (mod q)
        BigInteger Cprime = C.multiply(rInv).mod(q); //C' = (C * rInv) mod q

        // Copy w into an array so we can traverse backwards
        //Since the list is singly linked, it does not support backward traversal. I copy the elements into an array to allow reverse access in O(1) per element, enabling the greedy algorithm to run in linear time.
        int n = w.countNodes();
        BigInteger[] warr = new BigInteger[n];

        int idx = 0;
        w.reset();
        while (w.hasNext()) {
            warr[idx++] = (BigInteger) w.next();
        }

        int[] bits = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            if (Cprime.compareTo(warr[i]) >= 0) {
                bits[i] = 1;
                Cprime = Cprime.subtract(warr[i]);
            } else {
                bits[i] = 0;
            }
        }
        return bits;
    }

    static void printBigInteger(BigInteger x, int chunkSize) {
        String s = x.toString();
        for (int i = 0; i < s.length(); i += chunkSize) {
            int end = Math.min(i + chunkSize, s.length());
            System.out.println(s.substring(i, end));
        }
    }

    // Main
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String clear;

        while (true) {
            System.out.println("Enter a string and I will encrypt it as single large integer.");
            clear = br.readLine();
            if (clear == null) return;
            //string entered is too long. The user is prompted to try again.
            if (clear.length() > MAX_CHARS) {
                System.out.println("The string entered is too long (max 80 characters). Try again.");
            } else {
                break;
            }
        }

        System.out.println("Clear text:");
        System.out.println(clear);
        System.out.println("Number of clear text bytes = " + clear.getBytes(StandardCharsets.UTF_8).length);

        // Key generation
        KeyPair kp = generateKeys();

        // Encrypt
        int[] bits = textToBits(clear);
        BigInteger C = encrypt(kp.b, bits);

        System.out.println(clear + " is encrypted as ");
        printBigInteger(C, 60);

        // Decrypt
        int[] recoveredBits = decrypt(kp.w, kp.q, kp.r, C);
        String recovered = bitsToText(recoveredBits);

        System.out.println("Result of decryption: " + recovered);
    }
}

// Note: Portions of this code were developed with the assistance of ChatGPT
// The final implementation, structure, and comments are my own.