package io.github.zoowayss.starter.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SnowflakeIdGenerator {

    public static final SnowflakeIdGenerator generator = new SnowflakeIdGenerator(getWorkerId() % 32, 1);

    private final long twepoch = 1672531200000L;

    private final long workerIdBits = 5L;

    private final long datacenterIdBits = 5L;

    private final long maxWorkerId = ~(-1L << workerIdBits);

    private final long maxDatacenterId = ~(-1L << datacenterIdBits);

    private final long sequenceBits = 12L;

    private final long workerIdShift = sequenceBits;

    private final long datacenterIdShift = sequenceBits + workerIdBits;

    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final long sequenceMask = ~(-1L << sequenceBits);

    private long workerId;

    private long datacenterId;

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("workerId 超出范围");
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId 超出范围");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public static long getWorkerId() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] bytes = ip.getAddress();
            return ((bytes[bytes.length - 2] & 0B11) << 8) | (bytes[bytes.length - 1] & 0xFF);
        } catch (UnknownHostException e) {
            return 0;
        }
    }

    public synchronized String nextId() {
        return nextId("");
    }

    public synchronized String nextId(String prefix) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时间回退异常");
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                while ((timestamp = System.currentTimeMillis()) <= lastTimestamp) {}
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return prefix + (((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence);
    }
}
