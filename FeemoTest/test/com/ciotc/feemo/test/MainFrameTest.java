package com.ciotc.feemo.test;

import java.awt.Dimension;
import java.io.File;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ciotc.feemo.MainFrame;

public class MainFrameTest {

	private FrameFixture window;

	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Before
	public void onSetUp() {
		MainFrame frame = GuiActionRunner.execute(new GuiQuery<MainFrame>() {
			protected MainFrame executeInEDT() {
				return new MainFrame();
			}
		});
		// IMPORTANT: note the call to 'robot()'
		// we must use the Robot from FestSwingTestngTestCase
		window = new FrameFixture(frame);
		window.show(new Dimension(800, 600));
	}

	@After
	public void tearDown() {
		window.cleanUp();
	}

	/**
	 * 测试打开文件
	 */
	@Test
	@Ignore("not execute")
	public void openMovie() {
		//Assert.assertEquals(window, null);
		window.button("open").click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File("C:\\Program Files\\teemo\\Database\\患者甲\\患者甲_2012-03-13_17-06-35.tmo");
		window.dialog().fileChooser().selectFile(file).approve();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 测试新建文件
	 */
	@Test
	@Ignore("not execute")
	public void newMovie() {
		//Assert.assertEquals(window, null);
		window.button("new").click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 测试新建并打开文件
	 */
	@Test
	public void openNewMovie() {
		//Assert.assertEquals(window, null);
		window.button("open").click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File("C:\\Program Files\\teemo\\Database\\患者甲\\患者甲_2012-03-13_17-06-35.tmo");
		window.dialog().fileChooser().selectFile(file).approve();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Assert.assertEquals(window, null);
		window.tabbedPane("OutlookBar").selectTab(1);
		
		window.button("new").click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
