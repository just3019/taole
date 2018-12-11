package org.demon.util;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.apachecommons.CommonsLog;
import org.demon.enums.EnumValue;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CommonsLog
public class JSONUtil {

    private static final Gson GSON = handleMapDouble(getBaseBuilder()).create();
    private static final Gson GSON_PRETTY = handleMapDouble(getBaseBuilder()).setPrettyPrinting().create();

    /**
     * 初始化规则
     */
    public static GsonBuilder getBaseBuilder() {
        GsonBuilder gb = new GsonBuilder();
        // 排除有@ExcludeField 的属性
        gb.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                ExcludeField annotation = f.getAnnotation(ExcludeField.class);
                return annotation != null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        // gb.serializeNulls();
        gb.registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
        gb.registerTypeAdapter(int.class, new IntegerTypeAdapter(true));
        gb.registerTypeAdapter(Long.class, new LongTypeAdapter());
        gb.registerTypeAdapter(long.class, new LongTypeAdapter(true));
        // 下面已经定义 , 如有问题 换成这个
        //        gb.registerTypeAdapter(Double.class, new DoubleTypeAdapter());
        //        gb.registerTypeAdapter(Double.class, new DoubleTypeAdapter(true));

        gb.registerTypeAdapter(Date.class, new DateGsonAdapter())
                .registerTypeAdapter(Timestamp.class, new DateGsonAdapter())
                .registerTypeAdapter(java.sql.Date.class, new DateGsonAdapter())
                .setDateFormat(DateFormat.LONG);
        gb.registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        });
        //处理枚举
        gb.registerTypeHierarchyAdapter(EnumValue.class, new JSONUtil.EnumValueGsonAdapter());
        gb.disableHtmlEscaping();
        return gb;
    }

    /**
     * 处理Map 推荐使用
     */
    public static GsonBuilder handleMapDouble(GsonBuilder gb) {
        // 处理Integer值被当成Double用的问题
        gb.registerTypeAdapter(new TypeToken<Map<String, Object>>() {
        }.getType(), new DoubleToIntMapTypeAdapter());
        return gb;
    }

    public static Gson getGson() {
        return GSON;
    }

    public static Gson getGsonPretty() {
        return GSON_PRETTY;
    }

    /**
     * 将json字符串转换为对象
     */
    public static <T> T json2Obj(String json, @NonNull JSONType<T> type) {
        return json2Obj(json, type.type);
    }

    /**
     * 将json字符串转换为对象
     */
    public static <T> T json2Obj(String json, @NonNull Type type) {
        if (isEmptyJson(json)) {
            return null;
        }
        try {
            return GSON.fromJson(json, type);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     * 将json字符串转换为对象
     */
    public static <T> T json2Obj(String json, @NonNull Class<T> c) {
        if (isEmptyJson(json)) {
            return null;
        }
        try {
            return GSON.fromJson(json, c);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     * JsonElement 转化对象
     */
    @Deprecated
    public static <T> T json2Obj(JsonElement json, JSONType<T> type) {
        if (json == null) {
            return null;
        }
        return GSON.fromJson(json, type.getType());
    }

    /**
     * JsonElement 转化对象
     */
    @Deprecated
    public static <T> T json2Obj(JsonElement json, Class<T> type) {
        if (json == null) {
            return null;
        }
        return GSON.fromJson(json, type);
    }


    /**
     * Map 转 对象
     *
     * @see #obj2Obj(Object, Class)
     */
    @Deprecated
    public static <T> T map2Obj(Map map, Class<T> c) {
        if (StringUtil.isEmpty(map)) {
            return null;
        }
        try {
            return GSON.fromJson(GSON.toJson(map), c);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     * 对象转换对象 <br/>
     * 效率大于BeanUtils.copyProperties()
     */
    public static <T> T obj2Obj(Object object, Class<T> c) {
        if (StringUtil.isNull(object)) {
            return null;
        }
        return GSON.fromJson(GSON.toJson(object), c);
    }

    /**
     * 对象转换对象
     */
    public static <T> T obj2Obj(Object object, JSONType<T> type) {
        return obj2Obj(object, type.type);
    }

    /**
     * 对象转换对象
     */
    public static <T> T obj2Obj(Object object, Type type) {
        if (StringUtil.isNull(object)) {
            return null;
        }
        try {
            return GSON.fromJson(GSON.toJson(object), type);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     * 检查空JSON
     */
    private static boolean isEmptyJson(String json) {
        if (StringUtil.isEmpty(json) || json.matches("\\{\\s*\\}")) {
            return true;
        }
        return false;
    }

    /**
     * 将对象转换成字符串
     */
    public static <T> String obj2Json(T t) {
        return GSON.toJson(t);
    }

    /**
     * 将对象转换成字符串
     */
    public static <T> String obj2PrettyJson(T t) {
        return GSON_PRETTY.toJson(t);
    }

    /**
     * 验证JSON是否合法
     *
     * @param json json串
     */
    public static boolean validate(String json) {
        return new JSONValidator().validate(json);
    }

    /**
     * 解析json中某个属性的值
     *
     * @param jsonObject Json 对象
     * @param attrs      "aa.bb.cc"
     */
    public static String getJsonNodeValue(JsonObject jsonObject, String attrs) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (jsonObject != null && jsonObject.get(attrs) != null) {
                return jsonObject.get(attrs).getAsString();
            }
            return "";
        } else {
            JsonElement pEle = jsonObject.get(attrs.substring(0, index));
            if (pEle == null) {
                return "";
            }
            return getJsonNodeValue(pEle.getAsJsonObject(), attrs.substring(index + 1));
        }
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ExcludeField {

    }

    public static class DateGsonAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        }
    }

    public static class EnumValueGsonAdapter implements JsonSerializer<EnumValue>, JsonDeserializer<EnumValue> {

        public JsonElement serialize(EnumValue src, Type typeOfSrc, JsonSerializationContext context) {
            if (!(src instanceof Enum)) {
                throw new UnsupportedOperationException("EnumValue mush Emnu class");
            }
            Object obj = src.getValue();
            if (obj instanceof String) {
                return new JsonPrimitive((String) src.getValue());
            } else if (obj instanceof Number) {
                return new JsonPrimitive((Number) src.getValue());
            } else if (obj instanceof Character) {
                return new JsonPrimitive((Character) src.getValue());
            } else if (obj instanceof Boolean) {
                return new JsonPrimitive((Boolean) src.getValue());
            } else {
                throw new UnsupportedOperationException("Generic Just Support String,Number,Character,Boolean");
            }
        }

        public EnumValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Class classOfT = null;
            try {
                classOfT = typeOfT instanceof Class ? (Class) typeOfT : Class.forName(typeOfT.getTypeName());
            } catch (ClassNotFoundException e) {
                log.error(e);
            }
            if (classOfT == null || !classOfT.isEnum()) {
                throw new UnsupportedOperationException("EnumValue must Enum class");
            }
            try {
                return locateEnumValue(classOfT, json.getAsJsonPrimitive().getAsInt());
            } catch (Exception e) {
                return locateEnumValue(classOfT, json.getAsJsonPrimitive().getAsString());
            }
        }

        private EnumValue locateEnumValue(Class classOfT, String value) {
            EnumValue[] enums = (EnumValue[]) classOfT.getEnumConstants();
            for (EnumValue en : enums) {
                if (en.name().equals(value)) {
                    return en;
                }
            }
            return null;
        }

        private EnumValue locateEnumValue(Class classOfT, int value) {
            EnumValue[] enums = (EnumValue[]) classOfT.getEnumConstants();
            for (EnumValue en : enums) {
                if (en.getValue().equals(value)) {
                    return en;
                }
            }
            throw new IllegalArgumentException("unknow value：" + value + ",class:" + classOfT.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public static class NullBeanToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType.getPackage().getName().startsWith("org.sean.framework")) {
                try {
                    return new BeanNullAdapter<T>(rawType);
                } catch (Exception e) {
                    log.error(e);
                }
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static class BeanNullAdapter<T> extends TypeAdapter<T> {
        //bean 类型
        private final Class<T> clazz;

        public BeanNullAdapter(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public T read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            return JSONUtil.json2Obj(reader.nextString(), clazz);
        }

        @Override
        public void write(JsonWriter writer, T value) throws IOException {
            if (value == null) {
                try {
                    value = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (value == null) {
                return;
            }

            if (clazz.getPackage().getName().startsWith("org.sean.framework")) {
                writer.beginObject();
                Field[] fileds = clazz.getFields();
                Package pkg = null;
                for (Field field : fileds) {
                    pkg = field.getType().getPackage();
                    if (pkg == null) {
                        continue;
                    }
                    if (pkg.getName().startsWith("org.sean.framework")) {
                        System.out.println("");
                    }
                }
                writer.endObject();
            }

            TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) GSON.getAdapter(value.getClass());
            if (typeAdapter instanceof ObjectTypeAdapter) {
                writer.beginObject();
                writer.endObject();
                return;
            }

            typeAdapter.write(writer, value);
        }
    }

    public static class DoubleToIntMapTypeAdapter extends TypeAdapter<Object> {
        private final TypeAdapter<Object> delegate =
                getBaseBuilder().create().getAdapter(new TypeToken<Object>() {
                });

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            delegate.write(out, value);
        }

        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;
                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;
                case STRING:
                    return in.nextString();
                case NUMBER:
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }

                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        // 判断是不是Integer
                        if (lngNum > Integer.MAX_VALUE) {
                            return lngNum;
                        } else {
                            return (int) lngNum;
                        }
                    } else {
                        return dbNum;
                    }
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    public static class LongTypeAdapter extends TypeAdapter<Long> {

        private boolean isPrimitive;

        LongTypeAdapter() {
        }

        LongTypeAdapter(boolean isPrimitive) {
            this.isPrimitive = isPrimitive;
        }

        private Long getEmptyValue() {
            return isPrimitive ? 0L : null;
        }

        @Override
        public void write(JsonWriter out, Long value) {
            try {
                if (value == null) {
                    value = getEmptyValue();
                }
                out.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Long read(JsonReader in) {
            Long value = getEmptyValue();
            try {
                switch (in.peek()) {
                    case NULL:
                        in.nextNull();
                        break;
                    case BOOLEAN:
                        in.nextBoolean();
                        break;
                    case STRING:
                        value = Long.parseLong(in.nextString());
                        break;
                    default:
                        value = in.nextLong();
                        break;

                }
            } catch (Exception e) {
                log.error(e);
            }
            return value;
        }
    }

    public static class DoubleTypeAdapter extends TypeAdapter<Double> {

        private boolean isPrimitive;

        DoubleTypeAdapter() {
        }

        DoubleTypeAdapter(boolean isPrimitive) {
            this.isPrimitive = isPrimitive;
        }

        private Double getEmptyValue() {
            return isPrimitive ? 0d : null;
        }

        @Override
        public void write(JsonWriter out, Double value) {
            try {
                if (value == null) {
                    value = getEmptyValue();
                }
                if (value == value.longValue()) {
                    out.value(value.longValue());
                } else {
                    out.value(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Double read(JsonReader in) {
            Double value = getEmptyValue();
            try {
                switch (in.peek()) {
                    case NULL:
                        in.nextNull();
                        break;
                    case BOOLEAN:
                        in.nextBoolean();
                        break;
                    case STRING:
                        value = Double.parseDouble(in.nextString());
                        break;
                    default:
                        value = in.nextDouble();
                        break;

                }
            } catch (Exception e) {
                log.error(e);
            }
            return value;
        }
    }

    public static class IntegerTypeAdapter extends TypeAdapter<Integer> {

        private boolean isPrimitive;

        IntegerTypeAdapter() {
        }

        IntegerTypeAdapter(boolean isPrimitive) {
            this.isPrimitive = isPrimitive;
        }

        private Integer getEmptyValue() {
            return isPrimitive ? 0 : null;
        }

        @Override
        public void write(JsonWriter out, Integer value) {
            try {
                if (value == null) {
                    value = getEmptyValue();
                }
                out.value(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Integer read(JsonReader in) {
            Integer value = getEmptyValue();
            try {
                switch (in.peek()) {
                    case NULL:
                        in.nextNull();
                        break;
                    case BOOLEAN:
                        in.nextBoolean();
                        break;
                    case STRING:
                        value = Integer.parseInt(in.nextString());
                        break;
                    default:
                        value = in.nextInt();
                        break;

                }
            } catch (Exception e) {
                log.error(e);
            }
            return value;
        }
    }

}
