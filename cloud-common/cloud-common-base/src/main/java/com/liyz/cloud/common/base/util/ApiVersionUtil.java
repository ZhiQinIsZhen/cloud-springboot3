package com.liyz.cloud.common.base.util;

import com.google.common.base.Splitter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Desc:接口版本工具类
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/2/27 16:53
 */
@UtilityClass
public class ApiVersionUtil {


    /**
     * 比较两个版本号的大小
     *
     * @param sourceVersion 第一个版本号字符串，各部分由点（.）分隔
     * @param targetVersion 第二个版本号字符串，各部分由点（.）分隔
     * @return 返回值小于、等于、大于0分别表示sourceVersion小于、等于、大于targetVersion
     *         如果任一版本号为空，则返回-1
     */
    public static int compareVersion(String sourceVersion, String targetVersion) {
        // 检查任一版本号是否为空，为空则返回-1
        if (StringUtils.isAnyBlank(sourceVersion, targetVersion)) {
            return -1;
        }

        // 将源版本号字符串按点（.）分割并转换为整数列表
        List<Integer> sourceVersionList = Splitter.on("\\.").splitToList(sourceVersion).stream().map(Integer::parseInt).toList();
        // 将目标版本号字符串按点（.）分割并转换为整数列表
        List<Integer> targetVersionList = Splitter.on("\\.").splitToList(targetVersion).stream().map(Integer::parseInt).toList();

        // 比较版本号列表的长度，如果长度不等，则返回长度差
        if (sourceVersionList.size() != targetVersionList.size()) {
            return sourceVersionList.size() - targetVersionList.size();
        }

        // 遍历版本号列表，逐个比较各部分的数值
        for (int i = 0; i < sourceVersionList.size(); i++) {
            // 如果当前部分的版本号不相等，则返回它们的差值
            if (sourceVersionList.get(i) - targetVersionList.get(i) != 0) {
                return sourceVersionList.get(i) - targetVersionList.get(i);
            }
        }

        // 如果所有部分的版本号都相等，则返回0
        return 0;
    }
}
