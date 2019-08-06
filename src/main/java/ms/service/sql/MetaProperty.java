package ms.service.sql;

import java.util.Map;

public interface MetaProperty {

    String select(MetaEntity metaEntity);

    void set(Object object, Map<String, Object> data);
}
