package stepanenko.practice1;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import stepanenko.practice1.exception.CRC16Exception;
import stepanenko.practice1.exception.MagicByteException;
import stepanenko.practice1.model.Message;
import stepanenko.practice1.model.Packet;
import stepanenko.practice1.processing.DecryptorImpl;
import stepanenko.practice1.processing.EncryptorImpl;

/**
 * @author Liubomyr Stepanenko
 */
public class PacketTester {
    private static EncryptorImpl packetEncryptor;
    private static DecryptorImpl packetDecryptor;
    private static Message testMessage;
    private Packet testPacket;

    @BeforeClass
    public static void init() {
        packetEncryptor = new EncryptorImpl();
        packetDecryptor = new DecryptorImpl();
        testMessage = new Message(3, 11, new byte[] {1, 2, 4, 3, 0, 0, 9});
    }

    @Test(expected = MagicByteException.class)
    public void createPacket_WithIncorrectMagicByte_NotOk() {
        byte[] bytes = packetEncryptor.encryptPacketBytes(testMessage);
        bytes[0] = 0x7;
        testPacket = packetDecryptor.decryptPacket(bytes);
    }

    @Test(expected = CRC16Exception.class)
    public void createPacket_WithIncorrectCRC16Start_NotOk() {
        byte[] bytes = packetEncryptor.encryptPacketBytes(testMessage);
        bytes[Packet.HEADER_LENGTH] = bytes[Packet.HEADER_LENGTH - 1];
        testPacket = packetDecryptor.decryptPacket(bytes);
    }

    @Test(expected = CRC16Exception.class)
    public void createPacket_WithIncorrectCRC16End_NotOk() {
        byte[] bytes = packetEncryptor.encryptPacketBytes(testMessage);
        bytes[bytes.length - 1] = bytes[bytes.length - 2];
        testPacket = packetDecryptor.decryptPacket(bytes);
    }

    @Test
    public void encryptAndDecrypt_SinglePacket_Ok() {
        byte[] encryptedData = packetEncryptor.encryptPacketBytes(testMessage);
        testPacket = packetDecryptor.decryptPacket(encryptedData);
        Assert.assertEquals(testMessage, testPacket.getbMsg());
    }

    @Test
    public void encryptAndDecrypt_TwoSimilarPackets_Ok() {
        byte[] encryptedData = packetEncryptor.encryptPacketBytes(testMessage);
        testPacket = packetDecryptor.decryptPacket(encryptedData);
        byte[] anotherEncryptedData = packetEncryptor.encryptPacketBytes(testMessage);
        Packet anotherTestPacket = packetDecryptor.decryptPacket(anotherEncryptedData);
        Assert.assertEquals(testPacket.getbMsg(), anotherTestPacket.getbMsg());
    }
}