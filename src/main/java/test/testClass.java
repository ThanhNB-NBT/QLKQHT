package test;

import models.dao.ClassDAO;
import models.bean.Class;

public class testClass {
	public static void main(String[] args) {

		Class cls = ClassDAO.getClassById(1);

		System.out.print(cls);

	}

}
