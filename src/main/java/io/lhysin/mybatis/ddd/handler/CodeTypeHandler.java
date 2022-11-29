package io.lhysin.mybatis.ddd.handler;

import io.lhysin.mybatis.ddd.spec.Code;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * The type Code type handler.
 *
 * @param <E> the type parameter
 */
public class CodeTypeHandler<E extends Enum<E>> extends BaseTypeHandler<Code> {

    private final Class<E> type;

    /**
     * Instantiates a new Code type handler.
     *
     * @param type the type
     */
    public CodeTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Code parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Code getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return getCode(code);
    }

    @Override
    public Code getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return getCode(code);
    }

    @Override
    public Code getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return getCode(code);
    }

    /**
     * String to Enum implement Code
     * @param str code String
     * @return Code
     */
    private Code getCode(String str) {
        return Arrays.stream(type.getEnumConstants())
                .filter(Code.class::isInstance)
                .map(Code.class::cast)
                .filter(code -> code.getCode().equals(str))
                .findFirst()
                .orElse(null);
    }
}