package com.jdc.phoneshop.common.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.jdc.phoneshop.admin.model.entity.Employee.Role;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice.PriceStatus;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory.Operation;

public abstract class Dao<T> {
	
	private static final String SELECT = "select * from %s";
	private static final String INSERT = "insert into %s (%s) values (%s)";
	private static final String DELETE = "delete from %s where %s";
	private static final String UPDATE = "update %s set %s where %s";
	
	private Class<T> type;
	
	/**
	 * Constructor
	 * 
	 * @param type Class of Entity Object
	 */
	public Dao(Class<T> type) {
		super();
		this.type = type;
	}

	/**
	 * To find objects from the table
	 * 
	 * @param where WHERE Clause
	 * @param params Parameters of WHERE Clause
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public List<T> find(String where, List<Object> params) throws InstantiationException, IllegalAccessException {
		List<T> list = new ArrayList<>();
		// build sql
		String sql = String.format(SELECT, getTableName());

		if(null != where && null != params && params.size() > 0) {
			sql = sql.concat(" where ").concat(where);
		}

		
		// create statement
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			// set params
			if(null != params && params.size() > 0) {
				// set parameters
				for (int i = 0; i < params.size(); i++) {
					
					Object obj = convertType(params.get(i));
					stmt.setObject(i+1, obj);
				}
			}
			
			// execute query
			ResultSet rs = stmt.executeQuery();
			
			// get result data
			while(rs.next()) {
				list.add(getObject(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	

	/**
	 * 
	 * 
	 * @param t
	 * @param conn
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void create(T t, Connection conn) throws SQLException, IllegalArgumentException, IllegalAccessException {
		
		String sql = String.format(INSERT, getTableName(), getInsertColumns(), getInsertValues());
		List<Object> params = getInsertParams(t);
		
		boolean isAutoGeneate = isAutoGenerate();
		PreparedStatement stmt = null;
		
		// create statement
		if(isAutoGeneate) {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} else {
			stmt= conn.prepareStatement(sql);
		}
		
		// set parameters
		for (int i = 0; i < params.size(); i++) {
			
			Object obj = convertType(params.get(i));
			stmt.setObject(i+1, obj);
		}
		
		// execute update
		stmt.executeUpdate();
		
		// set generated primary key
		if(isAutoGeneate) {
			ResultSet rs = stmt.getGeneratedKeys();
			while(rs.next()) {
				int key = rs.getInt(1);
				setKey(key, t);
			}
		}
	}

	/**
	 * Delete data from table
	 * 
	 * @param where
	 * @param params
	 * @param conn
	 * @throws SQLException
	 */
	public void delete(String where, List<Object> params, Connection conn) throws SQLException {
		// build sql
		String sql = String.format(DELETE, getTableName(), where);
		
		// create statement
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		// set parameters
		for (int i = 0; i < params.size(); i++) {
			
			Object obj = convertType(params.get(i));
			stmt.setObject(i+1, obj);
		}
		
		stmt.executeUpdate();
		
	}
	
	/**
	 * Update to Database
	 * 
	 * @param set SET Clause of SQL
	 * @param where WHERE Clause of SQL
	 * @param params Parameter List
	 * @param conn Connection Object
	 * 
	 * @throws SQLException
	 */
	public void update(String set, String where, List<Object> params, Connection conn) throws SQLException {
		// build sql
		String sql = String.format(UPDATE, getTableName(), set, where);
		
		// create statement
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		// set parameters
		for (int i = 0; i < params.size(); i++) {
			
			Object obj = convertType(params.get(i));
			stmt.setObject(i+1, obj);
		}
		
		stmt.executeUpdate();

	}
	
	/**
	 * Find this table is need to generate PK or not
	 * 
	 * @return
	 */
	private boolean isAutoGenerate() {
		
		// get Table Annotation
		Table table = type.getAnnotation(Table.class);
		if(null != table) {
			return table.autoGenerate();
		}
		
		return false;
	}

	/**
	 * Set Generated PK to Data Object
	 * 
	 * @param key
	 * @param t
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setKey(int key, T t) throws IllegalArgumentException, IllegalAccessException {
		
		// get fields from class
		Field[] fs = type.getDeclaredFields();
		
		for(Field f : fs) {
			// get generated id annotation from field
			GeneratedID id = f.getAnnotation(GeneratedID.class);
			if(null != id) {
				f.setAccessible(true);
				f.set(t, key);
			}
		}
		
	}

	/**
	 * Convert Java Type to Database Type
	 * 
	 * @param object
	 * @return
	 */
	private Object convertType(Object object) {
		
		if(object instanceof LocalDate) {
			// local date
			return Date.valueOf((LocalDate)object);
			
		} else if(object instanceof LocalDateTime) {
			// local date time
			return Timestamp.valueOf((LocalDateTime)object);
			
		} else if(object instanceof PriceStatus) {
			// PriceStatus
			PriceStatus[] array = PriceStatus.values();
			for (int i = 0; i < array.length; i++) {
				if(object.equals(array[i])) {
					return i;
				}
			}
		} else if(object instanceof PurchaseStatus) {
			// PurchaseStatus
			PurchaseStatus[] array = PurchaseStatus.values();
			for (int i = 0; i < array.length; i++) {
				if(object.equals(array[i])) {
					return i;
				}
			}
		} else if(object instanceof Role) {
			// Role
			Role[] array = Role.values();
			for (int i = 0; i < array.length; i++) {
				if(object.equals(array[i])) {
					return i;
				}
			}
		} else if(object instanceof Operation) {
			// Operation
			Operation[] array = Operation.values();
			for (int i = 0; i < array.length; i++) {
				if(object.equals(array[i])) {
					return i;
				}
			}
		}
		
		return object;
	}
	
	/**
	 * Convert Database Type to Java Type
	 * 
	 * @param obj
	 * @param f
	 * @return
	 */
	private Object revertType(Object obj, Field f) {
		if(f.getType().equals(LocalDate.class)) {
			if(null != obj) {
				// local date
				Date date = (Date)obj;
				return date.toLocalDate();
			}
		} else if(f.getType().equals(LocalDateTime.class)) {
			// local date time
			Timestamp date = (Timestamp)obj;
			return date.toLocalDateTime();
			
		} else if(f.getType().equals(PriceStatus.class)) {
			// PriceStatus
			int data = (int)obj;
			PriceStatus[] array = PriceStatus.values();
			return array[data];
		} else if(f.getType().equals(PurchaseStatus.class)) {
			// PurchaseStatus
			int data = (int)obj;
			PurchaseStatus[] array = PurchaseStatus.values();
			return array[data];
		} else if(f.getType().equals(Role.class)) {
			// PriceStatus
			int data = (int)obj;
			Role[] array = Role.values();
			return array[data];
		} else if(f.getType().equals(Operation.class)) {
			// PriceStatus
			int data = (int)obj;
			Operation[] array = Operation.values();
			return array[data];
		}
		
		return obj;
	}

	/**
	 * Get Table Name
	 * 
	 * @return Table Name
	 */
	private String getTableName() {
		
		Table table = type.getAnnotation(Table.class);
		if(null != table && !table.name().isEmpty()) {
			return table.name();
		}
		
		return type.getSimpleName().toLowerCase();
	}

	/**
	 * Get Object from resultset
	 * 
	 * @param rs
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	protected T getObject(ResultSet rs) throws InstantiationException, IllegalAccessException, SQLException {
		T t = type.newInstance();
		
		Field [] fs = type.getDeclaredFields();
		
		for(Field f : fs) {
			String colName = getColumnName(f);
			Object value = rs.getObject(colName);
			f.setAccessible(true);
			f.set(t, revertType(value, f));
		}
		
		return t;
	}
	
	/**
	 * Get Column Name
	 * 
	 * @param f
	 * @return
	 */
	private String getColumnName(Field f) {
		Column column = f.getAnnotation(Column.class);
		if(null != column) {
			return column.name();
		}
		return f.getName().toLowerCase();
	}

	protected abstract String getInsertColumns();
	protected abstract String getInsertValues();
	protected abstract List<Object> getInsertParams(T t);
	
}
