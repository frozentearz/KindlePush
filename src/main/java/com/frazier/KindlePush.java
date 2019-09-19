package com.frazier;

import javax.servlet.http.HttpServletRequest;

import com.frazier.mail.EmailUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.*;

@SuppressWarnings("unused")
@RestController
public class KindlePush {
    @RequestMapping(value = "/push", method = {RequestMethod.POST})
    public Map<String, Object> push(@RequestParam MultipartFile file, HttpServletRequest request) {
        System.out.println("-----------Start-------------");
        Map<String, Object> result = new HashMap<>();
        String msg = "";

        String Email = request.getParameter("email");
        if (Email == null || Email.equals("") || Email.indexOf('@') == -1) {
            msg = "Email is wrong";
            result.put("msg", msg);
            return result;
        }

        if (null == file) {
            msg = "file is none";
            result.put("msg", msg);
            return result;
        }

        String bookName = file.getOriginalFilename();

        CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) file;
        DiskFileItem diskFileItem = (DiskFileItem) commonsmultipartfile.getFileItem();
        File book = diskFileItem.getStoreLocation();

        List<String> emails = new ArrayList<>();
        emails.add(Email);

        //send
        if (null == book || emails.size() == 0 || bookName == null || bookName.equals("")) {
            msg = "邮件发送参数组建失败";
            result.put("msg", msg);
            return result;
        }
        int resultCode = EmailUtils.sendAttachmentMail(emails, "EbookPush", "Kindle Ebooks Push", bookName, book);
        if (resultCode != 1) {
            msg = "发送失败";
            result.put("msg", msg);
            return result;
        }
        msg = "发送成功";
        result.put("msg", msg);
        result.put("code", 0);
        return result;
    }
}
