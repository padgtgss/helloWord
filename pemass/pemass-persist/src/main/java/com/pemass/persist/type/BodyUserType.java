package com.pemass.persist.type;

import com.pemass.common.core.json.JacksonObjectMapper;
import com.pemass.common.core.pojo.Body;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: List\<Body\> 持久化转换
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:48
 */
public class BodyUserType implements UserType {

    private ObjectMapper jacksonObjectMapper = new JacksonObjectMapper();

    private static final int[] SQL_TYPES = {Types.VARCHAR};

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class<List> returnedClass() {
        return List.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        try {
            String bodyString = rs.getString(names[0]);
            if (StringUtils.isBlank(bodyString)) {
                return null;
            }
            return jacksonObjectMapper.readValue(bodyString, new TypeReference<List<Body>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        try {
            if (value == null) {
                st.setNull(index, Types.VARCHAR);
            } else {
                String bodyString = jacksonObjectMapper.writeValueAsString(value);
                st.setString(index, bodyString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y)
            return true;
        if (x != null && y != null) {
            List xList = (List) x;
            List yList = (List) y;

            if (xList.size() != yList.size())
                return false;

            for (int i = 0; i < xList.size(); i++) {
                Body strx = (Body) xList.get(i);
                Body stry = (Body) yList.get(i);
                if (!(strx.equals(stry)))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }


    @Override
    public Object deepCopy(Object value) throws HibernateException {
        List sourcelist = (List) value;
        List targetlist = new ArrayList();
        if (sourcelist != null) {
            targetlist.addAll(sourcelist);
        }
        return targetlist;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }
} 


