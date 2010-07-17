package com.goodworkalan.depot;

import java.nio.ByteBuffer;

public class DataWrapper {
    public ByteBuffer[] wrap(ByteBuffer unwrapped) {
        return new ByteBuffer[] { unwrapped };
    }

    public ByteBuffer[] unwrap(ByteBuffer wrapped) {
        return new ByteBuffer[] { wrapped };
    }
}
