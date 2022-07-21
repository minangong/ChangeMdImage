package com.mng.matlabjar;



import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetPic {
    private static String folderName = "D:\\obsidian\\MdRepo";

    private static List<String> errFileNameList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        List<String> pathList = getAllFileName(folderName);
        System.out.println("成功获取所有文件名称！");
        System.out.println("第一个文件名称为:" + pathList.get(1));
        for(String path:pathList){
            getPicfrom(path);

        }
        // 如果发现文件名称和预期的不一样，别误操作了
//        System.out.println("输入任意数字，再按回车键继续...");
//        getPicfrom(pathList.get(0));
//        new Scanner(System.in).nextInt();
    }

    private static List<String> getAllFileName(String folderName) {
        ArrayList<String> filePathList = new ArrayList<>();
        dfs(folderName, filePathList);
        return filePathList;
    }

    /**
     * 递归获取文件名
     *
     * @param path
     * @param filePathList
     */
    private static void dfs(String path, List<String> filePathList) {
        File file = new File(path);
        if (file.isFile() && file.getName().endsWith(".md")) {
            filePathList.add(file.getAbsolutePath());
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tmpFile : files) {
                dfs(tmpFile.getAbsolutePath(), filePathList);
            }
        }
    }

    private static boolean getPicfrom(String filePath) {

        File file = new File(filePath);
        System.out.println(file.getParent());

        String filename = file.getName();
        System.out.println(filename.substring(0, filename.indexOf('.')));
        File pathdir = new File(file.getParent() + "\\" +
                filename.substring(0, filename.indexOf('.')) + ".assets");
        pathdir.mkdir();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String imgName = null;

            String regex = "[^/]*.png";
            Pattern pattern = Pattern.compile(regex);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                int count = 0;
                if(m.find()) {
                    imgName =  line.substring(m.start(),m.end());
                    System.out.println(imgName);
                    File imgFrom = new File("C:\\Users\\mng\\Desktop\\mng_images-main\\images\\"+imgName);
                    File imgTo = new File(file.getParent() + "\\" +
                            filename.substring(0, filename.indexOf('.')) + ".assets\\"+imgName);
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(imgFrom));
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imgTo));
                    byte[] bys = new byte[1024];
                    int len = 0;
                    while ((len = bis.read(bys)) != -1) {
                        bos.write(bys, 0, len);
                    }
                    bos.close();
                    bis.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
