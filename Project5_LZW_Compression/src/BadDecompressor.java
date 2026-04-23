// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 5

/// //////////// //////////// //////////// /////////
//leaving this for reference although not needed
/// //////////// //////////// //////////// /////////

import java.io.*;

public class BadDecompressor {

    public static void main(String[] args) throws IOException {

        DataInputStream in =
                new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(args[0])));

        DataOutputStream out =
                new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(args[1])));

        decompress(in, out);

        out.close();
        in.close();

        System.out.println("Decompression complete.");
    }

    public static void decompress(DataInputStream in, DataOutputStream out) throws IOException {

        while (true) {
            int byte1;
            int byte2;
            int byte3 = 0;
            boolean hasThird = true;

            try {
                byte1 = in.readUnsignedByte();
            } catch (EOFException e) {
                break;
            }

            try {
                byte2 = in.readUnsignedByte();
            } catch (EOFException e) {
                break;
            }

            try {
                byte3 = in.readUnsignedByte();
            } catch (EOFException e) {
                hasThird = false;
            }

            // rebuild first 12-bit value from byte1 and top 4 bits of byte2
            int first12 = ((byte1 << 4) | (byte2 >> 4)) & 0x0FFF;
            int firstByteOut = first12 & 0x00FF; // first byte out is low 8 bits of first12 (12-bit value)
            // we could have also done directly firstByteOut = ((byte1 << 4) | (byte2 >> 4)) & 0x00FF;
            out.writeByte(firstByteOut);

            if (hasThird) {
                // rebuild second 12-bit value from low 4 bits of byte2 and all of byte3
                int second12 = (((byte2 & 0x0F) << 8) | byte3) & 0x0FFF;
                int secondByteOut = second12 & 0x00FF; // second byte out is low 8 bits of second12 (12-bit value)
                // we could have also done directly secondByteOut = (((byte2 & 0x0F) << 8) | byte3) & 0x00FF;
                out.writeByte(secondByteOut);
            }
        }

        out.flush();
    }
}