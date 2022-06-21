package transto;

import transto.api.youdao.YoudaoApi;
import transto.util.ClipboardUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.URL;

/**
 * 应用程序
 * @author AkaneMurakawa
 * @date 2022-06-02
 */
public class Application extends JFrame {

    private JLabel transLabel;

    private Application self;

    /** 查询内容 */
    private static String queryContext = "";

    private static final int DEFAULT_WIDTH = 320;
    private static final int DEFAULT_HEIGHT = 240;
    private static final int DEFAULT_FONT_SIZE = 16;
    private static final String DEFAULT_LABEL = formatMsg("使用说明", "Ctrl+C复制，然后点击翻译框进行翻译，鼠标滚轮可放大缩小，1 QPS限制");

    public Application(String title) throws HeadlessException {
        super(title);
        self = this;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        transLabel = new JLabel(DEFAULT_LABEL);
        transLabel.setFont(new Font("微软雅黑", Font.PLAIN, DEFAULT_FONT_SIZE));
        transLabel.setBackground(new Color(246, 246, 246));
        // 设置不透明
        transLabel.setOpaque(true);
        this.add(transLabel, BorderLayout.CENTER);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 设置窗体位置、Frame大小
        this.setBounds(screenSize.width - DEFAULT_WIDTH, DEFAULT_HEIGHT / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // 置顶
        this.setAlwaysOnTop(true);
        URL resource = getClass().getResource("/icon.jpg");
        this.setIconImage(new ImageIcon(resource).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // 注册监听事件
        this.addMouseListener(new MouseHandler());
        this.addMouseWheelListener(new MouseWheelHandler());
    }

    /**
     * 鼠标滚轮
     */
    class MouseWheelHandler implements MouseWheelListener{

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            // 鼠标滚轮，向上时wheelRotation为负值，进行放大；向下时wheelRotation为正值，进行缩小
            int change = e.getWheelRotation() == -1 ? 10 : -10;
            self.setSize((int) self.getSize().getWidth() + change, (int) self.getSize().getHeight()  + change);
        }
    }

    /**
     * 鼠标事件
     */
    class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            ctrlCEvent();
        }

        private void ctrlCEvent(){
            String query = ClipboardUtils.getClipboardText();
            // 校验复制内容
            if("".equals(query.trim())){
                transLabel.setText(formatMsg("提示", "请先Ctrl+C复制内容"));
                return;
            }
            // 校验已翻译
            if (queryContext.equals(query)){
                return;
            }
            queryContext = query;
            // 翻译
            String result = formatMsg(query, YoudaoApi.translate(query));
            resetLabelAndFrame(result);
        }
    }

    /**
     * 自适应大小，3:2比例
     * 设 width_size = 3x, height_size = 2x, 字符的总数length为total
     * ∴ 3x * 2x = total
     * 则 width_size = (√total/5) * 2
     * 则 width = width_size * font_size + margin_size
     * height同理
     */
    private void resetLabelAndFrame(String result){
        int height = (int) (Math.sqrt(result.length() / 5 ) * 2  * DEFAULT_FONT_SIZE) + 40;
        height = height > DEFAULT_HEIGHT ? height : DEFAULT_HEIGHT;
        int width = (int) (Math.sqrt(result.length() / 5) * 3 * DEFAULT_FONT_SIZE) + 20;
        width = width > DEFAULT_WIDTH ? width : DEFAULT_WIDTH;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 判断是否靠左
        boolean isLeft = self.getX() < screenSize.width / 2;
        // 设置窗体位置、Frame大小，X、Y的位置相对顶点不改变
        if(isLeft){
            self.setBounds(self.getX(), self.getY(), width, height);
        }else{
            self.setBounds(screenSize.width - width, self.getY(), width, height);
        }
        transLabel.setText(result);
    }

    /**
     * 格式化信息
     * @param args0
     * @param args1
     * @return
     */
    private static String formatMsg(Object args0, Object args1){
       return String.format(
                    "<html>" +
                        "<body" +
                            "<p style='background-color:#ffffff; color:#555; padding: 10px; margin: 0px 10px 0px 10px;'>" +
                                "%s" +
                            "<p/>" +
                            "<p style='background-color:#ffffff; color:#5ebb8d; padding: 10px; margin: 0px 10px 0px 10px;'>" +
                                "%s" +
                            "<p/>" +
                        "</body>" +
                    "</html>", args0, args1);
    }

    public static void main(String[] args) {
        try {
            // 设置窗体风格
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Application("TransTo");
    }
}

