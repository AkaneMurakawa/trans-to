package com.github.transto.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * ClipboardUtils
 * @author AkaneMurakawa
 * @date 2022-06-02
 */
public class ClipboardUtils {

    /**
     * 从剪切板获得文字
     */
    public static String getClipboardText() {
        String clipboardText = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);
        // 检查内容是否是文本类型
        if (clipTf != null && clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                clipboardText = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clipboardText;
    }
}
