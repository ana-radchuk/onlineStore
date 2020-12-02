package ua.lviv.lgs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.lviv.lgs.dao.UserDao;
import ua.lviv.lgs.domain.User;
import ua.lviv.lgs.utils.ConnectionUtils;

public class UserDaoImpl implements UserDao {

	private static String READ_ALL = "select * from user";
	private static String CREATE = "insert into user(`first_name`, `last_name`, `email`, `password`, `role`) values (?,?,?,?,?)";
	private static String READ_BY_ID = "select * from user where id =?";
	private static String READ_BY_EMAIL = "select * from user where email=?";
	private static String UPDATE_BY_ID = "update user set first_name=?, last_name = ?, email = ?, password =?, role=? where id = ?";
	private static String DELETE_BY_ID = "delete from user where id=?";

	private static Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

	private Connection connection;
	private PreparedStatement preparedStatement;

	public UserDaoImpl() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		connection = ConnectionUtils.openConnection();
	}

	@Override
	public User create(User user) {
		try {
			preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getEmail());
			preparedStatement.setString(4, user.getPassword());
			preparedStatement.setString(5, user.getRole());
			preparedStatement.executeUpdate();

			ResultSet rs = preparedStatement.getGeneratedKeys();
			rs.next();
			user.setId(rs.getInt(1));
		} catch (SQLException e) {
			LOGGER.error(e);
		}

		return user;
	}

	@Override
	public User read(Integer id) {
		User user = null;
		try {
			preparedStatement = connection.prepareStatement(READ_BY_ID);
			preparedStatement.setInt(1, id);
			ResultSet result = preparedStatement.executeQuery();
			result.next();

			Integer userId = result.getInt("id");
			String firstName = result.getString("first_name");
			String lastName = result.getString("last_name");
			String email = result.getString("email");
			String password = result.getString("password");
			String role = result.getString("role");
			user = new User(userId, firstName, lastName, email, password, role);

		} catch (SQLException e) {
			LOGGER.error(e);
		}

		return user;
	}

	@Override
	public User update(User user) {
		try {
			preparedStatement = connection.prepareStatement(UPDATE_BY_ID);
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setString(2, user.getFirstName());
			preparedStatement.setString(3, user.getLastName());
			preparedStatement.setString(4, user.getEmail());			
			preparedStatement.setString(5, user.getPassword());
			preparedStatement.setString(6, user.getRole());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e);
		}

		return user;

	}

	@Override
	public void delete(Integer id) {
		try {
			preparedStatement = connection.prepareStatement(DELETE_BY_ID);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e);
		}
	}

	@Override
	public List<User> readAll() {
		List<User> userRecords = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(READ_ALL);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				Integer userId = result.getInt("id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String email = result.getString("email");
				String password = result.getString("password");
				String role = result.getString("role");
				userRecords.add(new User(userId, firstName, lastName, email, password, role));
			}
		} catch (SQLException e) {
			LOGGER.error(e);
		}

		return userRecords;
	}

	@Override
	public User getUserByEmail(String email) {
		User user = null;
		try {
			preparedStatement = connection.prepareStatement(READ_BY_EMAIL);
			preparedStatement.setString(1, email);
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				Integer userId = result.getInt("id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String password = result.getString("password");
				String role = result.getString("role");
				user = new User(userId, firstName, lastName, email, password, role);
			}
		} catch (SQLException e) {
			LOGGER.error(e);
		}

		return user;

	}

}