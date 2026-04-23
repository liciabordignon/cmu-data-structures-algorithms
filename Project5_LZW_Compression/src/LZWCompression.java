// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 5

/*
The following program implements 12-bit LZW compression and decompression.

ASCII files represent human-readable characters and typically use values in the range 0–127,
while binary files represent raw data and can use the full range of byte values from 0 to 255.
This program reads all files as raw bytes using DataInputStream, thus working identically for both ASCII and binary files.
However, in Java, the byte type is signed and ranges from -128 to 127.
Therefore, it is extremely important to read bytes using the extension readUnsignedByte().
While working only with ASCII files would likely not cause issues (since most values remain below 128), binary files frequently use values greater than 127.
These values would be interpreted as negative numbers if read incorrectly, leading to errors in bit manipulation.
For example, let's take a byte with value 200 (11001000 in binary)
- int x = in.readUnsignedByte(); -> 00000000 00000000 00000000 11001000 -> value = 200 (correct)
- int y = in.readByte(); -> 11111111 11111111 11111111 11001000 -> value = -56 (incorrect)
Now, in the code, if we try to use readByte and do a shift operation such as y << 4 we obtain 11111111 11111111 11001000 0000 insted of 00000000 00000000 11001000 0000 leading to errors that would break the compression/decompression process.
Using readUnsignedByte() avoids this problem by correctly interpreting all byte values in the range 0–255, ensuring proper handling of both ASCII and, most importantlyb, binary data.

Another alternative, would have been to use
byte b = in.readByte();
int x = b & 0xFF;
Indeed, the operation would remove the sign, restoring the correct unsigned value (200):
  11111111 11111111 11111111 11001000
& 00000000 00000000 00000000 11111111
--------------------------------------
00000000 00000000 00000000 11001000



Degree of compression obtained:
shortwords.txt:
bytes read = 50 bytes
bytes written = 56 bytes

Degree of compression obtained:
words.html:
bytes read = 2,493,531 bytes
bytes written = 1,069,850 bytes

CrimeLatLonXY1990.csv:
bytes read = 2,608,940 bytes
bytes written = 1,284,017 bytes

01_Overview.mp4:
bytes read = 25,008,838 bytes
bytes written = 33,769,127 bytes

*/
import java.io.*;

public class LZWCompression {

    public static void main(String[] args) throws IOException {
        boolean verbose = false; // v
        String mode; // c or d
        String inputFile;
        String outputFile;

        if (args.length == 3) {
            mode = args[0];
            inputFile = args[1];
            outputFile = args[2];
        } else if (args.length == 4 && args[1].equals("-v")) {
            mode = args[0];
            verbose = true;
            inputFile = args[2];
            outputFile = args[3];
        } else {
            System.out.println("Wrong input");
            return;
        }

        DataInputStream in =
                new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(inputFile)));

        DataOutputStream out =
                new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(outputFile)));


        if (mode.equals("-c")) {
            compress(in, out); // in this case we compress
        } else if (mode.equals("-d")) {
            decompress(in, out); // in this we decompress
        } else {
            System.out.println("First argument must be -c or -d");
            return;
        }

        out.close();
        in.close();


        if (verbose) {
            File inFile = new File(inputFile);
            File outFile = new File(outputFile);
            System.out.println("bytes read = " + inFile.length() + ", bytes written = " + outFile.length());
        }
    }


    private static void compress(DataInputStream in, DataOutputStream out) throws IOException {
        // we first enter all symbols in the table (all single-byte values 0-255)
        HashTable table = new HashTable(); // create table
        for (int i = 0; i < 256; i++) {
            table.put("" + (char) i, i); // the put method of the hash table inserts a key, value pair in the table
        }
        int nextCode = 256; //next code for new patterns (max codes = 4096 = 2^12)

        int waiting = -1; // This will store one 12-bit code until we get a second one so that we then have two 12-bit codes = 24 bits = 8 * 3 (we can only write 8 bits codes)

        // Read first byte into string s
        int first;
        try {
            first = in.readUnsignedByte(); // source: https://www.geeksforgeeks.org/java/datainputstream-readunsignedbyte-method-in-java-with-examples/
            // we use readUnsignedByte to avoid any sign extension
        } catch (EOFException e) {
            out.flush();
            return;
        }
        String s = "" + (char) first; //current string

        // while(any input left){read(character c);
        while (true) {
            int next;
            try {
                next = in.readUnsignedByte();
            } catch (EOFException e) {
                break;
            }
            char c = (char) next;

            // If s+c is already in the table, s= s+c
            if (table.get(s + c) != -1) {
                s = s + c;

            } else {
                /// /////////////////////
                // 1. output codeword(s);
                /// /////////////////////

                // Get the code for the current string s
                int code = table.get(s) & 0x0FFF; //using the mask we keep only 12 bits

                /// //// example: s= 'A'
                // - ASCII: A = 65
                // - binary: A = 01000001 (8 bits)
                // int code = table.get(s); -> code becomes automatically a 32-bit Java int -> 00000000 00000000 00000000 01000001   (32 bits)
                // using the mask we keep only 12 bits -> code & 0x0FFF; -> 0000 0100 0001

                // The above outputs a 12-bit code, but files are written in 8-bit bytes and therefore we can't write a single 12-bit value directly.
                // Instead, we pack two 12-bit chunks in 24 bits, or 3 bytes

                // If we don't already have a stored code waiting, we just save this one and wait for the next code
                if (waiting == -1) {
                    waiting = code;
                } else {
                    // Now we have two 12-bit codes:
                    int firstCode = waiting; // - firstCode (the one we saved earlier)
                    int secondCode = code; // - secondCode (the one we just computed)

                    // We now split these two 12-bit numbers into 3 bytes (8 bits each) -> same logic as badcompressor
                    int b1 = (firstCode >> 4) & 0xFF; //first 8 bits of firstCode
                    int b2 = ((firstCode & 0x0F) << 4) | ((secondCode >> 8) & 0x0F); //last 4 bits of firstCode (then shifted left) + first 4 bits of secondCode
                    int b3 = secondCode & 0xFF; // last 8 bits of secondCode

                    // Write the 3 bytes to the output file
                    out.writeByte(b1);
                    out.writeByte(b2);
                    out.writeByte(b3);

                    waiting = -1;
                }

                /// /////////////////////
                // 2. Enter s + c into the table;
                /// /////////////////////

                // We enter s+c in the table if it still has space (2^12 = 4096 = possible values of 12-bit codes)
                if (nextCode < 4096) {
                    table.put(s + c, nextCode++);
                } else {
                    // If full, we generate a brand new table and begin processing anew
                    table = new HashTable();
                    for (int i = 0; i < 256; i++) {
                        table.put("" + (char) i, i);
                    }
                    nextCode = 256;
                }

                /// /////////////////////
                // 3. s = c;
                /// /////////////////////
                s = "" + c; // Start new string from c -> s = c
            }
        }

        // Output code for the last string (output codeword(s);)
        int lastCode = table.get(s) & 0x0FFF;

        if (waiting == -1) {
            waiting = lastCode; // no pending code, we just store this one for now
        } else { // else same logic as above
            int firstCode = waiting;
            int secondCode = lastCode;

            int b1 = (firstCode >> 4) & 0xFF;
            int b2 = ((firstCode & 0x0F) << 4) | ((secondCode >> 8) & 0x0F);
            int b3 = secondCode & 0xFF;

            out.writeByte(b1);
            out.writeByte(b2);
            out.writeByte(b3);

            waiting = -1;
        }

        // In the end, there might still be ONE leftover 12-bit code (this happens if the total number of codes is odd)
        if (waiting != -1) {
            // We only have one 12-bit code left, so we can't form a full 24-bit block
            int b1 = (waiting >> 4) & 0xFF; // b1= first 8 bits of lastCode (12bits)
            int b2 = (waiting & 0x0F) << 4; // b2= last 4 bits of the code. We then shift them left automatically padding 0000 to the right

            // Write only 2 bytes (16 bits total) where only the first 12 bits have value, the last 4 bits are padding
            out.writeByte(b1);
            out.writeByte(b2);
        }
    }

    private static void decompress(DataInputStream in, DataOutputStream out) throws IOException {
        /// /////////////////////////////////////////////////////////
        // 1. we first enter all symbols in the table (all single-byte values 0-255)
        /// /////////////////////////////////////////////////////////
        String[] table = new String[4096]; // table that maps 12-bit codes → strings

        for (int i = 0; i < 256; i++) {
            table[i] = "" + (char) i;
        }
        int nextCode = 256; //next code for new patterns (max codes = 4096 = 2^12)

        // We will use these to read 12-bit chunks from a byte stream
        boolean halfUsed = false;      // tracks whether we already used half of a 3-byte block
        int savedPart = 0;        // stores 4 leftover bits from previous read

        /// /////////////////////////////////////////////////////////
        // 2. read(priorcodeword) and output its corresponding character;
        /// /////////////////////////////////////////////////////////
        int priorCode;
        try {
            int b1 = in.readUnsignedByte();
            int b2 = in.readUnsignedByte();

            // To form the first 12-bit code, we need to combine 2 bytes. TO do so, we take all 8 bits of b1 and the left 4 bits of b2
            priorCode = ((b1 << 4) | (b2 >> 4)) & 0x0FFF;

            savedPart = b2 & 0x0F; // Save the right 4 bits of b2 for the next code
            halfUsed = true; // We have used only part of the 3-byte block

        } catch (EOFException e) {
            return;// nothing left to decompress
        }

        String priorString = table[priorCode]; // Convert priorcode into its corresponding string
        writeString(out, priorString); //and output it

        /// /////////////////////////////////////////////////////////
        // 3. while(codewords are still left to be input) read codeword
        /// /////////////////////////////////////////////////////////
        while (true) {
            int code;

            try {
                if (!halfUsed) {
                    // in this case we don't have anything stored, so we read a fresh 12-bit code from two new bytes (same as before)
                    int b1 = in.readUnsignedByte();
                    int b2 = in.readUnsignedByte();
                    code = ((b1 << 4) | (b2 >> 4)) & 0x0FFF; //exactly as above

                    // Save remaining 4 bits for next time
                    savedPart = b2 & 0x0F;
                    halfUsed = true;

                } else {
                    // Otherwise, we need to use the leftover 4 bits + next byte to form a 12-bit code
                    int b3 = in.readUnsignedByte();

                    // Combine saved 4 bits (putting them to the left) + all 8 bits of b3 (to the right)
                    code = ((savedPart << 8) | b3) & 0x0FFF;

                    // halfUsed returns false
                    halfUsed = false;
                }
            } catch (EOFException e) {
                // No more data, we stop decompression
                break;
            }

            String entry;

            /// /////////////////////////////////////////////////////////
            // if(codeword not in the table)
            /// /////////////////////////////////////////////////////////
            if (table[code] == null) {
                entry = priorString + priorString.charAt(0);
                if (nextCode < 4096) {
                    //enter string(priorcodeword) + firstChar(string(priorcodeword)) into the table;
                    table[nextCode++] = entry;
                } else { //we used all 12-bit codes: 0–4095 (2^12)
                    // Reset table when full (this matches compression behavior)
                    table = new String[4096];
                    for (int i = 0; i < 256; i++) {
                        table[i] = "" + (char) i;
                    }
                    nextCode = 256;
                    // I choose not insert (s + c) in this step.
                    // This matches compression behavior and keeps both sides synchronized.
                }
                // output string(priorcodeword) + firstChar(string(priorcodeword));
                writeString(out, entry);

            /// /////////////////////////////////////////////////////////
            // else-> codeword in the table
            /// /////////////////////////////////////////////////////////
            } else {
                entry = table[code];
                if (nextCode < 4096) {
                    // enter string(priorcodeword) + firstChar(string(codeword)) into the table;
                    table[nextCode++] = priorString + entry.charAt(0);
                } else { //we used all 12-bit codes: 0–4095 (2^12)
                    // Reset table when full (this matches compression behavior)
                    table = new String[4096];
                    for (int i = 0; i < 256; i++) {
                        table[i] = "" + (char) i;
                    }
                    nextCode = 256;
                    // I choose not insert (s + c) in this step.
                    // This matches compression behavior and keeps both sides synchronized.
                }
                // output string(codeword);
                writeString(out, entry);

            }

            // priorcodeword = codeword;
            priorString = entry;
        }
    }

    private static void writeString(DataOutputStream out, String s) throws IOException {
        // this is a helper to write each character of the string as a single byte
        for (int i = 0; i < s.length(); i++) {
            out.writeByte((byte) (s.charAt(i) & 0xFF)); // Mask with 0xFF to be sure to get just the last 8 bits

        }
    }
}