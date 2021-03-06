package org.springbus.gui;


import com.alibaba.fastjson.JSON;
import com.github.jsonzou.jmockdata.util.StringUtils;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.strobel.assembler.InputTypeLoader;
import com.strobel.assembler.metadata.ClasspathTypeLoader;
import com.strobel.assembler.metadata.CompositeTypeLoader;
import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;



import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static org.springbus.gui.DubboCmd.parseMethod;
import static org.springbus.gui.TreeModelUtil.explorerTree;


public class DubboGUI  extends JFrame {


    private String host = "";
    private int port = 0;


    private Map<String, List<String>> mapList;


    private JComboBox serverList;

    private JComboBox methodList;

    private RSyntaxTextArea mockArea;

    private JLabel contentLabel;

    private DefaultMutableTreeNode rootNode;

    private String[] dataList;


    private CompositeTypeLoader  compositeTypeLoader=new CompositeTypeLoader(new ClasspathTypeLoader(),new InputTypeLoader());
    private  DecompilerSettings settings=   new DecompilerSettings();


    public DubboGUI(String host, int port) {
        try {
            UIManager.setLookAndFeel(new PlasticLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        this.port = port;
        this.host = host;

        settings.setTypeLoader(compositeTypeLoader);
        init();
        setVisible(true);
    }

    /**
     * init
     */
    private void init() {
        JPanel btPanel = new JPanel();
        btPanel.setLayout(new FlowLayout());
        JLabel serverName = new JLabel("服务:");
        serverName.setHorizontalAlignment(SwingConstants.RIGHT);
        serverName.setPreferredSize(new Dimension(40, 30));
        btPanel.add(serverName);
        serverList = new JComboBox();
        serverList.setEditable(false);
        serverList.setPreferredSize(new Dimension(400, 30));
        btPanel.add(serverList);
        JLabel methodName = new JLabel("方法:");
        methodName.setHorizontalAlignment(SwingConstants.RIGHT);
        methodName.setPreferredSize(new Dimension(40, 30));
        btPanel.add(methodName);
        methodList = new JComboBox();
        methodList.setPreferredSize(new Dimension(400, 30));
        btPanel.add(methodList);


        JButton btGet = new JButton("获取方法");
        btGet.setPreferredSize(new Dimension(120, 30));
        btPanel.add(btGet);

        JButton btExecute = new JButton("执行方法");

        btExecute.setPreferredSize(new Dimension(120, 30));
        btPanel.add(btExecute);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(btPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);


        RSyntaxTextArea resultArea = new RSyntaxTextArea(20, 60);
        resultArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        resultArea.setCodeFoldingEnabled(true);


        RSyntaxTextArea parArea = new RSyntaxTextArea(20, 60);

        parArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        parArea.setCodeFoldingEnabled(true);

        mockArea = new RSyntaxTextArea(20, 60);
        mockArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        mockArea.setCodeFoldingEnabled(true);


        JTabbedPane resultTabPanel = new JTabbedPane();

        rootNode = new DefaultMutableTreeNode("result");


        // 使用根节点创建树组件
        JTree tree = new JTree(rootNode);

        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);

        resultTabPanel.addTab("json结果", new RTextScrollPane(resultArea));
        resultTabPanel.addTab("json视图", new JScrollPane(tree));
        tabbedPane.addTab("执行参数", new RTextScrollPane(parArea));
        tabbedPane.addTab("模拟参数", new RTextScrollPane(mockArea));
        tabbedPane.addTab("执行结果", resultTabPanel);


        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentLabel = new JLabel("");
        contentLabel.setPreferredSize(new Dimension(-1, 30));

        JPanel contentLabelPanel = new JPanel();
        contentLabelPanel.setLayout(new BorderLayout());
        contentLabelPanel.setPreferredSize(new Dimension(-1, 30));
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(26, 30));
        contentLabelPanel.add(emptyLabel, BorderLayout.WEST);
        contentLabelPanel.add(contentLabel, BorderLayout.CENTER);
        contentPanel.add(contentLabelPanel, BorderLayout.NORTH);
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JTabbedPane sysPanel = new JTabbedPane();
        sysPanel.addTab("测试平台", mainPanel);


        JPanel codePanel = new JPanel();


        JLabel labelLoad = new JLabel("已经加载的类:");
        labelLoad.setHorizontalAlignment(SwingConstants.RIGHT);
        labelLoad.setPreferredSize(new Dimension(100, 30));
        codePanel.add(labelLoad);


        JComboBox txtLoadCLass = new JComboBox<>();
        txtLoadCLass.setPreferredSize(new Dimension(400, 30));
        codePanel.add(txtLoadCLass);

        JLabel labelClassName = new JLabel("请输入类名称:");
        labelClassName.setHorizontalAlignment(SwingConstants.RIGHT);
        labelClassName.setPreferredSize(new Dimension(100, 30));
        codePanel.add(labelClassName);

        JTextField txtClass = new JTextField();
        txtClass.setPreferredSize(new Dimension(400, 30));
        txtClass.setText("java.lang.String");
        codePanel.add(txtClass);

        JButton btDecompile = new JButton("反编译");
        btDecompile.setPreferredSize(new Dimension(100, 30));
        codePanel.add(btDecompile);

        JPanel codeContentPanel = new JPanel();
        codeContentPanel.setLayout(new BorderLayout());
        codeContentPanel.add(codePanel, BorderLayout.NORTH);
        RSyntaxTextArea codeArea = new RSyntaxTextArea();
        codeArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        codeArea.setCodeFoldingEnabled(true);
        codeContentPanel.add(new RTextScrollPane(codeArea), BorderLayout.CENTER);

        sysPanel.addTab("编译平台", codeContentPanel);

        this.setContentPane(sysPanel);
        this.setSize(1200, 800);
        this.setTitle("Dubbo Service 测试平台");

        btGet.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            String t = DubboCmd.executeCmd("ls -l", host, port);
            if (t != null) {
                mapList = DubboCmd.parseMethodList(t);
                serverList.setModel(new ServerModel(mapList));
                dataList = mapList.keySet().toArray(new String[0]);

                serverList.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(DubboGUI.this, "获取接口错误，请检查端口是否正确，服务是否开启！");
            }
        }));

        btDecompile.addActionListener(e -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    PlainTextOutput plt = new PlainTextOutput();
                    String tClass = txtClass.getText();
                    Decompiler.decompile(tClass, plt);
                    codeArea.setText(plt.toString());
                }
            });

        });
        serverList.addItemListener(this::serverChanged);

        methodList.addItemListener(this::methodStateChanged);

        btExecute.addActionListener(e -> SwingUtilities.invokeLater(() -> {

            if (StringUtils.isEmpty(parArea.getText())) {
                JOptionPane.showMessageDialog(DubboGUI.this, "请输入请求参数，或者从模拟参数选项里复制！");
                return;
            }
            String cmd = "invoke " + parArea.getText();
            cmd = cmd.replace("\r", "");
            cmd = cmd.replace("\n", "");
            String ret = DubboCmd.executeCmd(cmd, host, port);
            rootNode.removeAllChildren();
            if (ret != null) {
                String msg = ret;
                String time = "";
                int index = ret.indexOf("elapsed:");
                if (index > 0) {
                    msg = ret.substring(0, index);
                    time = ret.substring(index);
                    try {
                        msg = JSON.toJSONString(JSON.parse(msg), true);
                        explorerTree(JSON.parse(msg), rootNode);
                        tree.updateUI();
                        tree.expandPath(tree.getSelectionPath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    ret = msg + "\n" + time;
                }

            }
            resultArea.setText(ret);

            tabbedPane.setSelectedIndex(2);
        }));
        this.setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {


            public void windowClosing(WindowEvent e) {

                if (JOptionPane.showConfirmDialog(DubboGUI.this, "确认要退出吗？") == JOptionPane.YES_OPTION) {
                    DubboGUI.this.dispose();

                }

            }

        });


        try {
            Field f = null;
            f = ClassLoader.class.getDeclaredField("classes");
            f.setAccessible(true);
            Vector v = (Vector) f.get(Thread.currentThread().getContextClassLoader());

            for (Object o : v) {
                String className = o.toString().replace("class ", "").trim();
                txtLoadCLass.addItem(className);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        txtLoadCLass.addItemListener(e -> {
            String clasName = txtLoadCLass.getSelectedItem().toString();
            PlainTextOutput plt = new PlainTextOutput();

            Decompiler.decompile(clasName, plt,settings);
            codeArea.setText(plt.toString());
        });


    }

    private void loadMockData() {
        if (serverList.getItemCount() > 0 && methodList.getItemCount() > 0) {
            String testServerName = dataList[serverList.getSelectedIndex()];
            String testMethodName = methodList.getSelectedItem().toString();
            String[] methodDataList = parseMethod(testServerName, testMethodName);
            String mockData = methodDataList[0];
            mockArea.setText(mockData);
            mockData = methodDataList[1];
            contentLabel.setText(mockData);

        }
    }


    public static void main(String[] args) throws Exception {

        //new DubboGUI("127.0.0.1", 12345);
        //attach();
    }



    private void methodStateChanged(ItemEvent e) {
        SwingUtilities.invokeLater(() -> {
            loadMockData();
        });
    }

    private void serverChanged(ItemEvent e) {
        SwingUtilities.invokeLater(() -> {
            String item = dataList[serverList.getSelectedIndex()];
            List<String> list = mapList.get(item);
            methodList.setModel(new ServerModel(list));
            methodList.setSelectedIndex(0);
            loadMockData();
        });
    }
}
