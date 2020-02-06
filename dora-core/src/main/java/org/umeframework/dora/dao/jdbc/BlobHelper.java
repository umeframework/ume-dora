/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * Blob
 * 
 * @author Yue MA
 * 
 */
public class BlobHelper implements java.sql.Blob, Serializable {
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 2956329206541377737L;
    /**
     * ERR_HAS_FREEED
     */
    private static final String ERR_HAS_FREEED = "data has been freeed.";
    /**
     * ERR_OUT_OF_BOUNDARY
     */
    private static final String ERR_OUT_OF_BOUNDARY = "out of boundary.";
    /**
     * ERR_NOT_ENOUGH
     */
    private static final String ERR_NOT_ENOUGH = "no enough data.";
    /**
     * data
     */
    private byte[] data;
    /**
     * hasFreeed
     */
    private boolean hasFreeed = false;

    /**
     * Blob
     */
    public BlobHelper(final byte[] bytes) {
        this.data = bytes;
        hasFreeed = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#length()
     */
    @Override
    public long length() throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        return lbytes.length;
    }

    /**
     * getBytes
     * 
     * @return
     * @throws SQLException
     */
    public byte[] getBytes() throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        return lbytes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#getBytes(long, int)
     */
    @Override
    public byte[] getBytes(long pos, int length) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        final int dlen = lbytes.length;

        pos--;
        if (pos < 0 || pos > dlen) {
            throw new SQLException(ERR_OUT_OF_BOUNDARY);
        }
        if (length < 0 || length > dlen - pos) {
            throw new SQLException(ERR_NOT_ENOUGH);
        }

        final byte[] out = new byte[length];
        System.arraycopy(lbytes, (int) pos, out, 0, length);
        return out;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#getBinaryStream()
     */
    @Override
    public InputStream getBinaryStream() throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        return new ByteArrayInputStream(lbytes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#position(byte[], long)
     */
    @Override
    public long position(byte[] pattern, long start) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        final int dlen = lbytes.length;

        if (start > dlen || pattern == null) {
            return -1;
        } else if (start < 1) {
            start = 0;
        } else {
            start--;
        }
        final int plen = pattern.length;
        if (plen == 0 || start > dlen - plen) {
            return -1;
        }

        final int stop = dlen - plen;
        final byte b0 = pattern[0];

        outer_loop: for (int i = (int) start; i <= stop; i++) {
            if (lbytes[i] != b0) {
                continue;
            }
            int len = plen;
            int doffset = i;
            int poffset = 0;
            while (len-- > 0) {
                if (lbytes[doffset++] != pattern[poffset++]) {
                    continue outer_loop;
                }
            }
            return i + 1;
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#position(java.sql.Blob, long)
     */
    @Override
    public long position(java.sql.Blob pattern, long start) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        final int dlen = lbytes.length;

        if (start > dlen || pattern == null) {
            return -1;
        } else if (start < 1) {
            start = 0;
        } else {
            start--;
        }

        final long plen = pattern.length();
        if (plen == 0 || start > ((long) dlen) - plen) {
            return -1;
        }

        final int iplen = (int) plen;
        byte[] bap = pattern.getBytes(1, iplen);
        final int stop = dlen - iplen;
        final byte b0 = bap[0];

        outer_loop: for (int i = (int) start; i <= stop; i++) {
            if (lbytes[i] != b0) {
                continue;
            }

            int len = iplen;
            int doffset = i;
            int poffset = 0;

            while (len-- > 0) {
                if (lbytes[doffset++] != bap[poffset++]) {
                    continue outer_loop;
                }
            }
            return i + 1;
        }
        return -1;
    }

    /**
     * setBytes
     * 
     * @param bytes
     * @throws SQLException
     */
    public void setBytes(byte[] bytes) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        this.data = bytes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#setBytes(long, byte[])
     */
    @Override
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final int dlen = data.length;
        pos--;
        if (pos < 0 || pos > dlen) {
            return 0;
        }
        int length = bytes.length;
        if (length <= 0 || length > dlen - pos) {
            return 0;
        }
        System.arraycopy(bytes, 0, data, (int) pos, length);
        return length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#setBytes(long, byte[], int, int)
     */
    @Override
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final int dlen = data.length;

        pos--;
        if (pos < 0 || pos > dlen) {
            return 0;
        }
        int writeLength = bytes.length - offset;
        if (writeLength <= 0 || writeLength > dlen - pos) {
            return 0;
        }
        System.arraycopy(bytes, offset, data, (int) pos, writeLength);
        return writeLength;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#setBinaryStream(long)
     */
    @Override
    public OutputStream setBinaryStream(long pos) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        final int dlen = lbytes.length;

        pos--;
        if (pos < 0 || pos > dlen) {
            throw new SQLException(ERR_OUT_OF_BOUNDARY);
        } else {
            return new ByteArrayOutputStream(dlen - (int) pos);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#truncate(long)
     */
    @Override
    public void truncate(long len) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        final int dlen = lbytes.length;

        if (len > dlen) {
            throw new SQLException(ERR_NOT_ENOUGH);
        } else {
            int writeLenth = dlen - (int) len;
            byte[] newData = new byte[writeLenth];
            System.arraycopy(lbytes, (int) len, newData, 0, writeLenth);
            data = newData;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#free()
     */
    @Override
    public void free() throws SQLException {
        if (!hasFreeed) {
            final int dlen = data.length;
            byte[] newData = new byte[dlen];
            System.arraycopy(newData, 0, data, 0, dlen);
            hasFreeed = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.sql.Blob#getBinaryStream(long, long)
     */
    @Override
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        if (hasFreeed) {
            throw new SQLException(ERR_HAS_FREEED);
        }
        final byte[] lbytes = data;
        final int dlen = lbytes.length;

        pos--;
        if (pos < 0 || pos > dlen) {
            throw new SQLException(ERR_OUT_OF_BOUNDARY);
        }
        if (length < 0 || length > dlen - pos) {
            throw new SQLException(ERR_NOT_ENOUGH);
        }

        final byte[] newData = new byte[(int) length];
        System.arraycopy(lbytes, (int) pos, newData, 0, (int) length);
        return new ByteArrayInputStream(newData);
    }

}
