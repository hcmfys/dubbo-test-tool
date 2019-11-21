# dubbo-test-tool
dubbo服务测试工具

import java.io.IOException;

public class APIService {

    /**
     * 1.运行你的测试service服务
     * 2.修改service对应的 port 端口
     * 3.点击获取方法按钮
     * 4.选择方法，
     * 5.从模拟参数窗口 复制参数 到运行参数窗口
     * 6.执行方法，查看运行结果
     * @throws IOException
     */

    @Test
    public void openserver() throws IOException {
        new DubboGUI("127.0.0.1",21921);
        System.in.read();
    }

}
