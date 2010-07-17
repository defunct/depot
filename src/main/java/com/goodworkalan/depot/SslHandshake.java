package com.goodworkalan.depot;

import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_TASK;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_WRAP;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
import static javax.net.ssl.SSLEngineResult.Status.BUFFER_UNDERFLOW;
import static javax.net.ssl.SSLEngineResult.Status.OK;

import java.nio.ByteBuffer;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

/**
 * Implements an initial SSL handshake for the StartTLS command.
 * <p>
 * SSL will on occasion renegotiate a handshake while transporting application
 * data, so the handshake mechanism is built into the normal wrapping and
 * unwrapping functions. A handshake status flag is returned that indicates the
 * status of the handshake.
 * <p>
 * In order to run the initial handshake, you seed to wrap and unwrap empty
 * buffers, exchange empty buffers with the client.
 * <p>
 * FIXME Keeping this around for posterity. Delete when you get a chance.
 */
class SslHandshake extends Interpreter {
    /** An empty buffer to wrap. */
    private final static ByteBuffer BLANK = ByteBuffer.allocate(0);

    /** The command code sent with the StartTLS command. */
    private final String code;

    /** The SSL engine to use for this socket. */
    private final SSLEngine sslEngine;

    /** A buffer for incoming SSL encrypted data. */
    private ByteBuffer incoming;

    /** True if the handshake has finished. */
    private boolean finished;

    /**
     * Create an SSL handshake interperter with the given command tag from the
     * StartTLS command that uses the given SSL engine to encrypt this socket.
     * 
     * @param code
     *            The StartTLS command tag.
     * @param sslEngine
     *            The ssl engine.
     */
    public SslHandshake(String code, SSLEngine sslEngine) {
        SSLSession session = sslEngine.getSession();
        this.code = code;
        this.sslEngine = sslEngine;
        this.incoming = ByteBuffer.allocate(session.getPacketBufferSize());
        this.incoming.limit(0);
    }

    /**
     * Read SSL encrypted data from the data buffer and unwraps zero length
     * application data buffers from the client. SSL is able to handshake while
     * exchanging user data. During the initial handshake, the client
     * application will send only handshake data and the user buffers will be
     * empty. Here we unwrap buffers that have empty application buffers.
     * <p>
     * When the SSL engine expected data from the client it sets a
     * <code>NEED_UNWRAP</code> handshake status. This method will attempt to
     * read data from the incoming data and unwrap it. If there is not enough
     * data to unwrap, if the data buffer is empty, we return and wait for the
     * next read operation.
     * <p>
     * Any delegated tasks required by the SSL engine are run in this thread,
     * since it is already a worker thread.
     * <p>
     * A successful handshake will finish during this read method, so we set the
     * next wrapper and reset the interpreter to a command interpeter. A
     * finished flag will prevent execute from doing anything.
     *
     * @param data The input data send by the client.
     * @return True if there is enough data availble to execute the interpreter.
     */
    @Override
    public boolean read(ByteBuffer data) throws Bad {
        SSLSession session = sslEngine.getSession();

        ByteBuffer destination = ByteBuffer.allocate(session.getApplicationBufferSize());
        try {
            int dataLimit = data.limit();
            SSLEngineResult.HandshakeStatus handShakeStatus;
            SSLEngineResult.Status status = OK;
            do {
                int incomingRemaining = incoming.remaining();

                incoming.compact();

                if (incomingRemaining < incoming.capacity() && dataLimit - data.position() != 0) {
                    int overflow = (dataLimit - data.position()) - incoming.remaining();
                    if (overflow > 0) {
                        data.limit(data.position() + incoming.remaining());
                    } else {
                        data.limit(dataLimit);
                    }
                    incomingRemaining += data.remaining();
                    incoming.put(data);
                }
                
                incoming.flip();
                
                handShakeStatus = sslEngine.getHandshakeStatus();
                switch (handShakeStatus) {
                case NOT_HANDSHAKING:
                    break;
                case FINISHED:
                    break;
                case NEED_TASK:
                    Runnable runnable = null;
                    while ((runnable = sslEngine.getDelegatedTask()) != null) {
                        runnable.run();
                    }
                    break;
                case NEED_WRAP:
                    break;
                case NEED_UNWRAP:
                    SSLEngineResult result = sslEngine.unwrap(incoming, destination);
                    status = result.getStatus();
                    handShakeStatus = result.getHandshakeStatus();
                    System.out.println(destination.remaining() == destination.capacity());
                    break;
                }
            } while (
                (status == OK || (status == BUFFER_UNDERFLOW && dataLimit - data.position() != 0))
                && (handShakeStatus == NEED_UNWRAP || handShakeStatus == NEED_TASK));
            finished = handShakeStatus == FINISHED;
            return handShakeStatus != NEED_UNWRAP;
        } catch (SSLException e) {
            throw new Bad("*", "Unable to get going here");
        }    
    }
    
    /**
     * Write SSL encrypted data by sending empty blocks so that the SSL engine
     * will send its handshake information with the empty blocks.  SSL is able
     * to handshake while exchanging user data. During the initial handshake,
     * the client application will send only handshake data and the user buffers
     * will be empty. Here we unwrap buffers that have empty application
     * buffers.
     * <p>
     * When the SSL engine needs to send data to the client it will set a 
     * <code>NEED_WRAP</code> handshake status. This method will wrap an empty
     * buffer to send to the client. The SSL engine will tuck its handshake
     * information into the wrapped empty buffer.
     * <p>
     * Any delegated tasks required by the SSL engine are run in this thread,
     * since it is already a worker thread.
     * <p>
     * A successful handshake will finish during this read method, so this
     * method checks a finished flag and does nothing if it is set. The next
     * interpreter will be a command intepreter.
     *
     * @param session The depot session.
     * @param session The response.
     */
    @Override
    public void execute(DepotSession session, Response response) throws Bad {
        if (finished) {
            try {
                setNextInterpreter(this);
                SSLEngineResult.HandshakeStatus handShakeStatus;
                do {
                    handShakeStatus = sslEngine.getHandshakeStatus();
                    switch (handShakeStatus) {
                    case NOT_HANDSHAKING:
                        sslEngine.beginHandshake();
                        response.ok(code, "Begin TLS negotiation now");
                        break;
                    case FINISHED:
                        /*
                         * PROBLMES HERE DEAD CODE.
                         */
//                        session.setDataWrapper(new SslWrapper(sslEngine));
                        setNextInterpreter(session.newCommandInterpreter());
                        break;
                    case NEED_TASK:
                        Runnable runnable = null;
                        while ((runnable = sslEngine.getDelegatedTask()) != null) {
                            runnable.run();
                        }
                        break;
                    case NEED_WRAP:
                        ByteBuffer packet = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
                        SSLEngineResult result = sslEngine.wrap(BLANK, packet);
                        handShakeStatus = result.getHandshakeStatus();
                        switch (result.getStatus()) {
                        case OK:
                            packet.flip();
                            response.send(packet);
                            break;
                        }
                        break;
                    case NEED_UNWRAP:
                        break;
                    }
                } while (handShakeStatus == NOT_HANDSHAKING || handShakeStatus == NEED_WRAP || handShakeStatus == NEED_TASK);
            } catch (SSLException e) {
                e.printStackTrace();
                setNextInterpreter(session.newCommandInterpreter());
                response.bad("*", "Unable to get going here");
            }
        }
    }
}
