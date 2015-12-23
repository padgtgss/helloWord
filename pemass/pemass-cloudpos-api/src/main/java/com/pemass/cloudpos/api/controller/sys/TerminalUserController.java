/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.sys;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.util.FileUtil;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.service.pemass.sys.TerminalUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Description: TerminalUserController
 * @Author: zhou hang
 * @CreateTime: 2014-11-13 09:43
 */
@Controller
@RequestMapping("/terminalUser")
public class TerminalUserController {

    private Log logger = LogFactory.getLog(TerminalUserController.class);

    @Resource
    private TerminalUserService terminalUserService;

    /**
     * 修改用户信息
     *
     * @param terminalUser   b端对象
     * @param terminalUserId B端用户id
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_CASHIER','ROLE_TICKETER')")
    @RequestMapping(value = "/{terminalUserId}", method = RequestMethod.POST)
    @ResponseBody
    public Object updateTerminalUser(TerminalUser terminalUser, @PathVariable("terminalUserId") Long terminalUserId,
                                     @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) throws IOException {
        if (avatarFile != null && !avatarFile.isEmpty() && avatarFile.getSize() != 0) {
            String avatarPath = FileUtil.saveFile(avatarFile.getInputStream(), FileUtil.fetchExtension(avatarFile.getOriginalFilename()));
            terminalUser.setAvatar(avatarPath);
        }
        terminalUser.setId(terminalUserId);
        return ImmutableMap.of("result", terminalUserService.updateEntiry(terminalUser));
    }


}