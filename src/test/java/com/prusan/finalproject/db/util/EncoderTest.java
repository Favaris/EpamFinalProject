package com.prusan.finalproject.db.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EncoderTest {
    @Test
    void shouldEncodeProperly() {
        byte[] pass = Encoder.encodePassword("asadsadasdasdasdasdasdsdf");
        assertArrayEquals(pass, pass);
    }
}
