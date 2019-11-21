package org.springbus.gui;


import com.alibaba.fastjson.JSON;
import com.github.jsonzou.jmockdata.JMockData;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class DubboCmd {
    /**
     * @param cmd
     * @param host
     * @param port
     * @return
     */
    public static String executeCmd(String cmd, String host, int port) {

        try {
            Selector selector = null;


            Charset charset = Charset.forName("GBK");
            //新建selector
            selector = Selector.open();

            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress(host, port));
            channel.configureBlocking(false);
            channel.socket().setSoTimeout(10000);

            channel.register(selector, SelectionKey.OP_READ);

            ByteBuffer buffer = ByteBuffer.wrap((cmd + "\r\n").getBytes());
            channel.write(buffer);
            StringBuilder sb = new StringBuilder();
            boolean isStop = false;
            ByteBuffer msgBuf = ByteBuffer.allocate(1024);
            while (!isStop && selector.select() > 0) {
                //遍历selector上的已注册的key并且处理key对应的channel
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext() && !isStop) {
                    SelectionKey sk = it.next();
                    if (sk.isReadable()) {
                        //读取客户端数据处理
                        SocketChannel sc = (SocketChannel) sk.channel();
                        String content = "";
                        try {
                            int c = sc.read(buffer);
                            if (c > 0) {

                                buffer.flip();
                                int size = msgBuf.position() + buffer.limit();
                                if (size > msgBuf.capacity()) {
                                    ByteBuffer newBuf = ByteBuffer.allocate(2 * msgBuf.capacity());
                                    msgBuf.flip();
                                    newBuf.put(msgBuf);
                                    newBuf.put(buffer);
                                    msgBuf.clear();
                                    msgBuf = newBuf;
                                } else {
                                    msgBuf.put(buffer);
                                }
                                buffer.flip();
                                content += charset.decode(buffer);
                                sb.append(content);
                            }

                            String st = sb.toString();

                            if (st.length() > 7 && st.indexOf("\r\ndubbo>") > 0) {
                                isStop = true;
                                channel.socket().close();
                                sk.cancel();
                                if (sk.channel() != null)
                                    sk.channel().close();
                                selector.close();

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                            sk.cancel();
                            if (sk.channel() != null)
                                sk.channel().close();
                        }
                        buffer.clear();

                    }

                    it.remove();
                }
            }
            msgBuf.flip();
            String msg = charset.decode(msgBuf).toString();
            int index = msg.lastIndexOf("\r\ndubbo>");
            if (index != -1) {
                msg = msg.substring(0, index);
            }
            return msg;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Map<String, List<String>> parseMethodList(String list) {
        Map<String, List<String>> retMap = new HashMap<>();
        String[] mList = list.split("\r\n");
        for (String m : mList) {

            int i = m.indexOf("interface=");
            int j = m.indexOf("&", i + 10);
            if (i > 0 && j > 0 && j > i) {
                String interfaceName = m.substring(i + 10, j);
                retMap.put(interfaceName, new ArrayList<>());
                i = m.indexOf("methods=");
                j = m.indexOf("&", i + 8);
                if (i > 0 && j > 0 && j > i) {
                    String methodName = m.substring(i + 8, j);
                    String[] methodsList = methodName.split(",");
                    for (String method : methodsList) {
                        retMap.get(interfaceName).add(method);
                    }
                }
            }
        }
        return retMap;
    }


    /**
     * @param serverName
     * @param methodName
     * @return
     */
    public static String[] parseMethod(String serverName, String methodName) {
        StringJoiner sb = new StringJoiner(",");
        StringJoiner methodSb = new StringJoiner(",");
        String[] strList = new String[2];
        Method[] methods = new Method[0];
        try {
            Class<?> ctClass = Class.forName(serverName);
            methods = ctClass.getDeclaredMethods();


            for (Method m : methods) {
                if (m.getName().equals(methodName)) {
                    Parameter[] parameterTypes = m.getParameters();

                    int size = parameterTypes.length;
                    for (int i = 0; i < size; i++) {
                        Parameter p = parameterTypes[i];
                        Object ct = JMockData.mock(p.getType());

                        sb.add(JSON.toJSONString(ct,true));
                        methodSb.add(p.getType().getSimpleName() + " " + p.getName());
                    }


                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        strList[0] = serverName + "." + methodName + "(" + sb.toString() + ")";
        strList[1] = serverName + "." + methodName + "(" + methodSb.toString() + ")";
        return strList;

    }



    public static void main(String[] args) {

        String host = "172.18.109.9";
        String t = DubboCmd.executeCmd("ls -l", host, 21910);
        System.out.println(t);
        Map<String, List<String>> map = parseMethodList(t);
        System.out.println(JSON.toJSONString(map));

        //t = DubboCmd.executeCmd("invoke com.evergrande.decorate.cms.service.ArticleCategoryService.getDetails(1)", host, 21910);
        //System.out.println(t);
        new DubboGUI("192.168.56.2",21902);

    }
}
