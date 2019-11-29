package snod.com.cn.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;

public class LambadaTest {
	public LambadaTest() {}
	public LambadaTest(String s) {
		System.out.println("构造方法："+s);
	}
	public static void test(String s) {
		System.out.println("静态方法："+s);
	}
	public void test_two(String s) {
		System.out.println("普通方法:"+s);
	}
	
	public static void main(String[] args) {
		
		
		/**1.-----------------------------------------------------------------------------*/
		Thread t=new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Before Java8, too much code for too little to do");
			}
		
		});
		t.start();
		new Thread(()->{
			System.out.println("asdqasd");
			}
		).start();
		/**2.-----------------------------------------------------------------------------*/
		JButton j=new JButton();
		j.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(123);
				}
		});
		
		j.addActionListener((s)->{
			System.out.println(s.getID());
		});
		/**3.-----------------------------------------------------------------------------*/
		List<String> s=Arrays.asList("123","java");
		for (String string : s) {
			System.out.println(string);
		}
		s.forEach((x)->System.out.println(x));
		s.forEach(System.out::println);
		/**4.-----------------------------------------------------------------------------*/
		Consumer<String> methodParam = LambadaTest::test; //方法参数
		methodParam.accept("123");
		LambadaTest lambadaTest=new LambadaTest();
		Consumer<String> methodParams = lambadaTest::test_two; //方法参数
		methodParams.accept("456");
		Consumer<String> methodParamss = LambadaTest::new; //方法参数
		methodParamss.accept("789");
	}
}
