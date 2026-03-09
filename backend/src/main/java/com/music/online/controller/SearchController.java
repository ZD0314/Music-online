package com.music.online.controller;

import com.music.online.common.Result;
import com.music.online.service.SearchService;
import com.music.online.vo.SearchResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索控制器 - 用户端
 *
 * @since 2026-03-09
 */
@Api(tags = "搜索接口 - 用户端")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 综合搜索
     */
    @ApiOperation("综合搜索")
    @GetMapping
    public Result<SearchResultVO> search(
            @ApiParam("搜索关键词") @RequestParam String keyword
    ) {
        SearchResultVO result = searchService.search(keyword);
        return Result.success(result);
    }
}
