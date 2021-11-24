package org.apache.solr.handler.dataimport;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 15/08/12
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public class MongoMapperTransformer extends Transformer {
    //To identify the created_at field
    public static final String TIMESTAMP = "timestamp";
    @Override
    public Object transformRow(Map<String, Object> row, Context context) {

        for (Map<String, String> map : context.getAllEntityFields()) {
            String mongoFieldName = map.get(MONGO_FIELD);
            String mongoId = map.get(MONGO_ID);
            String timestamp = map.get(TIMESTAMP);
            if (mongoFieldName == null)
                continue;

            String columnFieldName = map.get(DataImporter.COLUMN);

            //If the field is ObjectId convert it into String
            if (Boolean.parseBoolean(mongoId)) {
                Object srcId = row.get(columnFieldName);
                row.put(columnFieldName, srcId.toString());
            }
            //If the field is Timestamp convert it into parsable format
            else if (Boolean.parseBoolean(timestamp)) {
                String ts = row.get(columnFieldName).toString();
                if (ts.contains("+")) {
                    ts = ts.split("\\+")[0] + 'Z';
                }
                row.put(columnFieldName, ts);
            } else {
                row.put(columnFieldName, row.get(mongoFieldName));
            }
        }

        return row;
    }


    public static final String MONGO_FIELD = "mongoField";

    //To identify the _id field
    public static final String MONGO_ID = "objectIdToString";
}
