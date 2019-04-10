package com.springboot.architecture.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * List根据指定字段排序
 * 
 * @author Jiangy
 * @date 2018年3月2日
 * 
 * 调用示例：new ListSortUtil<DeviceGroup>().mySort(m_list, "myFee", "asc");
 */
public class ListSortUtil<T> {
	/**
	 * 支持的排序字段类型：Date,String,int,Integer,double,Double,float,Float
	 * (该泛型必须符合javabean规范)
	 * 
	 * @param list
	 *            要排序的集合
	 * @param sortField
	 *            排序字段
	 * @param sortRule
	 *            排序规则：(asc or desc)
	 */
	public  void Sort(List<T> list, final String sortField, final String sortRule) {

		Collections.sort(list, new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				// 通过反射获取方法名称，如getId
				String methodName = "get" + sortField.substring(0, 1).toUpperCase()
						+ sortField.substring(1, sortField.length());
				String flag = null;
				try {
					Field[] fields = o1.getClass().getDeclaredFields();
					for (Field f : fields) {
						if (sortField.equals(f.getName())) {
							System.out.println(f.getName() + "  GenericType:  " + f.getGenericType().toString());
							if (f.getGenericType().toString().equals("class java.util.Date")) {
								flag = "Date";
								break;
							}
							if (f.getGenericType().toString().equals("class java.lang.String")) {
								flag = "String";
								break;
							}
							if (f.getGenericType().toString().equals("int")) {
								flag = "int";
								break;
							}
							if (f.getGenericType().toString().equals("class java.lang.Integer")) {
								flag = "Integer";
								break;
							}
							if (f.getGenericType().toString().equals("double")) {
								flag = "double";
								break;
							}
							if (f.getGenericType().toString().equals("class java.lang.Double")) {
								flag = "Double";
								break;
							}
							if (f.getGenericType().toString().equals("float")) {
								flag = "float";
								break;
							}
							if (f.getGenericType().toString().equals("class java.lang.Float")) {
								flag = "Float";
								break;
							}
						}
					}

					Method method1 = o1.getClass().getMethod(methodName, null);
					Method method2 = o2.getClass().getMethod(methodName, null);
					// System.out.println(method1.invoke(o1, null));
					// System.out.println(method2.invoke(o2, null));
					if ("Date".equals(flag)) {
						if ("desc".equals(sortRule)) {
							return ((Date) method1.invoke(o1, null)).getTime() < ((Date) method2.invoke(o2, null))
									.getTime() ? 1 : -1;
						} else {
							return ((Date) method1.invoke(o1, null)).getTime() < ((Date) method2.invoke(o2, null))
									.getTime() ? -1 : 1;
						}
					} else if ("String".equals(flag)) {
						if ("desc".equals(sortRule)) {
							return method2.invoke(o2, null).toString().compareTo(method1.invoke(o1, null).toString());
						} else {
							System.out.println("compare result : " + method1.invoke(o1, null).toString()
									.compareTo(method2.invoke(o2, null).toString()));
							return method1.invoke(o1, null).toString().compareTo(method2.invoke(o2, null).toString());
						}
					} else if ("int".equals(flag) || "Integer".equals(flag)) {
						if ("desc".equals(sortRule)) {
							return ((Integer) method2.invoke(o2, null)).compareTo((Integer) method1.invoke(o1, null));
						} else {
							return ((Integer) method1.invoke(o1, null)).compareTo((Integer) method2.invoke(o2, null));
						}
					} else if ("double".equals(flag) || "Double".equals(flag)) {
						if ("desc".equals(sortRule)) {
							return ((Double) method2.invoke(o2, null)).compareTo((Double) method1.invoke(o1, null));
						} else {
							return ((Double) method1.invoke(o1, null)).compareTo((Double) method2.invoke(o2, null));
						}
					} else if ("float".equals(flag) || "Float".equals(flag)) {
						if ("desc".equals(sortRule)) {
							return ((Float) method1.invoke(o1, null)) < ((Float) method2.invoke(o2, null)) ? 1 : -1;
						} else {
							return ((Float) method1.invoke(o1, null)) < ((Float) method2.invoke(o2, null)) ? -1 : 1;
						}
					}
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 0;
			}
		});

	}
}