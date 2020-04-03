package com.lrj.VO;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public class UserLevelVo {

	private Integer currentPoints;

	private String currentLevelName;

	private String nextLevelName;

	private Integer nextLevelPoints;

	/** 下一等级还差积分 **/
	public Integer getNextLevelNeedPoints() {

		/** 积分差 **/
		int differ = nextLevelPoints - currentPoints;

		if (differ <= 0) {
			return 0;
		} else {
			return differ;
		}

	}

	/**
	 * currentPoints
	 * 
	 * @return the currentPoints
	 * @since CodingExample Ver(编码范例查看) 1.0
	 */
	public Integer getCurrentPoints() {

		return currentPoints;
	}

	/**
	 * @param currentPoints
	 *            the currentPoints to set
	 */
	public void setCurrentPoints(Integer currentPoints) {

		this.currentPoints = currentPoints;
	}

	/**
	 * currentLevelName
	 * 
	 * @return the currentLevelName
	 * @since CodingExample Ver(编码范例查看) 1.0
	 */
	public String getCurrentLevelName() {

		return currentLevelName;
	}

	/**
	 * @param currentLevelName
	 *            the currentLevelName to set
	 */
	public void setCurrentLevelName(String currentLevelName) {

		this.currentLevelName = currentLevelName;
	}

	/**
	 * nextLevelName
	 * 
	 * @return the nextLevelName
	 * @since CodingExample Ver(编码范例查看) 1.0
	 */
	public String getNextLevelName() {

		return nextLevelName;
	}

	/**
	 * @param nextLevelName
	 *            the nextLevelName to set
	 */
	public void setNextLevelName(String nextLevelName) {

		this.nextLevelName = nextLevelName;
	}

	/**
	 * nextLevelPoints
	 * 
	 * @return the nextLevelPoints
	 * @since CodingExample Ver(编码范例查看) 1.0
	 */
	public Integer getNextLevelPoints() {

		return nextLevelPoints;
	}

	/**
	 * @param nextLevelPoints
	 *            the nextLevelPoints to set
	 */
	public void setNextLevelPoints(Integer nextLevelPoints) {

		this.nextLevelPoints = nextLevelPoints;
	}

}
