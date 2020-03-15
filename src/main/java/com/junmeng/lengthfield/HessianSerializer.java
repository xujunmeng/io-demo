package com.junmeng.lengthfield;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by jgsoft on 2020/3/15.
 */
public class HessianSerializer {

    public HessianSerializer() {

    }

    public static byte[] serialize(Object obj) throws IOException {
        if (obj == null) {
            throw new NullPointerException();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        ho.writeObject(obj);
        return os.toByteArray();
    }


    public static <T> T deserialize(byte[] data) throws IOException {
        if (data == null) {
            throw new NullPointerException();
        }

        ByteArrayInputStream is = new ByteArrayInputStream(data);
        HessianInput hi = new HessianInput(is);
        return (T)hi.readObject();

    }
}
