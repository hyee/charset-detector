/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1998
 * the Initial Developer. All Rights Reserved.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * DO NOT EDIT THIS DOCUMENT MANUALLY !!!
 * THIS FILE IS AUTOMATICALLY GENERATED BY THE TOOLS UNDER
 *    AutoDetect/tools/
 */

package top.shareall.charset.detector;

import top.shareall.charset.detector.jchardet.nsDetector;
import top.shareall.charset.detector.jchardet.nsPSMDetector;

import java.io.*;

/**
 * 探测流对象
 *
 * @author liujichun
 */
public class CharsetDetector {

    private boolean found = false;
    private String charset = "";

    /**
     * 探测字符串内容字符集
     *
     * @param content 字符串内容
     * @return 返回对应的字符集
     * @throws IOException
     */
    public String detectStr(String content) throws IOException {
        return detect(new ByteArrayInputStream(content.getBytes()));
    }

    /**
     * 依据文件路径探测文件内容字符集
     *
     * @param path
     * @return
     * @throws IOException
     */
    public String detect(String path) throws IOException {
        return detect(new File(path));
    }

    /**
     * 探测文件内容字符集
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String detect(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return detect(stream);
        }
    }

    /**
     * 探测流内容字符集内容
     *
     * @return
     * @throws Exception
     */
    public String detect(InputStream inputStream) throws IOException {
        int lang = nsPSMDetector.ALL;
        nsDetector det = new nsDetector(lang);
        det.Init(a -> {
            found = true;
            charset = a;
        });

        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;

        while ((len = inputStream.read(buf, 0, buf.length)) != -1) {
            if (isAscii)
                isAscii = det.isAscii(buf, len);
            if (!isAscii && !done)
                done = det.DoIt(buf, len, false);
        }
        det.DataEnd();

        if (isAscii) {
            charset = "ASCII";
            found = true;
        }

        if (!found) {
            String prob[] = det.getProbableCharsets();
            charset = prob[0];
        }
        return charset;
    }
}
