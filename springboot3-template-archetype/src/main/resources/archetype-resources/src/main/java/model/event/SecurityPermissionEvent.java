package ${package}.model.event;

import ${package}.common.model.event.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author alex meng
 * @createDate 2023-12-30 14:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SecurityPermissionEvent extends BaseEvent<String> {

}
