package com.ams.poi.xls2sql.jerseymaker;

/**
 * @author Akihiko Monden
 * 
 */
public class WebAPIMethod {
	/**
	 * @return the tableNameLogic
	 */
	public String getTableNameLogic() {
		return tableNameLogic;
	}

	/**
	 * @param tableNameLogic
	 *            the tableNameLogic to set
	 */
	public void setTableNameLogic(String tableNameLogic) {
		this.tableNameLogic = tableNameLogic;
	}

	/**
	 * @return the tableNamePhysics
	 */
	public String getTableNamePhysics() {
		return tableNamePhysics;
	}

	/**
	 * @param tableNamePhysics
	 *            the tableNamePhysics to set
	 */
	public void setTableNamePhysics(String tableNamePhysics) {
		this.tableNamePhysics = tableNamePhysics;
	}

	/**
	 * @return the methodGet
	 */
	public boolean isMethodGet() {
		return methodGet;
	}

	/**
	 * @param methodGet
	 *            the methodGet to set
	 */
	public void setMethodGet(boolean methodGet) {
		this.methodGet = methodGet;
	}

	/**
	 * @return the methodPost
	 */
	public boolean isMethodPost() {
		return methodPost;
	}

	/**
	 * @param methodPost
	 *            the methodPost to set
	 */
	public void setMethodPost(boolean methodPost) {
		this.methodPost = methodPost;
	}

	/**
	 * @return the methodPut
	 */
	public boolean isMethodPut() {
		return methodPut;
	}

	/**
	 * @param methodPut
	 *            the methodPut to set
	 */
	public void setMethodPut(boolean methodPut) {
		this.methodPut = methodPut;
	}

	/**
	 * @return the methodDelete
	 */
	public boolean isMethodDelete() {
		return methodDelete;
	}

	/**
	 * @param methodDelete
	 *            the methodDelete to set
	 */
	public void setMethodDelete(boolean methodDelete) {
		this.methodDelete = methodDelete;
	}

	private String tableNameLogic;
	private String tableNamePhysics;
	private boolean methodGet;
	private boolean methodPost;
	private boolean methodPut;
	private boolean methodDelete;

}