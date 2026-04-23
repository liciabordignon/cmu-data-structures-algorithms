// Name: Licia Bordignon (lbordign)
// Course: Data Structures and Algorithms (95-771)
// Assignment: Project 5

/// //////////// //////////// //////////// /////////
//leaving this for reference although not needed
/// //////////// //////////// //////////// /////////


import java.io.*;

public class BadCompressor {

    public static void main(String[] args) throws IOException {

        DataInputStream in =
                new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(args[0])));

        DataOutputStream out =
                new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(args[1])));

        compress(in, out);

        out.close();
        in.close();

        System.out.println("Compression complete.");
    }

    public static void compress(DataInputStream in, DataOutputStream out) throws IOException {

        while (true) {
            int firstByteIn;
            int secondByteIn = 0;
            boolean hasSecond = true;

            try {
                firstByteIn = in.readUnsignedByte(); //8 bits
            } catch (EOFException e) {
                break;
            }

            try {
                secondByteIn = in.readUnsignedByte(); //8 bits
            } catch (EOFException e) {
                hasSecond = false;
            }

            // 0x0FFF = 00000000 00000000 00001111 11111111
            // 0x00FF = 00000000 00000000 00000000 11111111
            // 0x00F = 00000000 00000000 00000000 00001111

            // example with A to understand
            // 00000000 00000000 00000000 01000001   (32 bits)
            // 00000000 00000000 00001111 11111111 (0x0FFF)
            int first12 = firstByteIn & 0x00FF; //  The end creates 12 bits (basically adding zeros at the beginning)
            int second12 = secondByteIn & 0x00FF;

            int out1 = (first12 >> 4) & 0xFF; // // take the first 8 bits of the 12-bit value (drop the last 4 bits)
            int out2 = ((first12 & 0x0F) << 4) | ((second12 >> 8) & 0x0F);  // take the last 4 bits of the 12-bit value and shift them left to become the high 4 bits
            // then OR with first 4 bits of second12 (obtained by shifting right 8 bits) - all zeros
            // result = middle byte: [last 4 bits of first][first 4 bits of second]
            int out3 = second12 & 0xFF; // take the LAST 8 bits of second12

            out.writeByte(out1);
            out.writeByte(out2);

            if (hasSecond) {
                out.writeByte(out3);
            }
        }

        out.flush();
    }
}