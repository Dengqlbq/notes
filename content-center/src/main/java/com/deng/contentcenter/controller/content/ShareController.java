package com.deng.contentcenter.controller.content;

import com.deng.contentcenter.auth.CheckLogin;
import com.deng.contentcenter.domain.dto.content.ShareDTO;
import com.deng.contentcenter.domain.entity.content.Share;
import com.deng.contentcenter.service.content.ShareService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    @GetMapping("/{id}")
    @CheckLogin
    public ShareDTO findById(@PathVariable Integer id) {
        return this.shareService.findById(id);
    }

    @GetMapping("/query")
    public PageInfo<Share> query(@RequestParam(required = false) String title,
                                 @RequestParam(required = false, defaultValue = "1") Integer page,
                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (size > 100) {
            size = 100;
        }

        return this.shareService.query(title, page, size);
    }
}
