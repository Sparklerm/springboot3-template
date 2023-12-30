package ${groupId}.common.model.base;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${groupId}.common.enums.YesNoEnum;
import ${groupId}.common.utils.StrUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;

/**
 * 基础查询对象
 *
 * @author alex meng
 * @createDate 2023-12-29 18:09
 */
@Data
public class BasePageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 默认分页最大页码
     */
    private static final long MAX_PAGE_SIZE = 50L;

    /**
     * 排序：正序
     */
    private static final String SORT_ASC = "ASC";

    /**
     * 排序：倒序
     */
    private static final String SORT_DESC = "DESC";

    @Schema(description = "查询页码")
    private long current;
    @Schema(description = "单页数据限制")
    private Long limit;
    @Schema(description = "排序字段")
    private String sortField;
    @Schema(description = "排序类型，默认正序")
    private String orderType = SORT_ASC;
    @Schema(description = "是否开启驼峰转下划线，默认开")
    private Boolean enableCamelToUnderline = YesNoEnum.YES.getValue();
    @Schema(description = "是否统计总条数，默认开")
    private Boolean searchCount = YesNoEnum.YES.getValue();

    public void setSortField(String sortField) {
        String sortFieldTmp = sortField;
        if (this.enableCamelToUnderline) {
            sortFieldTmp = StrUtils.convertCamelToUnderline(sortField);
        }
        this.sortField = sortFieldTmp;
    }

    public void setLimit(Long limit) {
        this.limit = limit < MAX_PAGE_SIZE ? limit : MAX_PAGE_SIZE;
    }

    public void setOrderType(String orderType) {
        switch (orderType) {
            case SORT_ASC, SORT_DESC -> this.orderType = orderType;
            default -> this.orderType = SORT_ASC;
        }
    }

    /**
     * 获取排序参数
     */
    public OrderItem getOrderItem() {
        if (StringUtils.isNotBlank(this.sortField)) {
            return new OrderItem(this.sortField, this.orderType.equals(SORT_ASC));
        }
        return null;
    }

    /**
     * 获取基础的分页对象
     */
    public <T> Page<T> getPage() {
        Page<T> page = new Page<>();
        page.setCurrent(this.current);
        page.setSize(this.limit);
        page.setSearchCount(this.searchCount);
        OrderItem orderItem = getOrderItem();
        if (ObjectUtils.isNotEmpty(orderItem)) {
            page.setOrders(Collections.singletonList(orderItem));
        }
        return page;
    }
}
