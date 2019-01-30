package com.stackroute.datamunger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */
	
	

	public String[] getSplitStrings(String queryString) {
		return queryString.split(" ");
	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(String queryString) {

		Pattern pattern = Pattern.compile(
				"from\\s+(?:\\w+\\.)*(\\w+)($|\\s+[WHERE,JOIN,START\\s+WITH,ORDER\\s+BY,GROUP\\s+BY])",
				Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(queryString);
		while (match.find()) {
			queryString = match.group(0);
		}
		String[] fileName = queryString.split(" ");

		return fileName[1];
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */

	public String getBaseQuery(String queryString) {
		String baseQuery = queryString.split("order by")[0].split("group by")[0].split("where")[0];
		return baseQuery;
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords as a
	 * substring. For eg: from_city,job_order_no,group_no etc. 2. The field name can
	 * contain '*'
	 * 
	 */

	public String[] getFields(String queryString) {

		String[] requiredfields = queryString.split("select")[1].trim().split("from")[0].split("[\\s,]+");
		for (String field : requiredfields) {

		}
		return requiredfields;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note: 1. The field name or value in the condition can contain keywords as a
	 * substring. For eg: from_city,job_order_no,group_no etc. 2. The query might
	 * not contain where clause at all.
	 */

	public String getConditionsPartQuery(String queryString) {

		queryString = queryString.toLowerCase();
		if (queryString.contains("where")) {
			String whereCondition = queryString.split("order by")[0].split("group by")[0].split("where")[1];

			return whereCondition;
		} else {
			return null;
		}
	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {

		queryString = queryString.toLowerCase();
		String[] nameAndValue;
		String propertyName, propertyValue, conditionalOperator;
		int counter = 1;
		if (queryString.contains("where")) {
			String whereCondition = queryString.split("order by")[0].trim().split("group by")[0].trim()
					.split("where")[1].trim();
			// can we change this line without using regex
			String[] conditions = whereCondition.split("\\s+and\\s+|\\s+or\\s+");
			for (String condition : conditions) {
				nameAndValue = condition.split("<=|>=|!=|=|<|>");
				propertyName = nameAndValue[0].trim();
				propertyValue = nameAndValue[1].trim();
				conditionalOperator = condition.split(propertyName)[1].trim().split(propertyValue)[0].trim();
				counter++;
			}
			return conditions;
		} else {
			return null;
		}
	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note: 1. AND/OR keyword
	 * will exist in the query only if where conditions exists and it contains
	 * multiple conditions. 2. AND/OR can exist as a substring in the conditions as
	 * well. For eg: name='Alexander',color='Red' etc. Please consider these as well
	 * when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {
		queryString = queryString.toLowerCase();
		String[] logicalOperators;
		// dont use an arraylist in this assignment
		List<String> operators = new ArrayList<>();

		if (queryString.contains("where")) {
			String whereCondition = queryString.split("order by")[0].trim().split("group by")[0].trim()
					.split("where")[1].trim();
			// avoid regex
			String[] conditions = whereCondition.split("\\s+");
			for (String word : conditions) {
				if (word.equals("and")) {
					operators.add("and");
					// logicalOperators=new String[]{"and"};
				} else if (word.equals("or")) {
					operators.add("or");
					// logicalOperators=new String[]{"or"};
				}
			}
			logicalOperators = new String[operators.size()];
			logicalOperators = operators.toArray(logicalOperators);
			return logicalOperators;
		} else {
			return null;
		}

	}

	/*
	 * This method extracts the order by fields from the query string. Note: 1. The
	 * query string can contain more than one order by fields. 2. The query string
	 * might not contain order by clause at all. 3. The field names,condition values
	 * might contain "order" as a substring. For eg:order_number,job_order Consider
	 * this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {

		if (queryString.contains("order by")) {
			// avoid regex
			String[] orderByField = queryString.split("order by")[1].trim().split("[\\s,]+");
			return orderByField;
		} else {
			return null;
		}
	}

	/*
	 * This method extracts the group by fields from the query string. Note: 1. The
	 * query string can contain more than one group by fields. 2. The query string
	 * might not contain group by clause at all. 3. The field names,condition values
	 * might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {

		if (queryString.contains("group by")) {
			// avoid regex
			String[] groupByField = queryString.split("group by")[1].trim().split("[\\s,]+");
			return groupByField;
		} else {
			return null;
		}
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note: 1.
	 * aggregate functions will start with "sum"/"count"/"min"/"max"/"avg" followed
	 * by "(" 2. The field names might contain"sum"/"count"/"min"/"max"/"avg" as a
	 * substring. For eg: account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {

		String aggregateName, aggregateField;
		int counter = 1;
		if (queryString.contains("count") || queryString.contains("sum") || queryString.contains("min")
				|| queryString.contains("max") || queryString.contains("avg")) {
			// please check the requirements. Is it actually required?
			String[] aggregateFunctions = queryString.split("select")[1].trim().split("from")[0].trim().split(",");
			for (String function : aggregateFunctions) {
				aggregateName = function.split("\\(")[0].trim();
				aggregateField = function.split("\\(")[1].trim().split("\\)")[0].trim();
				counter++;
			}
			return aggregateFunctions;
		} else {
			return null;
		}
	}

}
